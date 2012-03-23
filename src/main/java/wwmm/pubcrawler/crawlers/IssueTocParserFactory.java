package wwmm.pubcrawler.crawlers;

import nu.xom.Document;
import wwmm.pubcrawler.crawlers.IssueTocParser;

import java.net.URI;

/**
 * @author Sam Adams
 */
public interface IssueTocParserFactory {
    
    IssueTocParser createIssueTocParser(Document html, URI url, String journal);
    
}
