package wwmm.pubcrawler.crawlers.wiley;

import wwmm.pubcrawler.parsers.IssueTocParser;
import wwmm.pubcrawler.parsers.IssueTocParserFactory;
import wwmm.pubcrawler.crawlers.wiley.parsers.WileyIssueTocParser;
import wwmm.pubcrawler.http.DocumentResource;
import wwmm.pubcrawler.model.id.JournalId;

import javax.inject.Singleton;

/**
 * @author Sam Adams
 */
@Singleton
public class WileyIssueTocParserFactory implements IssueTocParserFactory<DocumentResource> {

    @Override
    public IssueTocParser createIssueTocParser(final JournalId journalId, final DocumentResource htmlDoc) {
        return new WileyIssueTocParser(htmlDoc.getDocument(), htmlDoc.getUrl(), journalId);
    }

}
