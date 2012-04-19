package wwmm.pubcrawler.crawlers.acta;

import nu.xom.Document;
import wwmm.pubcrawler.crawlers.IssueTocParser;
import wwmm.pubcrawler.crawlers.acta.parsers.IucrIssueTocParser;
import wwmm.pubcrawler.model.id.JournalId;

import javax.inject.Singleton;

/**
 * @author Sam Adams
 */
@Singleton
public class IucrIssueTocParserFactory {

    public IssueTocParser createIssueTocParser(final Document bodyHtml, final Document headHtml, final JournalId journalId) {
        return new IucrIssueTocParser(bodyHtml, headHtml, journalId);
    }

}
