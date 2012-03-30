package wwmm.pubcrawler.crawlers.springer.tasks;

import nu.xom.Document;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import wwmm.pubcrawler.controller.Fetcher;
import wwmm.pubcrawler.controller.URITask;
import wwmm.pubcrawler.crawlers.BasicHttpCrawlTask;
import wwmm.pubcrawler.crawlers.IssueHandler;
import wwmm.pubcrawler.crawlers.springer.SpringerIssueListParserFactory;
import wwmm.pubcrawler.crawlers.springer.parsers.SpringerIssueListParser;
import wwmm.pubcrawler.model.Issue;
import wwmm.pubcrawler.utils.HtmlUtils;
import wwmm.pubcrawler.v2.crawler.TaskData;

import javax.inject.Inject;
import java.net.URI;

/**
 * @author Sam Adams
 */
public class SpringerIssueListCrawlTask extends BasicHttpCrawlTask {

    private final SpringerIssueListParserFactory parserFactory;
    private final IssueHandler issueHandler;

    @Inject
    public SpringerIssueListCrawlTask(final Fetcher<URITask, CrawlerResponse> fetcher, final SpringerIssueListParserFactory parserFactory, final IssueHandler issueHandler) {
        super(fetcher);
        this.parserFactory = parserFactory;
        this.issueHandler = issueHandler;
    }

    @Override
    protected void handleResponse(final String id, final TaskData data, final CrawlerResponse response) throws Exception {
        final Document html = HtmlUtils.readHtmlDocument(response);
        final URI url = URI.create(data.getString("url"));
        final String journal = data.getString("journal");
        final SpringerIssueListParser parser = parserFactory.createIssueListParser(html, url, journal);

        for (final Issue issue : parser.findIssues()) {
            issueHandler.handleIssue(issue);
        }

        // TODO find archive volumes/issues
    }

}
