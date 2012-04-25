package wwmm.pubcrawler.crawlers.acta;

import wwmm.pubcrawler.crawlers.acta.parsers.IucrIssueListParser;
import wwmm.pubcrawler.http.DocumentResource;
import wwmm.pubcrawler.model.id.JournalId;
import wwmm.pubcrawler.parsers.IssueListParser;
import wwmm.pubcrawler.parsers.IssueListParserFactory;

import javax.inject.Singleton;

/**
 * @author Sam Adams
 */
@Singleton
public class IucrIssueListParserFactory implements IssueListParserFactory<DocumentResource> {

    @Override
    public IssueListParser createIssueListParser(final JournalId journalId, final DocumentResource resource) {
        return new IucrIssueListParser(resource.getDocument(), resource.getUrl(), journalId);
    }

}
