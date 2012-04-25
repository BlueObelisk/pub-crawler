package wwmm.pubcrawler.crawlers.acs;

import wwmm.pubcrawler.parsers.IssueTocParser;
import wwmm.pubcrawler.parsers.IssueTocParserFactory;
import wwmm.pubcrawler.crawlers.acs.parsers.AcsIssueTocParser;
import wwmm.pubcrawler.http.HtmlDocument;
import wwmm.pubcrawler.model.id.JournalId;

import javax.inject.Singleton;

/**
 * @author Sam Adams
 */
@Singleton
public class AcsIssueTocParserFactory implements IssueTocParserFactory<HtmlDocument> {

    @Override
    public IssueTocParser createIssueTocParser(final JournalId journalId, final HtmlDocument htmlDoc) {
        return new AcsIssueTocParser(htmlDoc.getDocument(), htmlDoc.getUrl(), journalId);
    }
    
}
