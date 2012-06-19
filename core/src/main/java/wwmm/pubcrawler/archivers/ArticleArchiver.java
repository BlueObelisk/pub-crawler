package wwmm.pubcrawler.archivers;

import wwmm.pubcrawler.model.Article;

/**
 * @author Sam Adams
 */
public interface ArticleArchiver {
    
    void archive(Article article);
    
}
