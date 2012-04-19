package wwmm.pubcrawler.crawlers.acs;

import nu.xom.Document;
import wwmm.pubcrawler.crawlers.IssueTocParser;
import wwmm.pubcrawler.crawlers.IssueTocParserFactory;
import wwmm.pubcrawler.crawlers.acs.parsers.AcsIssueTocParser;
import wwmm.pubcrawler.model.id.JournalId;

import javax.inject.Singleton;
import java.net.URI;

/**
 * @author Sam Adams
 */
@Singleton
public class AcsIssueTocParserFactory implements IssueTocParserFactory {
    
    @Override
    public IssueTocParser createIssueTocParser(final Document html, final URI url, final JournalId journalId) {
        return new AcsIssueTocParser(html, url, journalId);
    }
    
}
