package wwmm.pubcrawler.crawlers.acs;

import wwmm.pubcrawler.parsers.PublicationListParser;
import wwmm.pubcrawler.parsers.PublicationListParserFactory;
import wwmm.pubcrawler.crawlers.acs.parsers.AcsPublicationListParser;
import wwmm.pubcrawler.http.DocumentResource;

import javax.inject.Singleton;

/**
 * @author Sam Adams
 */
@Singleton
public class AcsPublicationListParserFactory implements PublicationListParserFactory<DocumentResource> {
    
    @Override
    public PublicationListParser createPublicationListParser(final DocumentResource htmlDocument) {
        return new AcsPublicationListParser(htmlDocument.getDocument());
    }
    
}
