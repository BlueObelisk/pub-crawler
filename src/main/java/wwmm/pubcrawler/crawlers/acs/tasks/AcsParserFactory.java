package wwmm.pubcrawler.crawlers.acs.tasks;

import nu.xom.Document;
import wwmm.pubcrawler.crawlers.acs.parsers.AcsArticleSplashPageParser;
import wwmm.pubcrawler.crawlers.acs.parsers.AcsIssueTocParser;
import wwmm.pubcrawler.crawlers.acs.parsers.AcsPublicationListParser;
import wwmm.pubcrawler.model.id.PublisherId;

import java.net.URI;

/**
 * @author Sam Adams
 */
public interface AcsParserFactory {

    AcsPublicationListParser createPublicationListParser(PublisherId acs, Document html, URI uri);

    AcsIssueTocParser createIssueTocParser(Document html, URI uri);
    
    AcsArticleSplashPageParser createArticleSplashPageParser();

}
