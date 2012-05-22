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

    private final TaskQueue taskQueue;

    @Inject
    public IucrJournalHandler(final TaskQueue taskQueue) {
        this.taskQueue = taskQueue;
    }

    @Override
    public void handleJournal(final Journal journal) {
        Task task = createTask(journal);
        taskQueue.queueTask(task);
    }

    private Task createTask(final Journal journal) {
        final URI url = URI.create("http://journals.iucr.org/" + journal.getAbbreviation() +"/contents/backissuesbdy.html");
        final IssueListCrawlTaskData data = new IssueListCrawlTaskData(url, "issuelist.html", ISSUE_LIST_CACHE_MAX_AGE, "iucr", journal.getAbbreviation());

        return TaskBuilder.newTask(IucrIssueListCrawlerTask.INSTANCE)
                .withId("iucr:issue-list:" + journal.getAbbreviation())
                .withInterval(ISSUE_LIST_CRAWL_INTERVAL)
                .withData(data)
                .build();
    }

}
