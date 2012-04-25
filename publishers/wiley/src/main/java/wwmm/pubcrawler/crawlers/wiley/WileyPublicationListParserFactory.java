package wwmm.pubcrawler.crawlers.wiley;

import wwmm.pubcrawler.parsers.PublicationListParser;
import wwmm.pubcrawler.parsers.PublicationListParserFactory;
import wwmm.pubcrawler.crawlers.wiley.parsers.WileyPublicationListParser;
import wwmm.pubcrawler.http.HtmlDocument;

import javax.inject.Singleton;

/**
 * @author Sam Adams
 */
@Singleton
public class WileyPublicationListParserFactory implements PublicationListParserFactory<HtmlDocument> {

    @Override
    public PublicationListParser createPublicationListParser(final HtmlDocument htmlDoc) {
        return new WileyPublicationListParser(htmlDoc.getDocument(), htmlDoc.getUrl());
    }

}
