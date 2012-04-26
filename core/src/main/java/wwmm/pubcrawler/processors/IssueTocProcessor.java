package wwmm.pubcrawler.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wwmm.pubcrawler.archivers.ArticleArchiver;
import wwmm.pubcrawler.archivers.IssueArchiver;
import wwmm.pubcrawler.crawlers.IssueHandler;
import wwmm.pubcrawler.parsers.IssueTocParser;
import wwmm.pubcrawler.parsers.IssueTocParserFactory;
import wwmm.pubcrawler.model.Article;
import wwmm.pubcrawler.model.Issue;
import wwmm.pubcrawler.model.id.JournalId;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

/**
 * @author Sam Adams
 */
@Singleton
public class IssueTocProcessor<T> {

    private static final Logger LOG = LoggerFactory.getLogger(IssueTocProcessor.class);

    private final IssueArchiver issueArchiver;
    private final ArticleArchiver articleArchiver;
    private final IssueHandler issueHandler;
    private final IssueTocParserFactory<T> parserFactory;

    @Inject
    public IssueTocProcessor(final IssueArchiver issueArchiver, final ArticleArchiver articleArchiver, final IssueHandler issueHandler, final IssueTocParserFactory<T> parserFactory) {
        this.issueArchiver = issueArchiver;
        this.articleArchiver = articleArchiver;
        this.issueHandler = issueHandler;
        this.parserFactory = parserFactory;
    }

    public void process(final String id, final JournalId journalId, final T resource) {
        final IssueTocParser parser = parserFactory.createIssueTocParser(journalId, resource);
        handleIssueDetails(id, parser);
        handleIssueLinks(id, parser);
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
