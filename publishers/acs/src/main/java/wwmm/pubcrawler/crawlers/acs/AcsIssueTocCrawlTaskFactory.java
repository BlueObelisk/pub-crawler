package wwmm.pubcrawler.crawlers.acs;

import wwmm.pubcrawler.crawler.Task;
import wwmm.pubcrawler.crawlers.AbstractIssueTocCrawlTaskFactory;
import wwmm.pubcrawler.crawlers.acs.tasks.AcsIssueTocCrawlTask;
import wwmm.pubcrawler.model.id.IssueId;
import wwmm.pubcrawler.model.id.JournalId;
import wwmm.pubcrawler.tasks.IssueTocCrawlTaskData;
import wwmm.pubcrawler.tasks.TaskSpecification;

import javax.inject.Singleton;
import java.net.URI;

/**
 * @author Sam Adams
 */
@Singleton
public class AcsIssueTocCrawlTaskFactory extends AbstractIssueTocCrawlTaskFactory {
    
    @Override
    public Task createCurrentIssueTocCrawlTask(final JournalId journalId, final URI url) {
        return createIssueTocCrawlTask(new IssueId(journalId, "current"), url);
    }

    @Override
    protected TaskSpecification<IssueTocCrawlTaskData> getCrawlerType() {
        return AcsIssueTocCrawlTask.INSTANCE;
    }
}
