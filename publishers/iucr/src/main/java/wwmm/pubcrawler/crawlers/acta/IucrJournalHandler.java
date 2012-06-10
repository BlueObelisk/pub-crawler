package wwmm.pubcrawler.crawlers.acta;

import wwmm.pubcrawler.crawler.Task;
import wwmm.pubcrawler.crawler.TaskBuilder;
import wwmm.pubcrawler.crawler.TaskQueue;
import wwmm.pubcrawler.crawlers.JournalHandler;
import wwmm.pubcrawler.crawlers.acta.tasks.IucrIssueListCrawlerTask;
import wwmm.pubcrawler.model.Journal;
import wwmm.pubcrawler.tasks.IssueListCrawlTaskData;

import javax.inject.Inject;
import java.net.URI;

import static wwmm.pubcrawler.Config.ISSUE_LIST_CACHE_MAX_AGE;
import static wwmm.pubcrawler.Config.ISSUE_LIST_CRAWL_INTERVAL;

/**
 * @author Sam Adams
 */
public class IucrJournalHandler implements JournalHandler {

    private static final String PUBLISHER = "iucr";

    private final TaskQueue taskQueue;

    @Inject
    public IucrJournalHandler(final TaskQueue taskQueue) {
        this.taskQueue = taskQueue;
    }

    @Override
    public void handleJournal(final Journal journal) {
        Task task = createIssueListCrawlTask(journal);
        taskQueue.queueTask(task);
    }

    private Task createIssueListCrawlTask(final Journal journal) {
        final URI url = URI.create("http://journals.iucr.org/" + journal.getAbbreviation() +"/contents/backissuesbdy.html");
        final IssueListCrawlTaskData data = new IssueListCrawlTaskData(url, "issuelist.html", ISSUE_LIST_CACHE_MAX_AGE, PUBLISHER, journal.getAbbreviation());

        return TaskBuilder.newTask(IucrIssueListCrawlerTask.INSTANCE)
                .withId("iucr:issue-list:" + journal.getAbbreviation())
                .withInterval(ISSUE_LIST_CRAWL_INTERVAL)
                .withData(data)
                .build();
    }

}
