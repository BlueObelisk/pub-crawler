package wwmm.pubcrawler.tasks.repository;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.joda.time.Duration;
import wwmm.pubcrawler.crawler.Task;
import wwmm.pubcrawler.crawler.TaskBuilder;
import wwmm.pubcrawler.tasks.Marshaller;
import wwmm.pubcrawler.tasks.TaskSpecification;

import javax.inject.Inject;

public class MongoTaskMapper {

    private static final String ID = "id";
    private static final String TYPE = "type";
    private static final String QUEUED = "queued";
    private static final String SCHEDULE = "schedule";
    private static final String INTERVAL = "interval";
    private static final String DATA = "data";
    private final TaskSpecificationFactory taskSpecificationFactory;

    @Inject
    public MongoTaskMapper(final TaskSpecificationFactory taskSpecificationFactory) {
        this.taskSpecificationFactory = taskSpecificationFactory;
    }

    public <T> DBObject mapTaskToBson(final Task<T> task) {
        final DBObject dbTask = new BasicDBObject();
        dbTask.put(ID, task.getId());
        dbTask.put(TYPE, task.getTaskSpecification().getClass().getName());
        dbTask.put(QUEUED, System.currentTimeMillis());
        dbTask.put(SCHEDULE, -1L);
        dbTask.put(INTERVAL, task.getInterval().toString());
        dbTask.put(DATA, mapDataToBson(task));
        return dbTask;
    }

    public <T> Task<T> mapBsonToTask(final BasicDBObject task) {
        final String id = task.getString(ID);
        final Duration interval = task.containsField(INTERVAL) ? new Duration(task.getString(INTERVAL)) : null;
        final TaskSpecification<T> taskSpecification = taskSpecificationFactory.getTaskSpecification(task.getString(TYPE));
        return TaskBuilder.newTask(taskSpecification)
                .withId(id)
                .withInterval(interval)
                .withData(mapDataFromBson(taskSpecification, task))
                .build();
    }

    private <T> DBObject mapDataToBson(final Task<T> task) {
        final BasicDBObject data = new BasicDBObject();
        final Marshaller<T> marshaller = task.getTaskSpecification().getDataMarshaller();
        marshaller.marshall(task.getData(), new BasicDbObjectDataSink(data));
        return data;
    }

    private <T> T mapDataFromBson(final TaskSpecification<T> taskSpecification, final BasicDBObject task) {
        final Marshaller<T> marshaller = taskSpecification.getDataMarshaller();
        final BasicDBObject data = (BasicDBObject) task.get(DATA);
        return marshaller.unmarshall(new BasicDbObjectDataSource(data));
    }
}