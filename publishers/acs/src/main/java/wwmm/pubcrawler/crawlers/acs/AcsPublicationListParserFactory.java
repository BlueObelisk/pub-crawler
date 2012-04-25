package wwmm.pubcrawler.crawlers.acs;

import wwmm.pubcrawler.parsers.PublicationListParser;
import wwmm.pubcrawler.parsers.PublicationListParserFactory;
import wwmm.pubcrawler.crawlers.acs.parsers.AcsPublicationListParser;
import wwmm.pubcrawler.http.HtmlDocument;

import javax.inject.Singleton;

/**
 * @author Sam Adams
 */
@Singleton
public class AcsPublicationListParserFactory implements PublicationListParserFactory<HtmlDocument> {
    
    @Override
    public PublicationListParser createPublicationListParser(final HtmlDocument htmlDocument) {
        return new AcsPublicationListParser(htmlDocument.getDocument());
    }
    
}
