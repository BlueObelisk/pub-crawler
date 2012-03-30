package wwmm.pubcrawler.crawlers.springer;

import nu.xom.Document;
import wwmm.pubcrawler.crawlers.IssueTocParser;
import wwmm.pubcrawler.crawlers.IssueTocParserFactory;
import wwmm.pubcrawler.crawlers.springer.parsers.SpringerIssueTocParser;
import wwmm.pubcrawler.model.id.JournalId;

import java.net.URI;

/**
 * @author Sam Adams
 */
public class SpringerIssueTocParserFactory implements IssueTocParserFactory {

    @Override
    public IssueTocParser createIssueTocParser(final Document html, final URI url, final JournalId journalId) {
        return new SpringerIssueTocParser(html, url, journalId);
    }

}
