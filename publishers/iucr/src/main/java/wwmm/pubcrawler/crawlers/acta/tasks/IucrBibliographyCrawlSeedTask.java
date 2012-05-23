package wwmm.pubcrawler.crawlers.acta.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wwmm.pubcrawler.crawler.Task;
import wwmm.pubcrawler.crawler.TaskQueue;
import wwmm.pubcrawler.crawlers.acta.IucrPublicationListCrawlTaskFactory;

import javax.inject.Inject;

/**
 * @author Sam Adams
 */
public class IucrBibliographyCrawlSeedTask implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(IucrBibliographyCrawlSeedTask.class);

    private final TaskQueue taskQueue;
    private final IucrPublicationListCrawlTaskFactory iucrPublicationListCrawlTaskFactory;

    @Inject
    public IucrBibliographyCrawlSeedTask(final TaskQueue taskQueue, final IucrPublicationListCrawlTaskFactory iucrPublicationListCrawlTaskFactory) {
        this.taskQueue = taskQueue;
        this.iucrPublicationListCrawlTaskFactory = iucrPublicationListCrawlTaskFactory;
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
        final Task task = iucrPublicationListCrawlTaskFactory.createCrawlTask();
        taskQueue.queueTask(task);
    }
}
