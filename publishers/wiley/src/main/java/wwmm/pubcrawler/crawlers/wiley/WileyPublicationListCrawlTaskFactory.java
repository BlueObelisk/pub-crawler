package wwmm.pubcrawler.crawlers.wiley;

import org.joda.time.Duration;
import wwmm.pubcrawler.crawler.CrawlTask;
import wwmm.pubcrawler.crawler.CrawlTaskBuilder;
import wwmm.pubcrawler.crawlers.wiley.tasks.WileyPublicationListCrawlTask;

import java.util.HashMap;
import java.util.Map;

public class WileyPublicationListCrawlTaskFactory {

    public CrawlTask createCrawlTask() {
        final Map<String, String> data = new HashMap<String, String>();
        data.put("url", Wiley.PUBLICATION_LIST_URL.toString());
        data.put("fileId", "journals.html");

        return new CrawlTaskBuilder()
            .ofType(WileyPublicationListCrawlTask.class)
            .withData(data)
            .withInterval(Duration.standardDays(1))
            .withId("wiley:journal-list")
            .build();
    }
}