package wwmm.pubcrawler.crawlers;

import wwmm.pubcrawler.model.id.IssueId;
import wwmm.pubcrawler.model.id.JournalId;
import wwmm.pubcrawler.v2.crawler.CrawlTask;

import java.net.URI;

/**
 * @author Sam Adams
 */
public interface IssueTocCrawlTaskFactory {

    CrawlTask createCurrentIssueTocCrawlTask(JournalId journalId, URI url);

    CrawlTask createIssueTocCrawlTask(IssueId issueId, URI url);

}
