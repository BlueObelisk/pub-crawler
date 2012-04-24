package wwmm.pubcrawler.crawlers.springer;

import org.joda.time.Duration;
import wwmm.pubcrawler.crawlers.springer.tasks.SpringerIssueListCrawlTask;
import wwmm.pubcrawler.crawler.CrawlTask;
import wwmm.pubcrawler.crawler.CrawlTaskBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sam Adams
 */
public class SpringerIssueListCrawlTaskFactory {

    public CrawlTask createIssueListCrawlTask(final String journal, final URI url) {
        final Map<String, String> map = new HashMap<String, String>();
        map.put("url", URI.create("http://www.springerlink.com/content/"+journal+"/").toString());
        map.put("fileId", "issues.html");
        map.put("journal", journal);

        return new CrawlTaskBuilder()
            .ofType(SpringerIssueListCrawlTask.class)
            .withInterval(Duration.standardDays(1))
            .withId("springer:issue-list:" + journal + "/current")
            .withData(map)
            .build();
    }

}
