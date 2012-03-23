package wwmm.pubcrawler.crawlers.acs;

import nu.xom.Document;
import wwmm.pubcrawler.crawlers.IssueTocParser;
import wwmm.pubcrawler.crawlers.IssueTocParserFactory;
import wwmm.pubcrawler.crawlers.acs.parsers.AcsIssueTocParser;

import javax.inject.Singleton;
import java.net.URI;

/**
 * @author Sam Adams
 */
@Singleton
public class AcsIssueTocParserFactory implements IssueTocParserFactory {
    
    @Override
    public IssueTocParser createIssueTocParser(final Document html, final URI url, final String journal) {
        return new AcsIssueTocParser(html, url, journal);
    }
    
}
