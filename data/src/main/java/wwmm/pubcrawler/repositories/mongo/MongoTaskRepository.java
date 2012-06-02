package wwmm.pubcrawler.repositories.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import org.joda.time.DateTime;
import wwmm.pubcrawler.crawler.Task;
import wwmm.pubcrawler.inject.Tasks;
import wwmm.pubcrawler.repositories.TaskRepository;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import static java.util.Arrays.asList;

/**
 * @author Sam Adams
 */
public class MongoTaskRepository implements TaskRepository {

    private final DBCollection collection;
    private final MongoTaskMapper mongoTaskMapper;

    @Inject
    public MongoTaskRepository(@Tasks final DBCollection collection, final MongoTaskMapper mongoTaskMapper) {
        this.collection = collection;
        this.mongoTaskMapper = mongoTaskMapper;
        this.collection.ensureIndex(new BasicDBObject("id", 1), "id_index", true);
    }

    @Override
    public Task getTask(final String taskId) {
        final BasicDBObject query = new BasicDBObject("id", taskId);
        final BasicDBObject update = new BasicDBObject("$set", new BasicDBObject("run", true));
        final BasicDBObject task = (BasicDBObject) collection.findAndModify(query, update);
        return mongoTaskMapper.mapBsonToTask(task);
    }

    @Override
    public List<String> getWaitingTaskIds(final String filter) {
        final BasicDBObject query = new BasicDBObject("$and", Arrays.asList(
                                                                            new BasicDBObject("id", Pattern.compile("^" + Pattern.quote(filter))),
                                                                            new BasicDBObject("run", new BasicDBObject("$ne", true))
        ));

        final List<String> tasks = new ArrayList<String>();
        final DBCursor cursor = collection.find(query);
        try {
            while (cursor.hasNext()) {
                final DBObject task = cursor.next();
                tasks.add((String) task.get("id"));
            }
        } finally {
            cursor.close();
        }
        return tasks;
    }

    @Override
    public boolean updateTask(final Task task) {
        final DBObject dbTask = collection.findOne(new BasicDBObject("id", task.getId()));
        if (dbTask == null) {
            collection.save(mongoTaskMapper.mapTaskToBson(task));
            return true;
        } else {
            if (shouldReRun(task, dbTask)) {
                dbTask.put("queued", System.currentTimeMillis());
                collection.save(dbTask);
                return true;
            }
            return false;
        }
    }

    @Override
    public List<Task> getNextQueuedTaskBatch(final long now, final int batchSize) {
        final List<Task> tasks = findNextQueuedTaskBatch(now);
        if (!tasks.isEmpty()) {
            final List<String> ids = getTaskIds(tasks);
            markQueuedTasks(ids);
        }
        return tasks;
    }

    @Override
    public void rescheduleTask(final String taskId, final long timestamp) {
        final BasicDBObject query = new BasicDBObject("id", taskId);
        final BasicDBObject update = new BasicDBObject();
        update.put("schedule", timestamp);
        update.put("queued", false);
        collection.update(query, new BasicDBObject("$set", update));
    }

    private boolean shouldReRun(final Task task, final DBObject dbTask) {
        if (task.getInterval() != null && dbTask.containsField("lastRun")) {
            final DateTime lastRun = new DateTime(dbTask.get("lastRun"));
            return lastRun.plus(task.getInterval()).isBeforeNow();
        }
        return false;
    }

    private List<Task> findNextQueuedTaskBatch(final long now) {
        final DBObject query = new BasicDBObject("$and", asList(
                                        new BasicDBObject("schedule", new BasicDBObject("$not", new BasicDBObject("$gt", now))),
                                        new BasicDBObject("queued", new BasicDBObject("$ne", true))
        ));

        final List<Task> tasks = new ArrayList<Task>();
        final DBCursor cursor = collection.find(query).limit(16);
        try {
            while (cursor.hasNext()) {
                tasks.add(mongoTaskMapper.mapBsonToTask((BasicDBObject) cursor.next()));
            }
        } finally {
            cursor.close();
        }
        return tasks;
    }

    private List<String> getTaskIds(final List<Task> tasks) {
        final List<String> ids = new ArrayList<String>(tasks.size());
        for (final Task task : tasks) {
            ids.add(task.getId());
        }
        return ids;
    }

    private void markQueuedTasks(final List<String> ids) {
        final DBObject update = new BasicDBObject("$set", new BasicDBObject("queued", true));
        collection.update(new BasicDBObject("id", new BasicDBObject("$in", ids)), update, false, true);
    }
}
