package wwmm.pubcrawler.crawlers;

import nu.xom.Document;

import java.net.URI;

/**
 * @author Sam Adams
 */
public interface IssueTocParserFactory {
    
    IssueTocParser createIssueTocParser(Document html, URI url, String journal);
    
}
