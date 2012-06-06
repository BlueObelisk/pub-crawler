package wwmm.pubcrawler.crawlers.wiley;

import wwmm.pubcrawler.crawler.Task;
import wwmm.pubcrawler.crawlers.AbstractIssueTocCrawlTaskFactory;
import wwmm.pubcrawler.crawlers.wiley.tasks.WileyIssueTocCrawlTask;
import wwmm.pubcrawler.model.Journal;
import wwmm.pubcrawler.model.id.IssueId;
import wwmm.pubcrawler.tasks.IssueTocCrawlTaskData;
import wwmm.pubcrawler.tasks.TaskSpecification;

import javax.inject.Singleton;
import java.net.URI;

/**
 * @author Sam Adams
 */
@Singleton
public class WileyIssueTocCrawlTaskFactory extends AbstractIssueTocCrawlTaskFactory {

    @Override
    public Task<IssueTocCrawlTaskData> createCurrentIssueTocCrawlTask(final Journal journal) {
        final String journalId = journal.getId().getJournalPart();
        final URI currentIssueUrl = URI.create("http://onlinelibrary.wiley.com/journal/" + journalId + "/currentissue");
        return createIssueTocCrawlTask(new IssueId(journal.getId(), "current"), currentIssueUrl);
    }

    @Override
    protected TaskSpecification<IssueTocCrawlTaskData> getCrawlerType() {
        return WileyIssueTocCrawlTask.INSTANCE;
    }

}
