package wwmm.pubcrawler.crawlers.wiley;

import nu.xom.Document;
import wwmm.pubcrawler.crawlers.PublicationListParser;
import wwmm.pubcrawler.crawlers.PublicationListParserFactory;
import wwmm.pubcrawler.crawlers.wiley.parsers.WileyPublicationListParser;

import javax.inject.Singleton;
import java.net.URI;

/**
 * @author Sam Adams
 */
@Singleton
public class WileyPublicationListParserFactory implements PublicationListParserFactory {

    @Override
    public PublicationListParser createPublicationListParser(final Document document) {
        return new WileyPublicationListParser(document, URI.create(document.getBaseURI()));
    }

}
