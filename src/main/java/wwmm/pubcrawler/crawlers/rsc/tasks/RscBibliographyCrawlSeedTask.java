package wwmm.pubcrawler.crawlers.rsc.tasks;

import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wwmm.pubcrawler.crawler.CrawlTask;
import wwmm.pubcrawler.crawler.CrawlTaskBuilder;
import wwmm.pubcrawler.crawler.TaskQueue;
import wwmm.pubcrawler.crawlers.elsevier.Elsevier;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

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
        final Map<String,String> data = new HashMap<String, String>();
        if (true) throw new UnsupportedOperationException();
//        data.put("url", Elsevier.OPML_URL.toString());
        data.put("fileId", "opml.xml");
        
        final CrawlTask task = new CrawlTaskBuilder()
            .ofType(RscPublicationListCrawlTask.class)
            .withData(data)
            .withMaxAge(Duration.standardDays(1))
            .withId("rsc:journal-list")
            .build();

        taskQueue.queueTask(task);
    }

}
