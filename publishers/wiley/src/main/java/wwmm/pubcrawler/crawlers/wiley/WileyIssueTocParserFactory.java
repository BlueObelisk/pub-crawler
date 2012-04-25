package wwmm.pubcrawler.crawlers.wiley;

import wwmm.pubcrawler.parsers.IssueTocParser;
import wwmm.pubcrawler.parsers.IssueTocParserFactory;
import wwmm.pubcrawler.crawlers.wiley.parsers.WileyIssueTocParser;
import wwmm.pubcrawler.http.HtmlDocument;
import wwmm.pubcrawler.model.id.JournalId;

import javax.inject.Singleton;

/**
 * @author Sam Adams
 */
@Singleton
public class WileyIssueTocParserFactory implements IssueTocParserFactory<HtmlDocument> {

    @Override
    public IssueTocParser createIssueTocParser(final JournalId journalId, final HtmlDocument htmlDoc) {
        return new WileyIssueTocParser(htmlDoc.getDocument(), htmlDoc.getUrl(), journalId);
    }

}
