package wwmm.pubcrawler.processors;

import wwmm.pubcrawler.crawlers.IssueHandler;
import wwmm.pubcrawler.crawlers.ResourceProcessor;
import wwmm.pubcrawler.model.IssueLink;
import wwmm.pubcrawler.model.id.JournalId;
import wwmm.pubcrawler.model.id.PublisherId;
import wwmm.pubcrawler.parsers.IssueListParser;
import wwmm.pubcrawler.parsers.IssueListParserFactory;
import wwmm.pubcrawler.tasks.IssueListCrawlTaskData;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author Sam Adams
 */
@Singleton
public class IssueListProcessor<T> implements ResourceProcessor<T, IssueListCrawlTaskData> {

    private final IssueListParserFactory<T> parserFactory;
    private final IssueHandler issueHandler;

    @Inject
    public IssueListProcessor(final IssueListParserFactory<T> parserFactory, final IssueHandler issueHandler) {
        this.parserFactory = parserFactory;
        this.issueHandler = issueHandler;
    }

    @Override
    public void process(final String taskId, final IssueListCrawlTaskData data, final T resource) {
        final PublisherId publisherId = new PublisherId(data.getPublisher());
        final JournalId journalId = new JournalId(publisherId, data.getJournal());

        final IssueListParser parser = parserFactory.createIssueListParser(journalId, resource);

        for (final IssueLink issueLink : parser.findIssues()) {
            issueHandler.handleIssueLink(issueLink);
        }
    }
}
