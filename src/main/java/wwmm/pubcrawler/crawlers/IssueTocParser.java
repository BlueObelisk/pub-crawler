package wwmm.pubcrawler.crawlers;

import wwmm.pubcrawler.model.Article;
import wwmm.pubcrawler.model.Issue;

import java.net.URI;
import java.util.List;

/**
 * @author Sam Adams
 */
public interface IssueTocParser {

    Issue getIssueDetails();
    
    List<Article> getArticles();

    Issue getPreviousIssue();

    List<Issue> getIssueLinks();

    List<URI> getIssueListLinks();

}
