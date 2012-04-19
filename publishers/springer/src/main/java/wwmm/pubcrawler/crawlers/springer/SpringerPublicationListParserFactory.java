package wwmm.pubcrawler.crawlers.springer;

import nu.xom.Document;
import wwmm.pubcrawler.crawlers.PublicationListParserFactory;
import wwmm.pubcrawler.crawlers.springer.parsers.SpringerPublicationListParser;

import javax.inject.Singleton;
import java.net.URI;

/**
 * @author Sam Adams
 */
@Singleton
public class SpringerPublicationListParserFactory implements PublicationListParserFactory {

    @Override
    public SpringerPublicationListParser createPublicationListParser(final Document document) {
        return new SpringerPublicationListParser(document, URI.create(document.getBaseURI()));
    }

}
