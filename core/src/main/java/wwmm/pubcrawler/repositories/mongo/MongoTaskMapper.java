package wwmm.pubcrawler.repositories.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.joda.time.Duration;
import wwmm.pubcrawler.crawler.CrawlRunner;
import wwmm.pubcrawler.crawler.CrawlTask;
import wwmm.pubcrawler.crawler.CrawlTaskBuilder;

import java.util.LinkedHashMap;
import java.util.Map;

public class MongoTaskMapper {

    public DBObject mapTaskToBson(final CrawlTask task) {
        final DBObject dbTask = new BasicDBObject();
        dbTask.put("id", task.getId());
        dbTask.put("type", task.getTaskClass().getName());
        dbTask.put("queued", System.currentTimeMillis());
        dbTask.put("schedule", -1L);
        dbTask.put("interval", task.getInterval().getMillis());
        dbTask.put("data", mapData(task));
        return dbTask;
    }

    private DBObject mapData(final CrawlTask task) {
        final DBObject data = new BasicDBObject();
        for (final String key : task.getData().keys()) {
            data.put(key, task.getData().getString(key));
        }
        return data;
    }

    public CrawlTask mapBsonToTask(final DBObject task) {
        final String id = (String) task.get("id");
        final Class<? extends CrawlRunner> type = getType(task);
        final Map<String, String> data = getData((DBObject) task.get("data"));
        final CrawlTaskBuilder builder = new CrawlTaskBuilder()
            .withId(id)
            .ofType(type)
            .withData(data);
        if (task.containsField("interval")) {
            builder.withInterval(new Duration(task.get("interval")));
        }
        return builder.build();
    }

    private Map<String, String> getData(final DBObject task) {
        final Map<String,String> data = new LinkedHashMap<String, String>();
        for (final String key : task.keySet()) {
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
}