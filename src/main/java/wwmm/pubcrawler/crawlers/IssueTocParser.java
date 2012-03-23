package wwmm.pubcrawler.crawlers;

import wwmm.pubcrawler.model.Article;
import wwmm.pubcrawler.model.Issue;

import java.util.List;

/**
 * @author Sam Adams
 */
public interface IssueTocParser {

    List<Article> getArticles();

    Issue getPreviousIssue();

}
