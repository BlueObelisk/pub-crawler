package wwmm.pubcrawler.crawlers.wiley;

import wwmm.pubcrawler.crawlers.wiley.parsers.WileyIssueListIndexParser;
import wwmm.pubcrawler.http.DocumentResource;
import wwmm.pubcrawler.model.id.JournalId;

import javax.inject.Singleton;

/**
 * @author Sam Adams
 */
@Singleton
public class WileyIssueListIndexParserFactory {

    public WileyIssueListIndexParser createIssueListIndexParser(final JournalId journalId, final DocumentResource htmlDoc) {
        return new WileyIssueListIndexParser(htmlDoc.getDocument(), htmlDoc.getUrl());
    }

}
