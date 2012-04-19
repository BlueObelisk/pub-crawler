package wwmm.pubcrawler.crawlers;

import nu.xom.Document;

/**
 * @author Sam Adams
 */
public interface PublicationListParserFactory {
    
    PublicationListParser createPublicationListParser(Document document);
    
}
