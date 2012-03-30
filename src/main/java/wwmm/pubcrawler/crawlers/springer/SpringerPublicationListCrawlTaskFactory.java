package wwmm.pubcrawler.crawlers.springer;

import org.joda.time.Duration;
import wwmm.pubcrawler.crawlers.wiley.tasks.WileyPublicationListCrawlTask;
import wwmm.pubcrawler.v2.crawler.CrawlTask;
import wwmm.pubcrawler.v2.crawler.CrawlTaskBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sam Adams
 */
public class SpringerPublicationListCrawlTaskFactory {

    public CrawlTask createIssueListCrawlTask(final URI url, final String key, final int page) {
        // Fetch pages
        Map<String,String> data = new HashMap<String, String>();
        data.put("url", url.toString());
        data.put("fileId", "journals_" + key + "_" + page + ".html");
        data.put("key", key);
        data.put("page", Integer.toString(page));

        return new CrawlTaskBuilder()
            .ofType(WileyPublicationListCrawlTask.class)
            .withData(data)
            .withMaxAge(Duration.standardDays(1))
            .withId("springer:journal-list/" + key + "/" + page)
            .build();
    }

}
