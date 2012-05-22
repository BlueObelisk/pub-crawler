package wwmm.pubcrawler.crawlers.wiley;

import wwmm.pubcrawler.crawler.Task;
import wwmm.pubcrawler.crawlers.AbstractIssueTocCrawlTaskFactory;
import wwmm.pubcrawler.crawlers.wiley.tasks.WileyIssueTocCrawlTask;
import wwmm.pubcrawler.model.id.JournalId;
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
    public Task<IssueTocCrawlTaskData> createCurrentIssueTocCrawlTask(final JournalId journalId, final URI url) {
        final String journal = journalId.getJournalPart();
        final URI currentIssueUrl = URI.create("http://onlinelibrary.wiley.com/journal/" + journal + "/currentissue");
        return super.createCurrentIssueTocCrawlTask(journalId, currentIssueUrl);
    }

    @Override
    protected TaskSpecification<IssueTocCrawlTaskData> getCrawlerType() {
        return WileyIssueTocCrawlTask.INSTANCE;
    }

}
