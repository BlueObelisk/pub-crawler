package wwmm.pubcrawler.crawlers.springer;

import wwmm.pubcrawler.parsers.PublicationListParserFactory;
import wwmm.pubcrawler.crawlers.springer.parsers.SpringerPublicationListParser;
import wwmm.pubcrawler.http.DocumentResource;

import javax.inject.Singleton;

/**
 * @author Sam Adams
 */
@Singleton
public class SpringerPublicationListParserFactory implements PublicationListParserFactory<DocumentResource> {

    @Override
    public SpringerPublicationListParser createPublicationListParser(final DocumentResource htmlDoc) {
        return new SpringerPublicationListParser(htmlDoc.getDocument(), htmlDoc.getUrl());
    }

}
