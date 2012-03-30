package wwmm.pubcrawler.crawlers.elsevier;

import org.joda.time.Duration;
import wwmm.pubcrawler.crawlers.elsevier.tasks.ElsevierIssueTocCrawlTask;
import wwmm.pubcrawler.model.id.PublisherId;
import wwmm.pubcrawler.v2.crawler.CrawlTask;
import wwmm.pubcrawler.v2.crawler.CrawlTaskBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sam Adams
 */
public class Elsevier {
    
    public static final URI OPML_URL = URI.create("http://feeds.sciencedirect.com/opml.xml");

    public static final PublisherId PUBLISHER_ID = new PublisherId("elsevier");

    public static CrawlTask createIssueTocTask(final URI url, final String journal, final String id) {
        final Map<String, String> map = new HashMap<String, String>();
        map.put("url", url.toString());
        map.put("fileId", "toc.html");
        map.put("journal", journal);

        return new CrawlTaskBuilder()
            .ofType(ElsevierIssueTocCrawlTask.class)
            .withMaxAge(Duration.standardDays(1))
            .withId("elsevier:issue-toc:" + journal + "/" + id)
            .withData(map)
            .build();
    }
}
