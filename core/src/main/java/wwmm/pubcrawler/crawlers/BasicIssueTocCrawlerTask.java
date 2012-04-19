package wwmm.pubcrawler.crawlers;

import nu.xom.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import wwmm.pubcrawler.archivers.ArticleArchiver;
import wwmm.pubcrawler.archivers.IssueArchiver;
import wwmm.pubcrawler.controller.Fetcher;
import wwmm.pubcrawler.controller.URITask;
import wwmm.pubcrawler.model.Article;
import wwmm.pubcrawler.model.Issue;
import wwmm.pubcrawler.model.id.JournalId;
import wwmm.pubcrawler.model.id.PublisherId;
import wwmm.pubcrawler.utils.HtmlUtils;
import wwmm.pubcrawler.crawler.TaskData;

import java.net.URI;
import java.util.List;

/**
 * @author Sam Adams
 */
public abstract class BasicIssueTocCrawlerTask extends BasicHttpCrawlTask {

    private static final Logger LOG = LoggerFactory.getLogger(BasicIssueTocCrawlerTask.class);
    
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

        handlePreviousIssue(id, parser);
        handleIssueLinks(id, parser);
        handleIssueDetails(id, parser);
        handleArticles(id, parser);
    }

    private void handleIssueDetails(final String id, final IssueTocParser parser) {
        try {
            final Issue issue = parser.getIssueDetails();
            issueArchiver.archive(issue);
        } catch (Exception e) {
            LOG.warn("Error finding issue details [" + id + "]", e);
        }
    }

    private void handleArticles(final String id, final IssueTocParser parser) {
        try {
            final List<Article> articles = parser.getArticles();
            if (articles != null) {
                for (final Article article : articles) {
                    articleArchiver.archive(article);
                }
            }
        } catch (Exception e) {
            LOG.warn("Error finding articles [" + id + "]", e);
        }
    }

    private void handlePreviousIssue(final String id, final IssueTocParser parser) {
        try {
            final Issue previousIssue = parser.getPreviousIssue();
            if (previousIssue != null) {
                issueHandler.handleIssueLink(previousIssue);
            }
        } catch (Exception e) {
            LOG.warn("Error finding previous issue [" + id + "]", e);
        }
    }

    private void handleIssueLinks(final String id, final IssueTocParser parser) {
        try {
            final List<Issue> issueLinks = parser.getIssueLinks();
            if (issueLinks != null) {
                for (final Issue issue : issueLinks) {
                    issueHandler.handleIssueLink(issue);
                }
            }
        } catch (Exception e) {
            LOG.warn("Error finding issue links [" + id + "]", e);
        }
    }

}
