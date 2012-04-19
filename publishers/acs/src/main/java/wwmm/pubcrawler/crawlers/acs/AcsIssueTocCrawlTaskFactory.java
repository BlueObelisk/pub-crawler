package wwmm.pubcrawler.crawlers.acs;

import wwmm.pubcrawler.crawlers.AbstractIssueTocCrawlTaskFactory;
import wwmm.pubcrawler.crawlers.acs.tasks.AcsIssueTocCrawlTask;
import wwmm.pubcrawler.model.id.IssueId;
import wwmm.pubcrawler.model.id.JournalId;
import wwmm.pubcrawler.crawler.CrawlRunner;
import wwmm.pubcrawler.crawler.CrawlTask;

import javax.inject.Singleton;
import java.net.URI;

/**
 * @author Sam Adams
 */
@Singleton
public class AcsIssueTocCrawlTaskFactory extends AbstractIssueTocCrawlTaskFactory {
    
    @Override
    public CrawlTask createCurrentIssueTocCrawlTask(final JournalId journalId, final URI url) {
        return createIssueTocCrawlTask(new IssueId(journalId, "current"), url);
    }

    @Override
    protected Class<? extends CrawlRunner> getCrawlerType() {
        return AcsIssueTocCrawlTask.class;
    }
    
}
