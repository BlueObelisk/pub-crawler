package wwmm.pubcrawler.crawlers.acs;

import org.joda.time.Duration;
import wwmm.pubcrawler.crawlers.acs.tasks.AcsIssueTocCrawlTask;
import wwmm.pubcrawler.model.id.PublisherId;
import wwmm.pubcrawler.v2.crawler.CrawlTask;
import wwmm.pubcrawler.v2.crawler.CrawlTaskBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sam Adams
 */
public class Acs {
    
    public static final PublisherId PUBLISHER_ID = new PublisherId("acs");

    public static final URI JOURNAL_LIST_URL = URI.create("http://pubs.acs.org/");

    public static CrawlTask createIssueTocTask(final URI url, String journal, String id) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("url", url.toString());
        map.put("fileId", "toc.html");
        map.put("journal", journal);

        return new CrawlTaskBuilder()
            .ofType(AcsIssueTocCrawlTask.class)
            .withMaxAge(Duration.standardDays(1))
            .withId("acs:issue-toc:" + journal + "/" + id)
            .withData(map)
            .build();
    }
}
