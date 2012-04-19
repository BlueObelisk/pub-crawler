package wwmm.pubcrawler.crawlers.acta;

import nu.xom.Document;
import wwmm.pubcrawler.crawlers.PublicationListParser;
import wwmm.pubcrawler.crawlers.PublicationListParserFactory;
import wwmm.pubcrawler.crawlers.acta.parsers.IucrPublicationListParser;

import javax.inject.Singleton;
import java.net.URI;

/**
 * @author Sam Adams
 */
@Singleton
public class IucrPublicationListParserFactory implements PublicationListParserFactory {

    @Override
    public PublicationListParser createPublicationListParser(final Document document) {
        return new IucrPublicationListParser(document, URI.create(document.getBaseURI()));
    }

}
