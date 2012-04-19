package wwmm.pubcrawler.crawlers.rsc;

import nu.xom.Document;
import wwmm.pubcrawler.crawlers.PublicationListParser;
import wwmm.pubcrawler.crawlers.PublicationListParserFactory;
import wwmm.pubcrawler.crawlers.rsc.parsers.RscPublicationListParser;

import javax.inject.Singleton;

/**
 * @author Sam Adams
 */
@Singleton
public class RscPublicationListParserFactory implements PublicationListParserFactory {

    @Override
    public PublicationListParser createPublicationListParser(final Document document) {
        return new RscPublicationListParser(document);
    }

}
