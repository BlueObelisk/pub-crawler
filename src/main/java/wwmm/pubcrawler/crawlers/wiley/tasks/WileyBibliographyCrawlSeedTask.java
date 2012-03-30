package wwmm.pubcrawler.crawlers.wiley.tasks;

import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wwmm.pubcrawler.crawlers.wiley.Wiley;
import wwmm.pubcrawler.v2.crawler.CrawlTask;
import wwmm.pubcrawler.v2.crawler.CrawlTaskBuilder;
import wwmm.pubcrawler.v2.crawler.TaskQueue;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sam Adams
 */
public class WileyBibliographyCrawlSeedTask implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(WileyBibliographyCrawlSeedTask.class);

    private final TaskQueue taskQueue;

    @Inject
    public WileyBibliographyCrawlSeedTask(final TaskQueue taskQueue) {
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
        final Map<String,String> data = new HashMap<String, String>();
        data.put("url", Wiley.PUBLICATION_LIST_URL.toString());
        data.put("fileId", "journals.html");
        
        final CrawlTask task = new CrawlTaskBuilder()
            .ofType(WileyPublicationListCrawlTask.class)
            .withData(data)
            .withMaxAge(Duration.standardDays(1))
            .withId("wiley:journal-list")
            .build();

        taskQueue.queueTask(task);
    }

}
