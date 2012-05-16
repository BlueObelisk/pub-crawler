package wwmm.pubcrawler.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wwmm.pubcrawler.crawler.CrawlTask;
import wwmm.pubcrawler.repositories.TaskRepository;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.LockSupport;

/**
 * @author Sam Adams
 */
public class TaskFeeder {
    
    private static final Logger LOG = LoggerFactory.getLogger(TaskFeeder.class);
    
    private final TaskRepository taskRepository;
    private final TaskReceiver receiver;
    private final long pollIntervalMillis;
    private final int batchSize;
    
    private final AtomicBoolean running = new AtomicBoolean(false);
    private volatile boolean stop = false;
    
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final Runner runner = new Runner();
    
    public TaskFeeder(final TaskRepository taskRepository, final TaskReceiver receiver, final long pollIntervalMillis, final int batchSize) {
        this.taskRepository = taskRepository;
        this.receiver = receiver;
        this.pollIntervalMillis = pollIntervalMillis;
        this.batchSize = batchSize;
    }
    
    public void start() {
        if (!running.compareAndSet(false, true)) {
            throw new IllegalStateException("Already running");
        }
        executor.execute(runner);
    }

    public void stop() {
        runner.stop();
    }

    public boolean isStopped() {
        return !running.get();
    }
    
    private class Runner implements Runnable {

        private volatile Thread thread;
        private volatile boolean stop;
        
        public void run() {
            thread = Thread.currentThread();

            while (!stop) {
                final long now = System.currentTimeMillis();
                final List<CrawlTask> tasks = taskRepository.getNextQueuedTaskBatch(System.currentTimeMillis(), batchSize);
                LOG.trace("Found {} tasks", tasks.size());
                for (final CrawlTask task : tasks) {
                    receiver.task(task);
                }
                sleep(now + pollIntervalMillis);
            }

            if (!running.compareAndSet(true, false)) {
                throw new IllegalStateException("Already stopped!");
            }
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
    }
    
}
