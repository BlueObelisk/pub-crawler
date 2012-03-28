package wwmm.pubcrawler.crawlers.springer.tasks;

import nu.xom.Document;
import wwmm.pubcrawler.crawlers.springer.parsers.SpringerPublicationListParser;

import javax.inject.Singleton;
import java.net.URI;

/**
 * @author Sam Adams
 */
@Singleton
public class SpringerPublicationListParserFactory {

    public SpringerPublicationListParser createPublicationListParser(final Document html, final URI url) {
        return new SpringerPublicationListParser(html, url);
    }

}
