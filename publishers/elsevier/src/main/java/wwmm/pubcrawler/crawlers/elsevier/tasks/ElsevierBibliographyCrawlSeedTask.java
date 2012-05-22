package wwmm.pubcrawler.crawlers.elsevier.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wwmm.pubcrawler.crawler.CrawlTask;
import wwmm.pubcrawler.crawler.Task;
import wwmm.pubcrawler.crawler.TaskQueue;
import wwmm.pubcrawler.crawlers.elsevier.ElsevierPublicationListCrawlTaskFactory;

import javax.inject.Inject;

/**
 * @author Sam Adams
 */
public class ElsevierBibliographyCrawlSeedTask implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(ElsevierBibliographyCrawlSeedTask.class);

    private final TaskQueue taskQueue;
    private final ElsevierPublicationListCrawlTaskFactory crawlTaskFactory;

    @Inject
    public ElsevierBibliographyCrawlSeedTask(final TaskQueue taskQueue, final ElsevierPublicationListCrawlTaskFactory factory) {
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
