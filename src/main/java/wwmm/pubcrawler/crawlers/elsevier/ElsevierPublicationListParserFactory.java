package wwmm.pubcrawler.crawlers.elsevier;

import nu.xom.Document;
import wwmm.pubcrawler.crawlers.PublicationListParser;
import wwmm.pubcrawler.crawlers.PublicationListParserFactory;
import wwmm.pubcrawler.crawlers.elsevier.parsers.ElsevierPublicationListParser;

import javax.inject.Singleton;

/**
 * @author Sam Adams
 */
@Singleton
public class ElsevierPublicationListParserFactory implements PublicationListParserFactory {

    @Override
    public PublicationListParser createPublicationListParser(final Document document) {
        return new ElsevierPublicationListParser(document);
    }

}
