package wwmm.pubcrawler.crawlers.acta;

import org.joda.time.Duration;
import wwmm.pubcrawler.crawler.CrawlTask;
import wwmm.pubcrawler.crawler.CrawlTaskBuilder;
import wwmm.pubcrawler.crawlers.acta.tasks.IucrPublicationListCrawlTask;

import java.util.HashMap;
import java.util.Map;

public class IucrPublicationListCrawlTaskFactory {

    public CrawlTask createCrawlTask() {
        final Map<String, String> data = new HashMap<String, String>();
        data.put("url", Iucr.JOURNALS_URL.toString());
        data.put("fileId", "index.html");

        return new CrawlTaskBuilder()
            .ofType(IucrPublicationListCrawlTask.class)
            .withData(data)
            .withMaxAge(Duration.standardDays(1))
            .withId("iucr:journal-list")
            .build();
    }
}