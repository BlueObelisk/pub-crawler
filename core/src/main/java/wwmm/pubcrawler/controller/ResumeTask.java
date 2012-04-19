package wwmm.pubcrawler.controller;

import wwmm.pubcrawler.crawler.TaskQueue;

import javax.inject.Inject;

/**
 * @author Sam Adams
 */
public class ResumeTask implements Runnable {

    private final TaskQueue taskQueue;

    @Inject
    public ResumeTask(final TaskQueue taskQueue) {
        this.taskQueue = taskQueue;
    }

    @Override
    public void run() {
        final String filter = System.getProperty("resume");
        System.err.println("Resuming tasks (filter : " + filter + ")");

        taskQueue.resumeTasks(filter);
    }
    
}
