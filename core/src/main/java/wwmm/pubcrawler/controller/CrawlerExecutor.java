package wwmm.pubcrawler.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wwmm.pubcrawler.crawler.Task;
import wwmm.pubcrawler.crawler.TaskQueue;
import wwmm.pubcrawler.tasks.TaskRunner;

import javax.inject.Inject;

/**
 * @author Sam Adams
 */
public class CrawlerExecutor implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(CrawlerExecutor.class);

    private final TaskQueue taskQueue;
    private final TaskFactory crawlerFactory;

    @Inject
    public CrawlerExecutor(final TaskQueue taskQueue, final TaskFactory crawlerFactory) {
        this.crawlerFactory = crawlerFactory;
        this.taskQueue = taskQueue;
    }

    @Override
    public void run() {
        LOG.info("Starting " + getClass().getSimpleName());

        Task task = taskQueue.nextTask();
        while (task != null) {
            LOG.info("Running task " + task.getId());
            runTask(task);
            task = taskQueue.nextTask();
        }
    }

    @SuppressWarnings("unchecked")
    private void runTask(final Task task) {
        runTaskTypeSafe(task);
    }

    private <T> void runTaskTypeSafe(final Task<T> task) {
        final TaskRunner<T> crawler;
        try {
            crawler = crawlerFactory.createCrawler(task);
        } catch (Exception e) {
            LOG.error("Error initialising crawler " + task.getId(), e);
            return;
        }
        try {
            crawler.run(task.getId(), task.getData());
        } catch (Exception e) {
            LOG.error("Error running crawler " + task.getId(), e);
        }
    }

}
