package wwmm.pubcrawler.v2.repositories.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import org.joda.time.DateTime;
import wwmm.pubcrawler.v2.crawler.CrawlRunner;
import wwmm.pubcrawler.v2.crawler.CrawlTask;
import wwmm.pubcrawler.v2.crawler.CrawlTaskBuilder;
import wwmm.pubcrawler.v2.inject.Tasks;
import wwmm.pubcrawler.v2.repositories.TaskRepository;

import javax.inject.Inject;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Sam Adams
 */
public class MongoTaskRepository implements TaskRepository {

    private final DBCollection collection;

    @Inject
    public MongoTaskRepository(@Tasks final DBCollection collection) {
        this.collection = collection;
        this.collection.ensureIndex(new BasicDBObject("id", 1), "id_index", true);
    }

    @Override
    public CrawlTask getTask(final String taskId) {
        final BasicDBObject query = new BasicDBObject("id", taskId);
        final BasicDBObject update = new BasicDBObject("$set", new BasicDBObject("run", true));
        final DBObject task = collection.findAndModify(query, update);

        final String id = (String) task.get("id");
        final Class<? extends CrawlRunner> type = getType(task);
        final Map<String,String> data = getData((DBObject) task.get("data"));
        return new CrawlTaskBuilder()
            .withId(id)
            .withData(data)
            .ofType(type)
            .build();
    }

    private Map<String, String> getData(final DBObject task) {
        final Map<String,String> data = new LinkedHashMap<String, String>();
        for (String key : task.keySet()) {
            data.put(key, (String) task.get(key));
        }
        return data;
    }

    private Class<? extends CrawlRunner> getType(final DBObject task) {
        final String type = (String) task.get("type");
        try {
            return (Class<? extends CrawlRunner>) Class.forName(type);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Unknown type: " + type, e);
        }
    }

    @Override
    public boolean updateTask(final CrawlTask task) {
        final DBObject dbTask = collection.findOne(new BasicDBObject("id", task.getId()));
        if (dbTask == null) {
            collection.save(createDbObject(task));
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
            DateTime lastRun = new DateTime(dbTask.get("lastRun"));
            return lastRun.plus(task.getMaxAge()).isBeforeNow();
        }
        return false;
    }

    private DBObject createDbObject(final CrawlTask task) {
        final DBObject dbTask = new BasicDBObject();
        dbTask.put("id", task.getId());
        dbTask.put("type", task.getTaskClass().getName());
        dbTask.put("queued", System.currentTimeMillis());
        final DBObject data = new BasicDBObject();
        for (String key : task.getData().keys()) {
            data.put(key, task.getData().getString(key));
        }
        dbTask.put("data", data);
        return dbTask;
    }

}
