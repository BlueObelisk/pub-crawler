package wwmm.pubcrawler.crawlers.wiley.tasks;

import wwmm.pubcrawler.controller.ArticleArchiver;
import wwmm.pubcrawler.controller.BasicHttpFetcher;
import wwmm.pubcrawler.controller.IssueArchiver;
import wwmm.pubcrawler.crawlers.BasicIssueTocCrawlerTask;
import wwmm.pubcrawler.crawlers.wiley.Wiley;
import wwmm.pubcrawler.crawlers.wiley.WileyIssueTocParserFactory;
import wwmm.pubcrawler.model.Issue;
import wwmm.pubcrawler.v2.crawler.CrawlTask;
import wwmm.pubcrawler.v2.crawler.TaskQueue;

import javax.inject.Inject;

/**
 * @author Sam Adams
 */
public class WileyIssueTocCrawlTask extends BasicIssueTocCrawlerTask {

    @Inject
    public WileyIssueTocCrawlTask(final BasicHttpFetcher fetcher, final WileyIssueTocParserFactory parserFactory, final TaskQueue taskQueue, final ArticleArchiver archiver, final IssueArchiver issueArchiver) {
        super(fetcher, parserFactory, taskQueue, archiver, issueArchiver);
    }

    @Override
    protected CrawlTask createIssueTocTask(final String journal, final Issue prev) {
        return Wiley.createIssueTocTask(journal, prev.getVolume() + '/' + prev.getNumber());
    }

}
