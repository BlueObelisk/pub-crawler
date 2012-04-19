package wwmm.pubcrawler.crawlers;

import wwmm.pubcrawler.model.Journal;
import wwmm.pubcrawler.crawler.CrawlTask;
import wwmm.pubcrawler.crawler.TaskQueue;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author Sam Adams
 */
@Singleton
public class EnqueuingJournalHandler implements JournalHandler {

    private final TaskQueue taskQueue;
    private final IssueTocCrawlTaskFactory taskFactory;

    @Inject
    public EnqueuingJournalHandler(final TaskQueue taskQueue, final IssueTocCrawlTaskFactory taskFactory) {
        this.taskQueue = taskQueue;
        this.taskFactory = taskFactory;
    }

    @Override
    public void handleJournal(final Journal journal) {
        final CrawlTask task = taskFactory.createCurrentIssueTocCrawlTask(journal.getId(), journal.getUrl());
        if (task != null) {
            taskQueue.queueTask(task);
        }
    }

}