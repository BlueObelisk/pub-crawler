package wwmm.pubcrawler.crawlers.acta;

import wwmm.pubcrawler.crawler.Task;
import wwmm.pubcrawler.crawlers.AbstractIssueTocCrawlTaskFactory;
import wwmm.pubcrawler.crawlers.acta.tasks.IucrIssueTocCrawlTask;
import wwmm.pubcrawler.model.id.JournalId;
import wwmm.pubcrawler.tasks.IssueTocCrawlTaskData;
import wwmm.pubcrawler.tasks.TaskSpecification;

import java.net.URI;

/**
 * @author Sam Adams
 */
public class IucrIssueTocCrawlTaskFactory extends AbstractIssueTocCrawlTaskFactory {

    @Override
    public Task<IssueTocCrawlTaskData> createCurrentIssueTocCrawlTask(final JournalId journalId, final URI url) {
        final String journal = journalId.getJournalPart();
        final URI currentIssueUrl = URI.create("http://onlinelibrary.wiley.com/journal/" + journal + "/currentissue");
        return super.createCurrentIssueTocCrawlTask(journalId, currentIssueUrl);
    }

    @Override
    protected TaskSpecification<IssueTocCrawlTaskData> getCrawlerType() {
        return IucrIssueTocCrawlTask.INSTANCE;
    }

}
