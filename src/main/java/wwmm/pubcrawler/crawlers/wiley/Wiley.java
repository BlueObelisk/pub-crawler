package wwmm.pubcrawler.crawlers.wiley;

import org.joda.time.Duration;
import wwmm.pubcrawler.crawlers.wiley.tasks.WileyIssueTocCrawlTask;
import wwmm.pubcrawler.model.id.PublisherId;
import wwmm.pubcrawler.v2.crawler.CrawlTask;
import wwmm.pubcrawler.v2.crawler.CrawlTaskBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sam Adams
 */
public class Wiley {

    public static final PublisherId PUBLISHER_ID = new PublisherId("wiley");

    public static final URI PUBLICATION_LIST_URL = URI.create("http://onlinelibrary.wiley.com/browse/publications?type=journal&&start=1&resultsPerPage=10000");

    public static CrawlTask createIssueTocTask(String journal, String id) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("url", URI.create("http://onlinelibrary.wiley.com/journal/" + journal + "/currentissue").toString());
        map.put("fileId", "toc.html");
        map.put("journal", journal);

        return new CrawlTaskBuilder()
            .ofType(WileyIssueTocCrawlTask.class)
            .withMaxAge(Duration.standardDays(1))
            .withId("wiley:issue-toc:" + journal + "/" + id)
            .withData(map)
            .build();
    }
}
