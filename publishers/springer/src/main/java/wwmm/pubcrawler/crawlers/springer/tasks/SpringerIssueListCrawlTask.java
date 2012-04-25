package wwmm.pubcrawler.crawlers.springer.tasks;

import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import wwmm.pubcrawler.controller.Fetcher;
import wwmm.pubcrawler.controller.URITask;
import wwmm.pubcrawler.crawlers.BasicIssueListHttpCrawlTask;
import wwmm.pubcrawler.crawlers.IssueHandler;
import wwmm.pubcrawler.crawlers.springer.SpringerIssueListParserFactory;
import wwmm.pubcrawler.http.DocumentResource;
import wwmm.pubcrawler.processors.IssueListProcessor;

import javax.inject.Inject;

/**
 * @author Sam Adams
 */
public class SpringerIssueListCrawlTask extends BasicIssueListHttpCrawlTask {

    @Inject
    public SpringerIssueListCrawlTask(final Fetcher<URITask, CrawlerResponse> fetcher, final SpringerIssueListParserFactory parserFactory, final IssueHandler issueHandler) {
        super(fetcher, new IssueListProcessor<DocumentResource>(parserFactory, issueHandler));
    }

}
