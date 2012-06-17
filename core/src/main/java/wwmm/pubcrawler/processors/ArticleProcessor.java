package wwmm.pubcrawler.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wwmm.pubcrawler.archivers.Archiver;
import wwmm.pubcrawler.crawlers.ArticleHandler;
import wwmm.pubcrawler.crawlers.ResourceProcessor;
import wwmm.pubcrawler.model.Article;
import wwmm.pubcrawler.model.id.ArticleId;
import wwmm.pubcrawler.parsers.ArticleParser;
import wwmm.pubcrawler.parsers.ArticleParserFactory;
import wwmm.pubcrawler.tasks.ArticleCrawlTaskData;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author Sam Adams
 */
@Singleton
public class ArticleProcessor<Resource> implements ResourceProcessor<Resource, ArticleCrawlTaskData> {

    private static final Logger LOG = LoggerFactory.getLogger(ArticleProcessor.class);

    private final Archiver<Article> articleArchiver;
    private final ArticleHandler articleHandler;
    private final ArticleParserFactory<Resource> parserFactory;

    @Inject
    public ArticleProcessor(final Archiver<Article> articleArchiver, final ArticleHandler articleHandler, final ArticleParserFactory<Resource> parserFactory) {
        this.articleArchiver = articleArchiver;
        this.articleHandler = articleHandler;
        this.parserFactory = parserFactory;
    }

    @Override
    public void process(final String taskId, final ArticleCrawlTaskData data, final Resource resource) {
        final ArticleId articleRef = data.getArticleRef();
        final ArticleParser parser = parserFactory.createArticleParser(articleRef, resource);
        handleArticle(taskId, parser);
    }

    private void handleArticle(final String id, final ArticleParser parser) {
        try {
            final Article article = parser.getArticleDetails();
            articleArchiver.archive(article);
            articleHandler.handleArticle(article);
        } catch (Exception e) {
            LOG.warn("Error finding issue details [" + id + "]", e);
        }
    }
}
