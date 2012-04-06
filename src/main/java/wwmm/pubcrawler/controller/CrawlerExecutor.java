package wwmm.pubcrawler.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wwmm.pubcrawler.crawler.CrawlRunner;
import wwmm.pubcrawler.crawler.CrawlTask;
import wwmm.pubcrawler.crawler.TaskQueue;

import javax.inject.Inject;

/**
 * @author Sam Adams
 */
public class CrawlerExecutor implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(CrawlerExecutor.class);

    private final TaskQueue taskQueue;
    private final CrawlerFactory crawlerFactory;

    @Inject
    public CrawlerExecutor(final TaskQueue taskQueue, final CrawlerFactory crawlerFactory) {
        this.crawlerFactory = crawlerFactory;
        this.taskQueue = taskQueue;
    }

    @Override
    public void run() {
        LOG.info("Starting " + getClass().getSimpleName());

        CrawlTask task = taskQueue.nextTask();
        while (task != null) {
            LOG.info("Running task " + task.getId());
            runTask(task);
            task = taskQueue.nextTask();
        }
    }

    private void runTask(final CrawlTask task) {
        final CrawlRunner crawler;
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
