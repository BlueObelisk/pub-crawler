package wwmm.pubcrawler.crawlers.elsevier;

import org.joda.time.Duration;
import wwmm.pubcrawler.crawler.CrawlTask;
import wwmm.pubcrawler.crawler.CrawlTaskBuilder;
import wwmm.pubcrawler.crawlers.elsevier.tasks.ElsevierPublicationListCrawlTask;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Sam Adams
 */
public class ElsevierPublicationListCrawlTaskFactory {

    public CrawlTask createCrawlTask() {
        final Map<String,String> data = new HashMap<String, String>();
        data.put("url", Elsevier.OPML_URL.toString());
        data.put("fileId", "opml.xml");

        return new CrawlTaskBuilder()
            .ofType(ElsevierPublicationListCrawlTask.class)
            .withData(data)
            .withMaxAge(Duration.standardDays(1))
            .withId("elsevier:journal-list")
            .build();
    }

}
