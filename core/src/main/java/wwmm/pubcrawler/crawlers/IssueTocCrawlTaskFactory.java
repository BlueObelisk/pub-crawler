package wwmm.pubcrawler.crawlers;

import wwmm.pubcrawler.crawler.Task;
import wwmm.pubcrawler.model.id.IssueId;
import wwmm.pubcrawler.model.id.JournalId;
import wwmm.pubcrawler.tasks.IssueTocCrawlTaskData;

import java.net.URI;

/**
 * @author Sam Adams
 */
public interface IssueTocCrawlTaskFactory {

    Task<IssueTocCrawlTaskData> createCurrentIssueTocCrawlTask(JournalId journalId, URI url);

    Task<IssueTocCrawlTaskData> createIssueTocCrawlTask(IssueId issueId, URI url);

}
