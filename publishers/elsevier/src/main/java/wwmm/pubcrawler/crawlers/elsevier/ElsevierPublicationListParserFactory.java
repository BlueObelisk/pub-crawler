package wwmm.pubcrawler.crawlers.elsevier;

import wwmm.pubcrawler.parsers.PublicationListParser;
import wwmm.pubcrawler.parsers.PublicationListParserFactory;
import wwmm.pubcrawler.crawlers.elsevier.parsers.ElsevierPublicationListParser;
import wwmm.pubcrawler.http.HtmlDocument;

import javax.inject.Singleton;

/**
 * @author Sam Adams
 */
@Singleton
public class ElsevierPublicationListParserFactory implements PublicationListParserFactory<HtmlDocument> {

    @Override
    public PublicationListParser createPublicationListParser(final HtmlDocument htmlDoc) {
        return new ElsevierPublicationListParser(htmlDoc.getDocument());
    }

}
