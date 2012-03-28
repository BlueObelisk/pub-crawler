package wwmm.pubcrawler.crawlers.acs;

import nu.xom.Document;
import wwmm.pubcrawler.crawlers.PublicationListParser;
import wwmm.pubcrawler.crawlers.PublicationListParserFactory;
import wwmm.pubcrawler.crawlers.acs.parsers.AcsPublicationListParser;

import javax.inject.Singleton;

/**
 * @author Sam Adams
 */
@Singleton
public class AcsPublicationListParserFactory implements PublicationListParserFactory {
    
    @Override
    public PublicationListParser createPublicationListParser(final Document document) {
        return new AcsPublicationListParser(document);
    }
    
}
