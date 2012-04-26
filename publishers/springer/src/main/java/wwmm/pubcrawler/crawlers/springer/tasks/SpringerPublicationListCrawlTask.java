package wwmm.pubcrawler.crawlers.springer.tasks;

import nu.xom.Document;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import wwmm.pubcrawler.archivers.JournalArchiver;
import wwmm.pubcrawler.http.Fetcher;
import wwmm.pubcrawler.http.URITask;
import wwmm.pubcrawler.crawlers.BasicHttpCrawlTask;
import wwmm.pubcrawler.crawlers.JournalHandler;
import wwmm.pubcrawler.crawlers.springer.SpringerPublicationListCrawlTaskFactory;
import wwmm.pubcrawler.crawlers.springer.SpringerPublicationListParserFactory;
import wwmm.pubcrawler.crawlers.springer.parsers.SpringerPublicationListParser;
import wwmm.pubcrawler.http.DocumentResource;
import wwmm.pubcrawler.model.Journal;
import wwmm.pubcrawler.utils.HtmlUtils;
import wwmm.pubcrawler.crawler.CrawlTask;
import wwmm.pubcrawler.crawler.TaskData;
import wwmm.pubcrawler.crawler.TaskQueue;

import javax.inject.Inject;
import java.net.URI;

/**
 * @author Sam Adams
 */
public class SpringerPublicationListCrawlTask extends BasicHttpCrawlTask {

    private final SpringerPublicationListParserFactory publicationListParserFactory;
    private final SpringerPublicationListCrawlTaskFactory issueIndexCrawlTaskFactory;
    private final JournalArchiver journalArchiver;
    private final JournalHandler journalHandler;
    private final TaskQueue taskQueue;

    @Inject
    public SpringerPublicationListCrawlTask(final Fetcher<URITask, CrawlerResponse> fetcher, final SpringerPublicationListParserFactory publicationListParserFactory, final JournalHandler journalHandler, final JournalArchiver journalArchiver, final SpringerPublicationListCrawlTaskFactory issueIndexCrawlTaskFactory, final TaskQueue taskQueue) {
        super(fetcher);
        this.publicationListParserFactory = publicationListParserFactory;
        this.journalHandler = journalHandler;
        this.journalArchiver = journalArchiver;
        this.issueIndexCrawlTaskFactory = issueIndexCrawlTaskFactory;
        this.taskQueue = taskQueue;
    }

    @Override
    protected void handleResponse(final String id, final TaskData data, final CrawlerResponse response) throws Exception {
        final Document html = HtmlUtils.readHtmlDocument(response);
        final URI url = URI.create(html.getBaseURI());

        final SpringerPublicationListParser parser = publicationListParserFactory.createPublicationListParser(new DocumentResource(url, html));
        for (final Journal journal : parser.findJournals()) {
            archiveJournal(journal);
            journalHandler.handleJournal(journal);
        }

        final URI nextPage = parser.getNextPage();
        if (nextPage != null) {
            final int page = Integer.parseInt(data.getString("page"));
            final String key = data.getString("key");
            final CrawlTask crawlTask = issueIndexCrawlTaskFactory.createIssueListCrawlTask(nextPage, key, page+1);
            taskQueue.queueTask(crawlTask);
        }
    }

    private void archiveJournal(final Journal journal) {
        journalArchiver.archive(journal);
    }
    
}
