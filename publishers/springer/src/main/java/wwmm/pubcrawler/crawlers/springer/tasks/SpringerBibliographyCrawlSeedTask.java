package wwmm.pubcrawler.crawlers.springer.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wwmm.pubcrawler.Config;
import wwmm.pubcrawler.crawler.Task;
import wwmm.pubcrawler.crawler.TaskBuilder;
import wwmm.pubcrawler.crawler.TaskQueue;
import wwmm.pubcrawler.crawlers.springer.SpringerPublicationListCrawlTaskData;

import javax.inject.Inject;
import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static wwmm.pubcrawler.Config.PUBLICATION_LIST_CACHE_MAX_AGE;

/**
 * @author Sam Adams
 */
public class SpringerBibliographyCrawlSeedTask implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(SpringerBibliographyCrawlSeedTask.class);

    private static final String URL_BASE = "http://www.springerlink.com/journals/all/%s/";
    private static final List<String> KEYS = Collections.unmodifiableList(Arrays.asList(
                                                                                        "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m",
                                                                                        "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0-9"
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

        final SpringerPublicationListCrawlTaskData data = new SpringerPublicationListCrawlTaskData(url, PUBLICATION_LIST_CACHE_MAX_AGE, key, 1);

        final Task task = TaskBuilder.newTask(SpringerPublicationListCrawlTask.INSTANCE)
                           .withId("springer:journal-list/" + key + "/1")
                           .withData(data)
                           .withInterval(Config.PUBLICATION_LIST_CRAWL_INTERVAL)
                           .build();

        taskQueue.queueTask(task);
    }

}
