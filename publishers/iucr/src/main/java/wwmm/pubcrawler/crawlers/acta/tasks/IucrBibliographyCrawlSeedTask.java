package wwmm.pubcrawler.crawlers.acta.tasks;

import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wwmm.pubcrawler.crawler.CrawlTask;
import wwmm.pubcrawler.crawler.CrawlTaskBuilder;
import wwmm.pubcrawler.crawler.TaskQueue;
import wwmm.pubcrawler.crawlers.acta.Iucr;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sam Adams
 */
public class IucrBibliographyCrawlSeedTask implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(IucrBibliographyCrawlSeedTask.class);

    private final TaskQueue taskQueue;

    @Inject
    public IucrBibliographyCrawlSeedTask(final TaskQueue taskQueue) {
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
        data.put("url", Iucr.JOURNALS_URL.toString());
        data.put("fileId", "index.html");
        
        final CrawlTask task = new CrawlTaskBuilder()
            .ofType(IucrPublicationListCrawlTask.class)
            .withData(data)
            .withMaxAge(Duration.standardDays(1))
            .withId("iucr:journal-list")
            .build();

        taskQueue.queueTask(task);
    }

}
