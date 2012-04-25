package wwmm.pubcrawler.crawlers.rsc;

import wwmm.pubcrawler.parsers.PublicationListParser;
import wwmm.pubcrawler.parsers.PublicationListParserFactory;
import wwmm.pubcrawler.crawlers.rsc.parsers.RscPublicationListParser;
import wwmm.pubcrawler.http.HtmlDocument;

import javax.inject.Singleton;

/**
 * @author Sam Adams
 */
@Singleton
public class RscPublicationListParserFactory implements PublicationListParserFactory<HtmlDocument> {

    @Override
    public PublicationListParser createPublicationListParser(final HtmlDocument htmlDoc) {
        return new RscPublicationListParser(htmlDoc.getDocument());
    }

}
