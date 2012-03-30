package wwmm.pubcrawler.crawlers.wiley.tasks;

import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import wwmm.pubcrawler.controller.ArticleArchiver;
import wwmm.pubcrawler.controller.Fetcher;
import wwmm.pubcrawler.controller.IssueArchiver;
import wwmm.pubcrawler.controller.URITask;
import wwmm.pubcrawler.crawlers.BasicIssueTocCrawlerTask;
import wwmm.pubcrawler.crawlers.IssueHandler;
import wwmm.pubcrawler.crawlers.wiley.WileyIssueTocParserFactory;

import javax.inject.Inject;

/**
 * @author Sam Adams
 */
public class WileyIssueTocCrawlTask extends BasicIssueTocCrawlerTask {

    @Inject
    public WileyIssueTocCrawlTask(final Fetcher<URITask, CrawlerResponse> fetcher, final WileyIssueTocParserFactory parserFactory, final ArticleArchiver archiver, final IssueArchiver issueArchiver, final IssueHandler issueHandler) {
        super(fetcher, parserFactory, archiver, issueArchiver, issueHandler);
    }

}
