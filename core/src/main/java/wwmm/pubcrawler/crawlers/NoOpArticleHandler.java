package wwmm.pubcrawler.crawlers;

import wwmm.pubcrawler.model.Article;

import javax.inject.Singleton;

/**
 * @author Sam Adams
 */
@Singleton
public class NoOpArticleHandler implements ArticleHandler {

    @Override
    public void handleArticleLink(final Article article) {
    }

    @Override
    public void handleArticle(final Article article) {
    }

}
