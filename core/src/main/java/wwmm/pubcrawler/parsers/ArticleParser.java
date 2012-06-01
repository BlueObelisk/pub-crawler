package wwmm.pubcrawler.parsers;

import wwmm.pubcrawler.model.Article;
import wwmm.pubcrawler.model.Issue;

import java.net.URI;
import java.util.List;

/**
 * @author Sam Adams
 */
public interface ArticleParser {

    Article getArticleDetails();

}
