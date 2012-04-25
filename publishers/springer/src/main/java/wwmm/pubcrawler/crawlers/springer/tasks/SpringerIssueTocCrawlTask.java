package wwmm.pubcrawler.crawlers.springer.tasks;

import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import wwmm.pubcrawler.archivers.ArticleArchiver;
import wwmm.pubcrawler.archivers.IssueArchiver;
import wwmm.pubcrawler.controller.Fetcher;
import wwmm.pubcrawler.controller.URITask;
import wwmm.pubcrawler.crawlers.BasicIssueTocCrawlerTask;
import wwmm.pubcrawler.crawlers.IssueHandler;
import wwmm.pubcrawler.crawlers.springer.SpringerIssueTocParserFactory;
import wwmm.pubcrawler.http.HtmlDocument;
import wwmm.pubcrawler.processors.IssueTocProcessor;

import javax.inject.Inject;

/**
 * @author Sam Adams
 */
public class SpringerIssueTocCrawlTask extends BasicIssueTocCrawlerTask {

    @Inject
    public SpringerIssueTocCrawlTask(final Fetcher<URITask, CrawlerResponse> fetcher, final SpringerIssueTocParserFactory parserFactory, final ArticleArchiver articleArchiver, final IssueArchiver issueArchiver, final IssueHandler issueHandler) {
        super(fetcher, new IssueTocProcessor<HtmlDocument>(issueArchiver, articleArchiver, issueHandler, parserFactory));
    }

}
