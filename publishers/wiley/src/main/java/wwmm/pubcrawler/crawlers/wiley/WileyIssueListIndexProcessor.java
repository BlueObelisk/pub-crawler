package wwmm.pubcrawler.crawlers.wiley;

import wwmm.pubcrawler.crawler.Task;
import wwmm.pubcrawler.crawler.TaskQueue;
import wwmm.pubcrawler.crawlers.ResourceProcessor;
import wwmm.pubcrawler.crawlers.wiley.parsers.WileyIssueListIndexParser;
import wwmm.pubcrawler.crawlers.wiley.tasks.WileyIssueListCrawlTask;
import wwmm.pubcrawler.http.DocumentResource;
import wwmm.pubcrawler.model.id.JournalId;
import wwmm.pubcrawler.model.id.PublisherId;
import wwmm.pubcrawler.tasks.IssueListCrawlTaskData;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author Sam Adams
 */
@Singleton
public class WileyIssueListIndexProcessor implements ResourceProcessor<DocumentResource, IssueListCrawlTaskData> {

    private final WileyIssueListIndexParserFactory parserFactory;
    private final TaskQueue taskQueue;
    private final WileyIssueListCrawlTaskFactory issueListCrawlTaskFactory;

    @Inject
    public WileyIssueListIndexProcessor(final WileyIssueListIndexParserFactory parserFactory, final TaskQueue taskQueue, final WileyIssueListCrawlTaskFactory issueListCrawlTaskFactory) {
        this.parserFactory = parserFactory;
        this.taskQueue = taskQueue;
        this.issueListCrawlTaskFactory = issueListCrawlTaskFactory;
    }

    @Override
    public void process(final String taskId, final IssueListCrawlTaskData data, final DocumentResource resource) {
        final JournalId journalId = data.getJournalId();
        final WileyIssueListIndexParser parser = parserFactory.createIssueListIndexParser(journalId, resource);
        for (final String year : parser.getIssueYears()) {
            final Task task = issueListCrawlTaskFactory.createIssueListCrawlTask(journalId.getJournalPart(), year);
            taskQueue.queueTask(task);
        }
    }
}
