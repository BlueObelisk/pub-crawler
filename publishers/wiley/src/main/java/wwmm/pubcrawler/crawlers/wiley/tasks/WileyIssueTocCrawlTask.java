package wwmm.pubcrawler.crawlers.wiley.tasks;

import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import wwmm.pubcrawler.archivers.ArticleArchiver;
import wwmm.pubcrawler.archivers.IssueArchiver;
import wwmm.pubcrawler.controller.Fetcher;
import wwmm.pubcrawler.controller.URITask;
import wwmm.pubcrawler.crawlers.BasicIssueTocCrawlerTask;
import wwmm.pubcrawler.crawlers.IssueHandler;
import wwmm.pubcrawler.crawlers.wiley.WileyIssueTocParserFactory;
import wwmm.pubcrawler.http.HtmlDocument;
import wwmm.pubcrawler.processors.IssueTocProcessor;

import javax.inject.Inject;

/**
 * @author Sam Adams
 */
public class WileyIssueTocCrawlTask extends BasicIssueTocCrawlerTask {

    @Inject
    public WileyIssueTocCrawlTask(final Fetcher<URITask, CrawlerResponse> fetcher, final WileyIssueTocParserFactory parserFactory, final ArticleArchiver articleArchiver, final IssueArchiver issueArchiver, final IssueHandler issueHandler) {
        super(fetcher, new IssueTocProcessor<HtmlDocument>(issueArchiver, articleArchiver, issueHandler, parserFactory));
    }

}
