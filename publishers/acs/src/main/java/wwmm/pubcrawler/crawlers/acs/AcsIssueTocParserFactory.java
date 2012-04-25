package wwmm.pubcrawler.crawlers.acs;

import wwmm.pubcrawler.parsers.IssueTocParser;
import wwmm.pubcrawler.parsers.IssueTocParserFactory;
import wwmm.pubcrawler.crawlers.acs.parsers.AcsIssueTocParser;
import wwmm.pubcrawler.http.DocumentResource;
import wwmm.pubcrawler.model.id.JournalId;

import javax.inject.Singleton;

/**
 * @author Sam Adams
 */
@Singleton
public class AcsIssueTocParserFactory implements IssueTocParserFactory<DocumentResource> {

    @Override
    public IssueTocParser createIssueTocParser(final JournalId journalId, final DocumentResource htmlDoc) {
        return new AcsIssueTocParser(htmlDoc.getDocument(), htmlDoc.getUrl(), journalId);
    }
    
}
