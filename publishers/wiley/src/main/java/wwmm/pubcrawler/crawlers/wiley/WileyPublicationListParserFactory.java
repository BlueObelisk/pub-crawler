package wwmm.pubcrawler.crawlers.wiley;

import wwmm.pubcrawler.parsers.PublicationListParser;
import wwmm.pubcrawler.parsers.PublicationListParserFactory;
import wwmm.pubcrawler.crawlers.wiley.parsers.WileyPublicationListParser;
import wwmm.pubcrawler.http.DocumentResource;

import javax.inject.Singleton;

/**
 * @author Sam Adams
 */
@Singleton
public class WileyPublicationListParserFactory implements PublicationListParserFactory<DocumentResource> {

    @Override
    public PublicationListParser createPublicationListParser(final DocumentResource htmlDoc) {
        return new WileyPublicationListParser(htmlDoc.getDocument(), htmlDoc.getUrl());
    }

}
