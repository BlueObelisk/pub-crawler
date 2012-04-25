package wwmm.pubcrawler.crawlers.rsc;

import wwmm.pubcrawler.parsers.IssueTocParser;
import wwmm.pubcrawler.parsers.IssueTocParserFactory;
import wwmm.pubcrawler.crawlers.rsc.parsers.RscIssueTocParser;
import wwmm.pubcrawler.http.DocumentResource;
import wwmm.pubcrawler.model.id.JournalId;

import javax.inject.Singleton;

/**
 * @author Sam Adams
 */
@Singleton
public class RscIssueTocParserFactory implements IssueTocParserFactory<DocumentResource> {

    @Override
    public IssueTocParser createIssueTocParser(final JournalId journalId, final DocumentResource htmlDoc) {
        return new RscIssueTocParser(htmlDoc.getDocument(), htmlDoc.getUrl(), journalId);
    }

}
