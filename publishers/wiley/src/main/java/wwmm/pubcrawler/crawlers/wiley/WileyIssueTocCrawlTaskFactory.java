package wwmm.pubcrawler.crawlers.wiley;

import wwmm.pubcrawler.crawlers.AbstractIssueTocCrawlTaskFactory;
import wwmm.pubcrawler.crawlers.wiley.tasks.WileyIssueTocCrawlTask;
import wwmm.pubcrawler.model.id.JournalId;
import wwmm.pubcrawler.crawler.CrawlRunner;
import wwmm.pubcrawler.crawler.CrawlTask;

import javax.inject.Singleton;
import java.net.URI;

/**
 * @author Sam Adams
 */
@Singleton
public class WileyIssueTocCrawlTaskFactory extends AbstractIssueTocCrawlTaskFactory {

    @Override
    public CrawlTask createCurrentIssueTocCrawlTask(final JournalId journalId, final URI url) {
        final String journal = journalId.getJournalPart();
        final URI currentIssueUrl = URI.create("http://onlinelibrary.wiley.com/journal/" + journal + "/currentissue");
        return super.createCurrentIssueTocCrawlTask(journalId, currentIssueUrl);
    }

    @Override
    protected Class<? extends CrawlRunner> getCrawlerType() {
        return WileyIssueTocCrawlTask.class;
    }

}
