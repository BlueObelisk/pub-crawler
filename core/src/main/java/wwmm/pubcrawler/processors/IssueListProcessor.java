package wwmm.pubcrawler.processors;

import wwmm.pubcrawler.crawlers.IssueHandler;
import wwmm.pubcrawler.model.Issue;
import wwmm.pubcrawler.model.id.JournalId;
import wwmm.pubcrawler.parsers.IssueListParser;
import wwmm.pubcrawler.parsers.IssueListParserFactory;

/**
 * @author Sam Adams
 */
public class IssueListProcessor<T> {

    private final IssueListParserFactory<T> parserFactory;
    private final IssueHandler issueHandler;

    public IssueListProcessor(final IssueListParserFactory<T> parserFactory, final IssueHandler issueHandler) {
        this.parserFactory = parserFactory;
        this.issueHandler = issueHandler;
    }
    
    public void processIssueList(final JournalId journalId, T resource) {
        final IssueListParser parser = parserFactory.createIssueListParser(journalId, resource);

        for (final Issue issue : parser.findIssues()) {
            issueHandler.handleIssue(issue);
        }
    }
}