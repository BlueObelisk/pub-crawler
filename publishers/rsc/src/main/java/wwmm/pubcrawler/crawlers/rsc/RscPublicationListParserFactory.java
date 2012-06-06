package wwmm.pubcrawler.crawlers.rsc;

import wwmm.pubcrawler.crawlers.rsc.parsers.RscPublicationListParser;
import wwmm.pubcrawler.http.DocumentResource;
import wwmm.pubcrawler.parsers.PublicationListParser;
import wwmm.pubcrawler.parsers.PublicationListParserFactory;

import javax.inject.Singleton;

/**
 * @author Sam Adams
 */
@Singleton
public class RscPublicationListParserFactory implements PublicationListParserFactory<DocumentResource> {

    @Override
    public PublicationListParser createPublicationListParser(final DocumentResource doc) {
        return new RscPublicationListParser(doc.getDocument(), doc.getUrl());
    }

}
