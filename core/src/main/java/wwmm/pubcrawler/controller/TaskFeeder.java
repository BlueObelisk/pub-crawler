package wwmm.pubcrawler.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wwmm.pubcrawler.crawler.Task;
import wwmm.pubcrawler.tasks.repository.TaskRepository;

import java.util.List;
import java.util.concurrent.locks.LockSupport;

/**
 * @author Sam Adams
 */
public class TaskFeeder implements Runnable {

    private static Logger LOG = LoggerFactory.getLogger(TaskFeeder.class);

    private volatile Thread thread;
    private volatile boolean stop;
    private volatile boolean stopped;

    private final TaskRepository taskRepository;
    private final TaskReceiver receiver;
    private final long pollIntervalMillis;
    private final int batchSize;

    public TaskFeeder(final TaskRepository taskRepository, final TaskReceiver receiver, final long pollIntervalMillis, final int batchSize) {
        this.taskRepository = taskRepository;
        this.receiver = receiver;
        this.pollIntervalMillis = pollIntervalMillis;
        this.batchSize = batchSize;
    }

    @Override
    public void run() {
        thread = Thread.currentThread();

        while (!stop) {
            final long now = System.currentTimeMillis();
            final List<Task> tasks = taskRepository.getNextQueuedTaskBatch(now, batchSize);
            LOG.trace("Found {} tasks", tasks.size());
            for (final Task task : tasks) {
                receiver.task(task);
            }
            sleep(now + pollIntervalMillis);
        }

        stopped = true;
    }

    private void sleep(final long targetTime) {
        while (!Thread.currentThread().isInterrupted() && System.currentTimeMillis() < targetTime) {
            LockSupport.parkUntil(this, targetTime);
        }
        if (Thread.currentThread().isInterrupted()) {
            Thread.interrupted();
        }
    }

    public void stop() {
        this.stop = true;
        LockSupport.unpark(this.thread);
    }

    public boolean isStopped() {
        return stopped;
    }
}
