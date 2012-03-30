package wwmm.pubcrawler.crawlers.acs.tasks;

import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wwmm.pubcrawler.crawlers.acs.Acs;
import wwmm.pubcrawler.v2.crawler.CrawlTask;
import wwmm.pubcrawler.v2.crawler.CrawlTaskBuilder;
import wwmm.pubcrawler.v2.crawler.TaskQueue;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sam Adams
 */
public class AcsBibliographyCrawlSeedTask implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(AcsBibliographyCrawlSeedTask.class);

    private final TaskQueue taskQueue;

    @Inject
    public AcsBibliographyCrawlSeedTask(final TaskQueue taskQueue) {
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
        data.put("url", Acs.JOURNAL_LIST_URL.toString());
        data.put("fileId", "index.html");
        
        final CrawlTask task = new CrawlTaskBuilder()
            .ofType(AcsPublicationListCrawlTask.class)
            .withData(data)
            .withMaxAge(Duration.standardDays(1))
            .withId("acs:journal-list")
            .build();

        taskQueue.queueTask(task);
    }

}
