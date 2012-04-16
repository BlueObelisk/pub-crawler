package wwmm.pubcrawler.crawlers.acta.tasks;

import nu.xom.Document;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import wwmm.pubcrawler.controller.Fetcher;
import wwmm.pubcrawler.controller.URITask;
import wwmm.pubcrawler.crawler.CrawlRunner;
import wwmm.pubcrawler.crawler.TaskData;
import wwmm.pubcrawler.crawler.common.HtmlCrawler;
import wwmm.pubcrawler.crawlers.BasicHttpCrawlTask;
import wwmm.pubcrawler.crawlers.IssueHandler;
import wwmm.pubcrawler.crawlers.IssueTocParser;
import wwmm.pubcrawler.crawlers.acta.parsers.IucrIssueListParser;
import wwmm.pubcrawler.model.Article;
import wwmm.pubcrawler.model.Issue;
import wwmm.pubcrawler.model.id.JournalId;
import wwmm.pubcrawler.model.id.PublisherId;
import wwmm.pubcrawler.utils.HtmlUtils;

import javax.inject.Inject;
import java.net.URI;
import java.util.List;

/**
 * @author Sam Adams
 */
public class IucrIssueListCrawlerTask extends BasicHttpCrawlTask {

    private final IssueHandler issueHandler;

    @Inject
    public IucrIssueListCrawlerTask(final Fetcher<URITask, CrawlerResponse> fetcher, final IssueHandler issueHandler) {
        super(fetcher);
        this.issueHandler = issueHandler;
    }

    @Override
    protected void handleResponse(final String id, final TaskData data, final CrawlerResponse response) throws Exception {
        final Document html = HtmlUtils.readHtmlDocument(response);
        final URI url = URI.create(data.getString("url"));

        final PublisherId publisherId = new PublisherId(data.getString("publisher"));
        final JournalId journalId = new JournalId(publisherId, data.getString("journal"));

        final IucrIssueListParser parser = new IucrIssueListParser(html, url, journalId);
        for (final Issue issue : parser.findIssues()) {
            issueHandler.handleIssueLink(issue);
        }
    }

}
