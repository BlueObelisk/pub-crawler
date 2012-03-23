package wwmm.pubcrawler.crawlers.elsevier.tasks;

import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wwmm.pubcrawler.crawlers.elsevier.Elsevier;
import wwmm.pubcrawler.v2.crawler.CrawlTask;
import wwmm.pubcrawler.v2.crawler.CrawlTaskBuilder;
import wwmm.pubcrawler.v2.crawler.TaskQueue;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sam Adams
 */
public class ElsevierBibliographyCrawlSeedTask implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(ElsevierBibliographyCrawlSeedTask.class);

    private final TaskQueue taskQueue;

    @Inject
    public ElsevierBibliographyCrawlSeedTask(final TaskQueue taskQueue) {
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
        Map<String,String> data = new HashMap<String, String>();
        data.put("url", Elsevier.OPML_URL.toString());
        data.put("fileId", "opml.xml");
        
        final CrawlTask task = new CrawlTaskBuilder()
            .ofType(ElsevierPublicationListCrawlTask.class)
            .withData(data)
            .withMaxAge(Duration.standardDays(1))
            .withId("elsevier:journal-list")
            .build();

        taskQueue.queueTask(task);
    }

}
