package wwmm.pubcrawler.crawlers;

import wwmm.pubcrawler.model.Article;

public interface ArticleHandler {

    void handleArticleLink(Article article);

    void handleArticle(Article article);
}
