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
import wwmm.pubcrawler.repositories.TaskRepository;
import wwmm.pubcrawler.repositories.mongo.MongoTaskMapper;
import wwmm.pubcrawler.repositories.mongo.MongoTaskRepository;
import wwmm.pubcrawler.repositories.mongo.TaskSpecificationFactory;
import wwmm.pubcrawler.tasks.Marshaller;
import wwmm.pubcrawler.tasks.TaskRunner;
import wwmm.pubcrawler.tasks.TaskSpecification;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.LockSupport;

/**
 * @author Sam Adams
 */
public class TaskRunnerIT {

    private static final long TIMEOUT_MILLIS = 10000;

    private Mongo mongo;

    private DB db;
    private TaskRepository taskRepository;
    private Receiver receiver;
    private TaskFeeder taskRunner;

    @Before
    public void setUp() throws Exception {
        mongo = new Mongo();
        db = mongo.getDB("test");  // + Math.abs(new Random().nextLong()));

        taskRepository = new MongoTaskRepository(db.getCollection("tasks"), new MongoTaskMapper(new Factory()));
        receiver = new Receiver();

        taskRunner = new TaskFeeder(taskRepository, receiver, 1000L, 4);
        taskRunner.start();
    }

    @After
    public void tearDown() throws Exception {
        taskRunner.stop();
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

        long timeout = System.currentTimeMillis() + TIMEOUT_MILLIS;
        while (receiver.tasks.size() != 10) {
            if (System.currentTimeMillis() > timeout) {
                throw new TimeoutException();
            }
            LockSupport.parkNanos(10000000l);
        }
    }

    @Test
    public void testReRunsTasks() throws Exception {

        Task<String> task = TaskBuilder.newTask(new Specification())
                          .withId("task")
                          .withInterval(Duration.standardSeconds(5))
                          .build();
        taskRepository.updateTask(task);

        long timeout = System.currentTimeMillis() + TIMEOUT_MILLIS;
        while (receiver.tasks.isEmpty()) {
            if (System.currentTimeMillis() > timeout) {
                throw new TimeoutException();
            }
            LockSupport.parkNanos(10000000l);
        }

        receiver.tasks.clear();

        timeout = System.currentTimeMillis() + TIMEOUT_MILLIS*2;
        while (receiver.tasks.isEmpty()) {
            if (System.currentTimeMillis() > timeout) {
                throw new TimeoutException();
            }
            LockSupport.parkNanos(10000000l);
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
        public TaskSpecification<String> getTaskSpecification(final String typeName) {
            return new Specification();
        }
    }

    static class Specification implements TaskSpecification<String> {

        @Override
        public Class<TaskRunner<String>> getRunnerClass() {
            return null;
        }

        @Override
        public Marshaller<String> getDataMarshaller() {
            return null;
        }
    }
}
