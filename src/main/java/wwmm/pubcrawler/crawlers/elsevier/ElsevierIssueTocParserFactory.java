package wwmm.pubcrawler.crawlers.elsevier;

import nu.xom.Document;
import wwmm.pubcrawler.crawlers.IssueTocParser;
import wwmm.pubcrawler.crawlers.IssueTocParserFactory;
import wwmm.pubcrawler.crawlers.elsevier.parsers.ElsevierIssueTocParser;
import wwmm.pubcrawler.model.id.JournalId;

import javax.inject.Singleton;
import java.net.URI;

/**
 * @author Sam Adams
 */
@Singleton
public class ElsevierIssueTocParserFactory implements IssueTocParserFactory {

    @Override
    public IssueTocParser createIssueTocParser(final Document html, final URI url, final JournalId journalId) {
        return new ElsevierIssueTocParser(html, url, journalId);
    }

}
