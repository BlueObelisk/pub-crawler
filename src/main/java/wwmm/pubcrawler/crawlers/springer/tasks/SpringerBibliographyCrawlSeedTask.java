package wwmm.pubcrawler.crawlers.springer.tasks;

import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wwmm.pubcrawler.crawlers.wiley.tasks.WileyPublicationListCrawlTask;
import wwmm.pubcrawler.v2.crawler.CrawlTask;
import wwmm.pubcrawler.v2.crawler.CrawlTaskBuilder;
import wwmm.pubcrawler.v2.crawler.TaskQueue;

import javax.inject.Inject;
import java.net.URI;
import java.util.*;

/**
 * @author Sam Adams
 */
public class SpringerBibliographyCrawlSeedTask implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(SpringerBibliographyCrawlSeedTask.class);

    private static final String URL_BASE = "http://www.springerlink.com/journals/all/%s/";
    private static final List<String> KEYS = Collections.unmodifiableList(Arrays.asList(
        "a", "b"
//        , "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m",
//        "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0-9"
    ));

    private final TaskQueue taskQueue;

    @Inject
    public SpringerBibliographyCrawlSeedTask(final TaskQueue taskQueue) {
        this.taskQueue = taskQueue;
    }

    @Override
    public void run() {
        try {
            for (final String key : KEYS) {
                enqueueSeedTask(key);
            }
        } catch (Exception e) {
            LOG.error("Error seeding crawl", e);
        }
    }

    private void enqueueSeedTask(final String key) {
        final URI url = URI.create(String.format(URL_BASE, key));

        // Fetch pages
        final Map<String,String> data = new HashMap<String, String>();
        data.put("url", url.toString());
        data.put("fileId", "journals_" + key + "_1.html");
        data.put("key", key);
        data.put("page", "1");
        
        final CrawlTask task = new CrawlTaskBuilder()
            .ofType(SpringerPublicationListCrawlTask.class)
            .withData(data)
            .withMaxAge(Duration.standardDays(1))
            .withId("springer:journal-list/" + key + "/1")
            .build();

        taskQueue.queueTask(task);
    }

}
