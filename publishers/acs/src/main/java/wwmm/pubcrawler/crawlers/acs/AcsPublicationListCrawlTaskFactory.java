package wwmm.pubcrawler.crawlers.acs;

import org.joda.time.Duration;
import wwmm.pubcrawler.crawler.CrawlTask;
import wwmm.pubcrawler.crawler.CrawlTaskBuilder;
import wwmm.pubcrawler.crawlers.acs.tasks.AcsPublicationListCrawlTask;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Sam Adams
 */
public class AcsPublicationListCrawlTaskFactory {
    
    public CrawlTask createCrawlTask() {
        final Map<String,String> data = new HashMap<String, String>();
        data.put("url", Acs.JOURNAL_LIST_URL.toString());
        data.put("fileId", "index.html");

        return new CrawlTaskBuilder()
            .ofType(AcsPublicationListCrawlTask.class)
            .withData(data)
            .withInterval(Duration.standardDays(1))
            .withId("acs:journal-list")
            .build();
    }
    
}
