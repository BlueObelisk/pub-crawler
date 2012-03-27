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

}
