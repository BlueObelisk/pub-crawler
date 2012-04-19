package wwmm.pubcrawler.repositories.mongo;

import com.mongodb.*;
import org.joda.time.DateTime;
import wwmm.pubcrawler.crawler.CrawlRunner;
import wwmm.pubcrawler.crawler.CrawlTask;
import wwmm.pubcrawler.inject.Tasks;
import wwmm.pubcrawler.repositories.TaskRepository;

import javax.inject.Inject;
import java.util.*;
import java.util.regex.Pattern;

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
    public CrawlTask getTask(final String taskId) {
        final BasicDBObject query = new BasicDBObject("id", taskId);
        final BasicDBObject update = new BasicDBObject("$set", new BasicDBObject("run", true));
        final DBObject task = collection.findAndModify(query, update);

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
    public boolean updateTask(final CrawlTask task) {
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

    private boolean shouldReRun(final CrawlTask task, final DBObject dbTask) {
        if (task.getMaxAge() != null && dbTask.containsField("lastRun")) {
            final DateTime lastRun = new DateTime(dbTask.get("lastRun"));
            return lastRun.plus(task.getMaxAge()).isBeforeNow();
        }
        return false;
    }
}
