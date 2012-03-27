package wwmm.pubcrawler.crawlers.acs;

import org.joda.time.Duration;
import wwmm.pubcrawler.crawlers.IssueTocCrawlTaskFactory;
import wwmm.pubcrawler.crawlers.acs.tasks.AcsIssueTocCrawlTask;
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
public class AcsIssueTocCrawlTaskFactory implements IssueTocCrawlTaskFactory {
    
    @Override
    public CrawlTask createCurrentIssueTocCrawlTask(final String journal, final URI url) {
        return createIssueTocCrawlTask(journal, url, "current");
    }

    @Override
    public CrawlTask createIssueTocCrawlTask(final String journal, final URI url, final String id) {
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
