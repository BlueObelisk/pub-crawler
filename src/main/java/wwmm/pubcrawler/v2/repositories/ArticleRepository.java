package wwmm.pubcrawler.v2.repositories;

import wwmm.pubcrawler.model.Article;
import wwmm.pubcrawler.model.Issue;

import java.util.List;

/**
 * @author Sam Adams
 */
public interface ArticleRepository {

    void updateArticle(Article article);

    List<Article> getArticlesForIssue(String issueId);

}
