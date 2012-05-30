package wwmm.pubcrawler.crawlers.nature.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wwmm.pubcrawler.crawler.Task;
import wwmm.pubcrawler.crawler.TaskQueue;
import wwmm.pubcrawler.crawlers.nature.NaturePublicationListCrawlTaskFactory;

import javax.inject.Inject;

/**
 * @author Sam Adams
 */
public class NatureBibliographyCrawlSeedTask implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(NatureBibliographyCrawlSeedTask.class);

    private final TaskQueue taskQueue;
    private final NaturePublicationListCrawlTaskFactory crawlTaskFactory;

    @Inject
    public NatureBibliographyCrawlSeedTask(final TaskQueue taskQueue, final NaturePublicationListCrawlTaskFactory factory) {
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
