package wwmm.pubcrawler.crawlers.acta;

import org.joda.time.Duration;
import wwmm.pubcrawler.crawler.CrawlTask;
import wwmm.pubcrawler.crawler.CrawlTaskBuilder;
import wwmm.pubcrawler.crawler.TaskQueue;
import wwmm.pubcrawler.crawlers.JournalHandler;
import wwmm.pubcrawler.crawlers.acta.tasks.IucrIssueListCrawlerTask;
import wwmm.pubcrawler.model.Journal;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

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
        CrawlTask task = createTask(journal);
        taskQueue.queueTask(task);
    }

    private CrawlTask createTask(final Journal journal) {
        final Map<String, String> map = new HashMap<String, String>();
        map.put("url", "http://journals.iucr.org/" + journal.getAbbreviation() +"/contents/backissuesbdy.html");
        map.put("fileId", "issuelist.html");
        map.put("publisher", "iucr");
        map.put("journal", journal.getAbbreviation());

        return new CrawlTaskBuilder()
            .ofType(IucrIssueListCrawlerTask.class)
            .withInterval(Duration.standardDays(1))
            .withId("iucr:issue-list:" + journal.getAbbreviation())
            .withData(map)
            .build();
    }

}
