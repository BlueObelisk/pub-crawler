package wwmm.pubcrawler.crawlers.rsc;

import nu.xom.Document;
import wwmm.pubcrawler.crawlers.IssueTocParser;
import wwmm.pubcrawler.crawlers.IssueTocParserFactory;
import wwmm.pubcrawler.crawlers.rsc.parsers.RscIssueTocParser;
import wwmm.pubcrawler.model.id.JournalId;

import javax.inject.Singleton;
import java.net.URI;

/**
 * @author Sam Adams
 */
@Singleton
public class RscIssueTocParserFactory implements IssueTocParserFactory {

    @Override
    public IssueTocParser createIssueTocParser(final Document html, final URI url, final JournalId journalId) {
        return new RscIssueTocParser(html, url, journalId);
    }

}
