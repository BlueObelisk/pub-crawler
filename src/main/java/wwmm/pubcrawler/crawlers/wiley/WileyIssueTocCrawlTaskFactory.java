package wwmm.pubcrawler.crawlers.wiley;

import org.joda.time.Duration;
import wwmm.pubcrawler.crawlers.IssueTocCrawlTaskFactory;
import wwmm.pubcrawler.crawlers.wiley.tasks.WileyIssueTocCrawlTask;
import wwmm.pubcrawler.v2.crawler.CrawlTask;
import wwmm.pubcrawler.v2.crawler.CrawlTaskBuilder;

import javax.inject.Singleton;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sam Adams
 */
@Singleton
public class WileyIssueTocCrawlTaskFactory implements IssueTocCrawlTaskFactory {

    @Override
    public CrawlTask createCurrentIssueTocCrawlTask(final String journal, final URI url) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("url", URI.create("http://onlinelibrary.wiley.com/journal/" + journal + "/currentissue").toString());
        map.put("fileId", "toc.html");
        map.put("journal", journal);

        return new CrawlTaskBuilder()
            .ofType(WileyIssueTocCrawlTask.class)
            .withMaxAge(Duration.standardDays(1))
            .withId("wiley:issue-toc:" + journal + "/current")
            .withData(map)
            .build();
    }

    @Override
    public CrawlTask createIssueTocCrawlTask(final String journal, final URI url, final String id) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("url", url.toString());
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
