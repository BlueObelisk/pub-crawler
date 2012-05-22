package wwmm.pubcrawler.crawlers.wiley.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wwmm.pubcrawler.crawler.Task;
import wwmm.pubcrawler.crawler.TaskQueue;
import wwmm.pubcrawler.crawlers.wiley.WileyPublicationListCrawlTaskFactory;
import wwmm.pubcrawler.tasks.HttpCrawlTaskData;

import javax.inject.Inject;

/**
 * @author Sam Adams
 */
public class WileyBibliographyCrawlSeedTask implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(WileyBibliographyCrawlSeedTask.class);

    private final TaskQueue taskQueue;
    private final WileyPublicationListCrawlTaskFactory crawlTaskFactory;

    @Inject
    public WileyBibliographyCrawlSeedTask(final TaskQueue taskQueue, final WileyPublicationListCrawlTaskFactory wileyPublicationListCrawlTaskFactory) {
        this.taskQueue = taskQueue;
        this.crawlTaskFactory = wileyPublicationListCrawlTaskFactory;
    }

    @Override
    public void run() {
        try {
            enqueueSeedTask();
        } catch (Exception e) {
            LOG.error("Error seeding crawl", e);
        }
    }

    private void enqueueSeedTask() {
        final Task<HttpCrawlTaskData> task = crawlTaskFactory.createCrawlTask();
        taskQueue.queueTask(task);
    }
}
