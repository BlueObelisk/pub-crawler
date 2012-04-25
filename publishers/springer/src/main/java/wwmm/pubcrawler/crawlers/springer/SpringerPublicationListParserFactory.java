package wwmm.pubcrawler.crawlers.springer;

import wwmm.pubcrawler.parsers.PublicationListParserFactory;
import wwmm.pubcrawler.crawlers.springer.parsers.SpringerPublicationListParser;
import wwmm.pubcrawler.http.HtmlDocument;

import javax.inject.Singleton;

/**
 * @author Sam Adams
 */
@Singleton
public class SpringerPublicationListParserFactory implements PublicationListParserFactory<HtmlDocument> {

    @Override
    public SpringerPublicationListParser createPublicationListParser(final HtmlDocument htmlDoc) {
        return new SpringerPublicationListParser(htmlDoc.getDocument(), htmlDoc.getUrl());
    }

}
