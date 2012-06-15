package wwmm.pubcrawler.repositories;

import wwmm.pubcrawler.model.Article;

import java.util.List;

/**
 * @author Sam Adams
 */
public interface ArticleRepository {

    void updateArticle(Article article);

    List<Article> getArticlesForIssue(String issueId);

    void saveOrUpdateArticle(Article article);

    long getNextSequence();
}
