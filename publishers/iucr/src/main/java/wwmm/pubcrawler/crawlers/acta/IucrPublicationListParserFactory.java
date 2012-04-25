package wwmm.pubcrawler.crawlers.acta;

import wwmm.pubcrawler.parsers.PublicationListParser;
import wwmm.pubcrawler.parsers.PublicationListParserFactory;
import wwmm.pubcrawler.crawlers.acta.parsers.IucrPublicationListParser;
import wwmm.pubcrawler.http.DocumentResource;

import javax.inject.Singleton;

/**
 * @author Sam Adams
 */
@Singleton
public class IucrPublicationListParserFactory implements PublicationListParserFactory<DocumentResource> {

    @Override
    public PublicationListParser createPublicationListParser(final DocumentResource htmlDoc) {
        return new IucrPublicationListParser(htmlDoc.getDocument(), htmlDoc.getUrl());
    }

}
