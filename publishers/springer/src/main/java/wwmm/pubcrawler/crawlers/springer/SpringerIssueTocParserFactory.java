package wwmm.pubcrawler.crawlers.springer;

import wwmm.pubcrawler.parsers.IssueTocParser;
import wwmm.pubcrawler.parsers.IssueTocParserFactory;
import wwmm.pubcrawler.crawlers.springer.parsers.SpringerIssueTocParser;
import wwmm.pubcrawler.http.HtmlDocument;
import wwmm.pubcrawler.model.id.JournalId;

/**
 * @author Sam Adams
 */
public class SpringerIssueTocParserFactory implements IssueTocParserFactory<HtmlDocument> {

    @Override
    public IssueTocParser createIssueTocParser(final JournalId journalId, final HtmlDocument htmlDoc) {
        return new SpringerIssueTocParser(htmlDoc.getDocument(), htmlDoc.getUrl(), journalId);
    }

}
