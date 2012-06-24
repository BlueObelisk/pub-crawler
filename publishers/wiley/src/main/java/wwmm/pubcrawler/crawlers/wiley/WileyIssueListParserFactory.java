package wwmm.pubcrawler.crawlers.wiley;

import wwmm.pubcrawler.crawlers.wiley.parsers.WileyIssueListParser;
import wwmm.pubcrawler.crawlers.wiley.parsers.WileyIssueTocParser;
import wwmm.pubcrawler.http.DocumentResource;
import wwmm.pubcrawler.model.id.JournalId;
import wwmm.pubcrawler.parsers.IssueListParser;
import wwmm.pubcrawler.parsers.IssueListParserFactory;
import wwmm.pubcrawler.parsers.IssueTocParser;
import wwmm.pubcrawler.parsers.IssueTocParserFactory;

import javax.inject.Singleton;

/**
 * @author Sam Adams
 */
@Singleton
public class WileyIssueListParserFactory implements IssueListParserFactory<DocumentResource> {

    @Override
    public IssueListParser createIssueListParser(final JournalId journalId, final DocumentResource resource) {
        return new WileyIssueListParser(resource.getDocument(), resource.getUrl(), journalId);
    }

}
