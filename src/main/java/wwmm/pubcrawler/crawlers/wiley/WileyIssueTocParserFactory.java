package wwmm.pubcrawler.crawlers.wiley;

import nu.xom.Document;
import wwmm.pubcrawler.crawlers.IssueTocParser;
import wwmm.pubcrawler.crawlers.IssueTocParserFactory;

import java.net.URI;

/**
 * @author Sam Adams
 */
public class WileyIssueTocParserFactory implements IssueTocParserFactory {

    @Override
    public IssueTocParser createIssueTocParser(final Document html, final URI url, final String journal) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

}
