package wwmm.pubcrawler.crawlers.rsc.tasks;

import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wwmm.pubcrawler.Config;
import wwmm.pubcrawler.crawler.*;
import wwmm.pubcrawler.crawlers.rsc.Rsc;
import wwmm.pubcrawler.tasks.HttpCrawlTaskData;
import wwmm.pubcrawler.tasks.PublicationListCrawlTaskData;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

import static wwmm.pubcrawler.Config.PUBLICATION_LIST_CACHE_MAX_AGE;
import static wwmm.pubcrawler.Config.PUBLICATION_LIST_CRAWL_INTERVAL;

/**
 * @author Sam Adams
 */
public class RscBibliographyCrawlSeedTask implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(RscBibliographyCrawlSeedTask.class);

    private final TaskQueue taskQueue;

    @Inject
    public RscBibliographyCrawlSeedTask(final TaskQueue taskQueue) {
        this.taskQueue = taskQueue;
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
        final HttpCrawlTaskData data = new HttpCrawlTaskData(Rsc.JOURNAL_LIST, "journals.html", PUBLICATION_LIST_CACHE_MAX_AGE, null);
        final Task<HttpCrawlTaskData> task = TaskBuilder.newTask(RscPublicationListCrawlTask.INSTANCE)
                .withData(data)
                .withInterval(PUBLICATION_LIST_CRAWL_INTERVAL)
                .withId("rsc:journal-list")
                .build();

        taskQueue.queueTask(task);
    }

}
