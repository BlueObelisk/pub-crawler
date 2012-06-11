package wwmm.pubcrawler.crawlers.acs;

import wwmm.pubcrawler.crawlers.acs.parsers.AcsArticleSuppInfoPageParser;
import wwmm.pubcrawler.http.DocumentResource;
import wwmm.pubcrawler.model.id.ArticleId;
import wwmm.pubcrawler.parsers.ArticleParser;
import wwmm.pubcrawler.parsers.ArticleParserFactory;

import javax.inject.Singleton;

/**
 * @author Sam Adams
 */
@Singleton
public class AcsArticleParserFactory implements ArticleParserFactory<DocumentResource> {

    @Override
    public ArticleParser createArticleParser(final ArticleId articleRef, final DocumentResource htmlDoc) {
        return new AcsArticleSuppInfoPageParser(articleRef, htmlDoc.getDocument(), htmlDoc.getUrl());
    }
    
}
