package wwmm.pubcrawler.crawlers.wiley;

import wwmm.pubcrawler.crawler.Task;
import wwmm.pubcrawler.crawler.TaskBuilder;
import wwmm.pubcrawler.crawler.TaskQueue;
import wwmm.pubcrawler.crawlers.JournalHandler;
import wwmm.pubcrawler.crawlers.wiley.tasks.WileyIssueListIndexCrawlTask;
import wwmm.pubcrawler.model.Journal;
import wwmm.pubcrawler.tasks.IssueListCrawlTaskData;

import javax.inject.Inject;
import java.net.URI;

import static wwmm.pubcrawler.Config.ISSUE_LIST_CACHE_MAX_AGE;
import static wwmm.pubcrawler.Config.ISSUE_LIST_CRAWL_INTERVAL;

/**
 * @author Sam Adams
 */
public class WileyJournalHandler implements JournalHandler {

    private static final String PUBLISHER = "wiley";

    private final TaskQueue taskQueue;

    @Inject
    public WileyJournalHandler(final TaskQueue taskQueue) {
        this.taskQueue = taskQueue;
    }

    @Override
    public void handleJournal(final Journal journal) {
        Task task = createIssueListCrawlTask(journal);
        taskQueue.queueTask(task);
    }

    private Task createIssueListCrawlTask(final Journal journal) {
        final String journalId = journal.getId().getJournalPart();
        final URI issuesUrl = URI.create("http://onlinelibrary.wiley.com/journal/" + journalId + "/issues");
        final IssueListCrawlTaskData data = new IssueListCrawlTaskData(issuesUrl, "issues.html", ISSUE_LIST_CACHE_MAX_AGE, PUBLISHER, journal.getAbbreviation());

        return TaskBuilder.newTask(WileyIssueListIndexCrawlTask.INSTANCE)
                .withId("wiley:issue-list-index:" + journal.getAbbreviation())
                .withInterval(ISSUE_LIST_CRAWL_INTERVAL)
                .withData(data)
                .build();
    }
}
