package wwmm.pubcrawler.integration;

import com.mongodb.DB;
import com.mongodb.Mongo;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import wwmm.pubcrawler.controller.TaskFeeder;
import wwmm.pubcrawler.controller.TaskReceiver;
import wwmm.pubcrawler.crawler.Task;
import wwmm.pubcrawler.crawler.TaskBuilder;
import wwmm.pubcrawler.tasks.repository.TaskRepository;
import wwmm.pubcrawler.tasks.repository.MongoTaskMapper;
import wwmm.pubcrawler.tasks.repository.MongoTaskRepository;
import wwmm.pubcrawler.tasks.repository.TaskSpecificationFactory;
import wwmm.pubcrawler.tasks.*;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.LockSupport;

/**
 * @author Sam Adams
 */
public class TaskRunnerIT {

    private static final long TIMEOUT_MILLIS = 10000L;
    private static final long MILLIS_10 = 10000000L;

    private Mongo mongo;

    private DB db;
    private TaskRepository taskRepository;
    private Receiver receiver;
    private TaskFeeder taskFeeder;

    private Executor executor = Executors.newSingleThreadExecutor();

    @Before
    public void setUp() throws Exception {
        mongo = new Mongo();
        db = mongo.getDB("test");

        taskRepository = new MongoTaskRepository(db.getCollection("tasks"), new MongoTaskMapper(new Factory()));
        receiver = new Receiver();

        taskFeeder = new TaskFeeder(taskRepository, receiver, 1000L, 4);
        executor.execute(taskFeeder);
    }

    @After
    public void tearDown() throws Exception {
        taskFeeder.stop();
        db.dropDatabase();
        mongo.close();
    }

    @Test
    public void testRunsTasks() throws Exception {
        for (int i = 0; i < 10; i++) {
            Task task = TaskBuilder.newTask(new Specification())
                              .withId("task-" + i)
                              .build();
            taskRepository.updateTask(task);
        }

        waitForTasksToRun(10, TIMEOUT_MILLIS);
    }

    @Test
    public void testReRunsTasks() throws Exception {

        Task<Void> task = TaskBuilder.newTask(new Specification())
                          .withId("task")
                          .withInterval(Duration.standardSeconds(5))
                          .build();
        taskRepository.updateTask(task);

        waitForTasksToRun(1, TIMEOUT_MILLIS);

        receiver.tasks.clear();

        waitForTasksToRun(1, TIMEOUT_MILLIS * 2);
    }

    private void waitForTasksToRun(final long taskCount, final long maxWait) throws TimeoutException {
        long timeout = System.currentTimeMillis() + maxWait;
        while (receiver.tasks.size() < taskCount) {
            if (System.currentTimeMillis() > timeout) {
                throw new TimeoutException();
            }
            LockSupport.parkNanos(MILLIS_10);
        }
    }


    private class Receiver implements TaskReceiver {

        private final Set<String> tasks = new HashSet<String>();

        @Override
        public void task(final Task task) {
            tasks.add(task.getId());

            final DateTime nextRun = new DateTime().plus(task.getInterval());
            taskRepository.rescheduleTask(task.getId(), nextRun.getMillis());

        }

    }

    static class Factory implements TaskSpecificationFactory {

        @Override
        @SuppressWarnings("unchecked")
        public TaskSpecification<Void> getTaskSpecification(final String typeName) {
            return new Specification();
        }
    }

    static class Specification implements TaskSpecification<Void> {

        @Override
        public Class<TaskRunner<Void>> getRunnerClass() {
            return null;
        }

        @Override
        public Marshaller<Void> getDataMarshaller() {
            return new Marshaller<Void>() {
                @Override
                public void marshall(final Void data, final DataSink target) {
                }

                @Override
                public Void unmarshall(final DataSource source) {
                    return null;
                }
            };
        }
    }
}
