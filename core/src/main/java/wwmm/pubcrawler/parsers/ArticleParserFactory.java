package wwmm.pubcrawler.parsers;

import wwmm.pubcrawler.model.Article;

/**
 * @author Sam Adams
 */
public interface ArticleParserFactory<T> {
    
    ArticleParser createArticleParser(Article articleRef, T resource);
    
}
