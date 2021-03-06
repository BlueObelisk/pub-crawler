package wwmm.pubcrawler.crawlers.springer;

import nu.xom.Document;
import wwmm.pubcrawler.crawlers.springer.parsers.SpringerIssueListParser;
import wwmm.pubcrawler.http.DocumentResource;
import wwmm.pubcrawler.model.id.JournalId;
import wwmm.pubcrawler.parsers.IssueListParser;
import wwmm.pubcrawler.parsers.IssueListParserFactory;

import javax.inject.Singleton;
import java.net.URI;

/**
 * @author Sam Adams
 */
@Singleton
public class SpringerIssueListParserFactory implements IssueListParserFactory<DocumentResource> {

    @Override
    public IssueListParser createIssueListParser(final JournalId journalId, final DocumentResource resource) {
        return new SpringerIssueListParser(journalId, resource.getDocument(), resource.getUrl());
    }

}
