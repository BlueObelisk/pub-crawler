package wwmm.pubcrawler.crawlers;

import wwmm.pubcrawler.crawler.Task;
import wwmm.pubcrawler.model.IssueLink;
import wwmm.pubcrawler.model.Journal;
import wwmm.pubcrawler.tasks.IssueTocCrawlTaskData;

/**
 * @author Sam Adams
 */
public interface IssueTocCrawlTaskFactory<T extends IssueTocCrawlTaskData> {

    Task<T> createCurrentIssueTocCrawlTask(Journal journal);

    Task<T> createIssueTocCrawlTask(IssueLink issueLink);

}
