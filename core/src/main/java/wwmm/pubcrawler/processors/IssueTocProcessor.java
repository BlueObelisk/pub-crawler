package wwmm.pubcrawler.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wwmm.pubcrawler.archivers.ArticleArchiver;
import wwmm.pubcrawler.archivers.IssueArchiver;
import wwmm.pubcrawler.crawlers.ArticleHandler;
import wwmm.pubcrawler.crawlers.IssueHandler;
import wwmm.pubcrawler.crawlers.ResourceProcessor;
import wwmm.pubcrawler.model.Article;
import wwmm.pubcrawler.model.Issue;
import wwmm.pubcrawler.model.IssueLink;
import wwmm.pubcrawler.model.id.JournalId;
import wwmm.pubcrawler.model.id.PublisherId;
import wwmm.pubcrawler.parsers.IssueTocParser;
import wwmm.pubcrawler.parsers.IssueTocParserFactory;
import wwmm.pubcrawler.tasks.IssueTocCrawlTaskData;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

/**
 * @author Sam Adams
 */
@Singleton
public class IssueTocProcessor<Resource> implements ResourceProcessor<Resource,IssueTocCrawlTaskData> {

    private static final Logger LOG = LoggerFactory.getLogger(IssueTocProcessor.class);

    private final IssueArchiver issueArchiver;
    private final IssueHandler issueHandler;
    private final ArticleArchiver articleArchiver;
    private final ArticleHandler articleHandler;
    private final IssueTocParserFactory<Resource> parserFactory;

    @Inject
    public IssueTocProcessor(final IssueArchiver issueArchiver, final IssueHandler issueHandler, final ArticleArchiver articleArchiver, final ArticleHandler articleHandler, final IssueTocParserFactory<Resource> parserFactory) {
        this.issueArchiver = issueArchiver;
        this.issueHandler = issueHandler;
        this.articleArchiver = articleArchiver;
        this.articleHandler = articleHandler;
        this.parserFactory = parserFactory;
    }

    @Override
    public void process(final String taskId, final IssueTocCrawlTaskData data, final Resource resource) {
        final PublisherId publisherId = new PublisherId(data.getPublisher());
        final JournalId journalId = new JournalId(publisherId, data.getJournal());
        final IssueTocParser parser = parserFactory.createIssueTocParser(journalId, resource);
        handleIssueDetails(taskId, parser);
//        handleIssueLinks(taskId, parser);
        handleArticles(taskId, parser);
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
                    articleHandler.handleArticleLink(article);
                }
            }
        } catch (Exception e) {
            LOG.warn("Error finding articles [" + id + "]", e);
        }
    }

    private void handleIssueLinks(final String id, final IssueTocParser parser) {
        try {
            final List<IssueLink> issueLinks = parser.getIssueLinks();
            if (issueLinks != null) {
                for (final IssueLink issueLink : issueLinks) {
                    issueHandler.handleIssueLink(issueLink);
                }
            }
        } catch (Exception e) {
            LOG.warn("Error finding issue links [" + id + "]", e);
        }
    }
}
