package wwmm.pubcrawler.crawlers.nature;

import wwmm.pubcrawler.http.DocumentResource;
import wwmm.pubcrawler.parsers.PublicationListParser;
import wwmm.pubcrawler.parsers.PublicationListParserFactory;

import javax.inject.Singleton;

/**
 * @author Sam Adams
 */
@Singleton
public class NaturePublicationListParserFactory implements PublicationListParserFactory<DocumentResource> {

    @Override
    public PublicationListParser createPublicationListParser(final DocumentResource resource) {
        return null;
    }
}
