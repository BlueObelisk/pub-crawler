package wwmm.pubcrawler.crawlers;

import wwmm.pubcrawler.v2.crawler.CrawlTask;

import java.net.URI;

/**
 * @author Sam Adams
 */
public interface IssueTocCrawlTaskFactory {

    CrawlTask createCurrentIssueTocCrawlTask(String journal, URI url);

    CrawlTask createIssueTocCrawlTask(String journal, URI url, String id);

}
