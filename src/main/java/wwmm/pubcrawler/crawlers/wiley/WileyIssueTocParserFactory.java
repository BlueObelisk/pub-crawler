package wwmm.pubcrawler.crawlers.wiley;

import nu.xom.Document;
import wwmm.pubcrawler.crawlers.IssueTocParser;
import wwmm.pubcrawler.crawlers.IssueTocParserFactory;
import wwmm.pubcrawler.crawlers.wiley.parsers.WileyIssueTocParser;

import javax.inject.Singleton;
import java.net.URI;

/**
 * @author Sam Adams
 */
@Singleton
public class WileyIssueTocParserFactory implements IssueTocParserFactory {

    @Override
    public IssueTocParser createIssueTocParser(final Document html, final URI url, final String journal) {
        return new WileyIssueTocParser(html, url, journal);
    }

}
