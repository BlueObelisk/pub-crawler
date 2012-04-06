package wwmm.pubcrawler.crawlers;

import nu.xom.Document;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import wwmm.pubcrawler.controller.ArticleArchiver;
import wwmm.pubcrawler.controller.Fetcher;
import wwmm.pubcrawler.controller.IssueArchiver;
import wwmm.pubcrawler.controller.URITask;
import wwmm.pubcrawler.model.Article;
import wwmm.pubcrawler.model.Issue;
import wwmm.pubcrawler.model.id.IssueId;
import wwmm.pubcrawler.model.id.JournalId;
import wwmm.pubcrawler.model.id.PublisherId;
import wwmm.pubcrawler.utils.HtmlUtils;
import wwmm.pubcrawler.v2.crawler.TaskData;

import java.net.URI;

/**
 * @author Sam Adams
 */
public abstract class BasicIssueTocCrawlerTask extends BasicHttpCrawlTask {

    private final IssueTocParserFactory parserFactory;
    private final ArticleArchiver articleArchiver;
    private final IssueArchiver issueArchiver;
    private final IssueHandler issueHandler;

    public BasicIssueTocCrawlerTask(final Fetcher<URITask, CrawlerResponse> fetcher, final IssueTocParserFactory parserFactory, final ArticleArchiver archiver, final IssueArchiver issueArchiver, final IssueHandler issueHandler) {
        super(fetcher);
        this.parserFactory = parserFactory;
        this.articleArchiver = archiver;
        this.issueArchiver = issueArchiver;
        this.issueHandler = issueHandler;
    }

    @Override
    protected void handleResponse(final String id, final TaskData data, final CrawlerResponse response) throws Exception {
        final Document html = HtmlUtils.readHtmlDocument(response);
        final URI url = URI.create(data.getString("url"));
        
        final PublisherId publisherId = new PublisherId(data.getString("publisher"));
        final JournalId journalId = new JournalId(publisherId, data.getString("journal"));
        
        final IssueTocParser parser = parserFactory.createIssueTocParser(html, url, journalId);

        final Issue issue = parser.getIssueDetails();
        issueArchiver.archive(issue);

        for (final Article article : parser.getArticles()) {
            article.setIssueRef(issue.getId());
            articleArchiver.archive(article);
        }
        
        issueHandler.handleIssue(issue);
    }

}
