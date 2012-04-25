package wwmm.pubcrawler.crawlers.acta;

import wwmm.pubcrawler.parsers.PublicationListParser;
import wwmm.pubcrawler.parsers.PublicationListParserFactory;
import wwmm.pubcrawler.crawlers.acta.parsers.IucrPublicationListParser;
import wwmm.pubcrawler.http.HtmlDocument;

import javax.inject.Singleton;

/**
 * @author Sam Adams
 */
@Singleton
public class IucrPublicationListParserFactory implements PublicationListParserFactory<HtmlDocument> {

    @Override
    public PublicationListParser createPublicationListParser(final HtmlDocument htmlDoc) {
        return new IucrPublicationListParser(htmlDoc.getDocument(), htmlDoc.getUrl());
    }

}
