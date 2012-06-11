package wwmm.pubcrawler.parsers;

import wwmm.pubcrawler.model.id.ArticleId;

/**
 * @author Sam Adams
 */
public interface ArticleParserFactory<T> {
    
    ArticleParser createArticleParser(ArticleId articleRef, T resource);
    
}
