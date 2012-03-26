package wwmm.pubcrawler.crawlers.elsevier;

import nu.xom.Document;
import wwmm.pubcrawler.crawlers.PublicationListParser;
import wwmm.pubcrawler.crawlers.PublicationListParserFactory;
import wwmm.pubcrawler.crawlers.elsevier.parsers.ElsevierPublicationListParser;

/**
 * @author Sam Adams
 */
public class ElsevierPublicationListParserFactory implements PublicationListParserFactory {

    @Override
    public PublicationListParser createPublicationListParser(final Document document) {
        return new ElsevierPublicationListParser(document);
    }

}
