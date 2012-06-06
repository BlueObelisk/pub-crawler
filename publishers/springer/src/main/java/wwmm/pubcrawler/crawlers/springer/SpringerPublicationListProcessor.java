package wwmm.pubcrawler.crawlers.springer;

import wwmm.pubcrawler.archivers.JournalArchiver;
import wwmm.pubcrawler.crawler.Task;
import wwmm.pubcrawler.crawler.TaskQueue;
import wwmm.pubcrawler.crawlers.JournalHandler;
import wwmm.pubcrawler.crawlers.ResourceProcessor;
import wwmm.pubcrawler.crawlers.springer.SpringerPublicationListCrawlTaskData;
import wwmm.pubcrawler.crawlers.springer.SpringerPublicationListCrawlTaskFactory;
import wwmm.pubcrawler.crawlers.springer.SpringerPublicationListParserFactory;
import wwmm.pubcrawler.crawlers.springer.parsers.SpringerPublicationListParser;
import wwmm.pubcrawler.http.DocumentResource;
import wwmm.pubcrawler.model.Journal;

import javax.inject.Inject;
import java.net.URI;

/**
 * @author Sam Adams
 */
public class SpringerPublicationListProcessor implements ResourceProcessor<DocumentResource,SpringerPublicationListCrawlTaskData> {

    private final SpringerPublicationListParserFactory publicationListParserFactory;
    private final SpringerPublicationListCrawlTaskFactory issueIndexCrawlTaskFactory;
    private final JournalArchiver journalArchiver;
    private final JournalHandler journalHandler;
    private final TaskQueue taskQueue;

    @Inject
    public SpringerPublicationListProcessor(final SpringerPublicationListParserFactory publicationListParserFactory, final SpringerPublicationListCrawlTaskFactory issueIndexCrawlTaskFactory, final JournalArchiver journalArchiver, final JournalHandler journalHandler, final TaskQueue taskQueue) {
        this.publicationListParserFactory = publicationListParserFactory;
        this.issueIndexCrawlTaskFactory = issueIndexCrawlTaskFactory;
        this.journalArchiver = journalArchiver;
        this.journalHandler = journalHandler;
        this.taskQueue = taskQueue;
    }

    @Override
    public void process(final String taskId, final SpringerPublicationListCrawlTaskData data, final DocumentResource documentResource) {

        final SpringerPublicationListParser parser = publicationListParserFactory.createPublicationListParser(documentResource);
        for (final Journal journal : parser.findJournals()) {
            journalArchiver.archive(journal);
            journalHandler.handleJournal(journal);
        }

        final URI nextPage = parser.getNextPage();
        if (nextPage != null) {
            final String key = data.getKey();
            final int page = data.getPage();
            final Task crawlTask = issueIndexCrawlTaskFactory.createIssueListCrawlTask(nextPage, key, page + 1);
            taskQueue.queueTask(crawlTask);
        }
    }
}
