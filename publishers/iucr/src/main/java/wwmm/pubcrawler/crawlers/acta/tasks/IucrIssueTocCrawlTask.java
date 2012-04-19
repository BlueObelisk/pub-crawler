package wwmm.pubcrawler.crawlers.acta.tasks;

import nu.xom.Document;
import org.joda.time.Duration;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import wwmm.pubcrawler.archivers.ArticleArchiver;
import wwmm.pubcrawler.archivers.IssueArchiver;
import wwmm.pubcrawler.controller.Fetcher;
import wwmm.pubcrawler.controller.URITask;
import wwmm.pubcrawler.crawler.TaskData;
import wwmm.pubcrawler.crawlers.HttpCrawlTask;
import wwmm.pubcrawler.crawlers.IssueHandler;
import wwmm.pubcrawler.crawlers.IssueTocParser;
import wwmm.pubcrawler.crawlers.acta.IucrIssueTocParserFactory;
import wwmm.pubcrawler.model.Article;
import wwmm.pubcrawler.model.Issue;
import wwmm.pubcrawler.model.id.JournalId;
import wwmm.pubcrawler.model.id.PublisherId;
import wwmm.pubcrawler.utils.HtmlUtils;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URI;

/**
 * @author Sam Adams
 */
public class IucrIssueTocCrawlTask extends HttpCrawlTask {

    private final IucrIssueTocParserFactory parserFactory;
    private final ArticleArchiver articleArchiver;
    private final IssueArchiver issueArchiver;
    private final IssueHandler issueHandler;

    @Inject
    public IucrIssueTocCrawlTask(final IucrIssueTocParserFactory parserFactory, final ArticleArchiver articleArchiver, final IssueArchiver issueArchiver, final IssueHandler issueHandler, final Fetcher<URITask, CrawlerResponse> fetcher) {
        super(fetcher);
        this.parserFactory = parserFactory;
        this.articleArchiver = articleArchiver;
        this.issueArchiver = issueArchiver;
        this.issueHandler = issueHandler;
    }

    @Override
    public void run(final String id, final TaskData data) throws Exception {
        final Duration maxAge = data.containsKey("maxAge") ? new Duration(Long.valueOf(data.getString("maxAge"))) : null;
        final URI url = URI.create(data.getString("url"));

        final CrawlerResponse bodyResponse = fetchResource(id, "isscontsbdy.html", url.resolve("isscontsbdy.html"), null, maxAge);
        final CrawlerResponse headResponse = fetchResource(id, "isscontshdr.html", url.resolve("isscontshdr.html"), null, maxAge);

        handleResponse(id, data, bodyResponse, headResponse);
    }

    private void handleResponse(final String id, final TaskData data, final CrawlerResponse bodyResponse, final CrawlerResponse headResponse) throws IOException {
        final Document bodyHtml = HtmlUtils.readHtmlDocument(bodyResponse);
        final Document headHtml = HtmlUtils.readHtmlDocument(headResponse);

        final PublisherId publisherId = new PublisherId(data.getString("publisher"));
        final JournalId journalId = new JournalId(publisherId, data.getString("journal"));

        final IssueTocParser parser = parserFactory.createIssueTocParser(bodyHtml, headHtml, journalId);

        final Issue issue = parser.getIssueDetails();
        issueArchiver.archive(issue);

        for (final Article article : parser.getArticles()) {
            article.setIssueRef(issue.getId());
            articleArchiver.archive(article);
        }

        issueHandler.handleIssue(issue);
    }

}
