package wwmm.pubcrawler.controller;

import wwmm.pubcrawler.v2.crawler.TaskQueue;
import wwmm.pubcrawler.v2.repositories.TaskRepository;

import javax.inject.Inject;

/**
 * @author Sam Adams
 */
public class ResumeTask implements Runnable {

    private TaskQueue taskQueue;

    @Inject
    public ResumeTask(final TaskRepository taskRepository) {
        this.taskQueue = taskQueue;
    }

    @Override
    public void run() {
        final String filter = System.getProperty("resume");
        System.err.println("Resuming tasks (filter : " + filter + ")");

        taskQueue.resumeTasks(filter);
    }
    
}
