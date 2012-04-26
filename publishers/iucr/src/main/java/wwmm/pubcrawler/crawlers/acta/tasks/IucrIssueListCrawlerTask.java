package wwmm.pubcrawler.crawlers.acta.tasks;

import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import wwmm.pubcrawler.http.Fetcher;
import wwmm.pubcrawler.http.URITask;
import wwmm.pubcrawler.crawlers.BasicIssueListHttpCrawlTask;
import wwmm.pubcrawler.crawlers.IssueHandler;
import wwmm.pubcrawler.crawlers.acta.IucrIssueListParserFactory;
import wwmm.pubcrawler.http.DocumentResource;
import wwmm.pubcrawler.processors.IssueListProcessor;

import javax.inject.Inject;

/**
 * @author Sam Adams
 */
public class IucrIssueListCrawlerTask extends BasicIssueListHttpCrawlTask {

    @Inject
    public IucrIssueListCrawlerTask(final Fetcher<URITask, CrawlerResponse> fetcher, final IucrIssueListParserFactory parserFactory, final IssueHandler issueHandler) {
        super(fetcher, new IssueListProcessor<DocumentResource>(parserFactory, issueHandler));
    }

}
