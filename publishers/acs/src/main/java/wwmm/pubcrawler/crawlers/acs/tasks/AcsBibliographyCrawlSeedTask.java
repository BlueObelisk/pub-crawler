package wwmm.pubcrawler.crawlers.acs.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wwmm.pubcrawler.crawler.Task;
import wwmm.pubcrawler.crawler.TaskQueue;
import wwmm.pubcrawler.crawlers.acs.AcsPublicationListCrawlTaskFactory;

import javax.inject.Inject;

/**
 * @author Sam Adams
 */
public class AcsBibliographyCrawlSeedTask implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(AcsBibliographyCrawlSeedTask.class);

    private final TaskQueue taskQueue;
    private final AcsPublicationListCrawlTaskFactory crawlTaskFactory;

    @Inject
    public AcsBibliographyCrawlSeedTask(final TaskQueue taskQueue, final AcsPublicationListCrawlTaskFactory factory) {
        this.taskQueue = taskQueue;
        this.crawlTaskFactory = factory;
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
        final Task task = crawlTaskFactory.createCrawlTask();
        taskQueue.queueTask(task);
    }
}
