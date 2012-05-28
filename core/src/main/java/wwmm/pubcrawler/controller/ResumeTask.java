package wwmm.pubcrawler.controller;

import wwmm.pubcrawler.crawler.TaskQueue;
import wwmm.pubcrawler.inject.Publisher;

import javax.inject.Inject;

/**
 * @author Sam Adams
 */
public class ResumeTask implements Runnable {

    private final TaskQueue taskQueue;
    private final String publisher;

    @Inject
    public ResumeTask(final TaskQueue taskQueue, @Publisher final String publisher) {
        this.taskQueue = taskQueue;
        this.publisher = publisher;
    }

    @Override
    public void run() {
        System.err.println("Resuming tasks (filter : " + publisher + ")");
        taskQueue.resumeTasks(publisher);
    }
}
