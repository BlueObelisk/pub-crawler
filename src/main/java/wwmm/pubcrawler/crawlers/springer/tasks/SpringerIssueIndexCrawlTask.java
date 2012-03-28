package wwmm.pubcrawler.crawlers.springer.tasks;

import nu.xom.Document;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import wwmm.pubcrawler.controller.Fetcher;
import wwmm.pubcrawler.controller.JournalArchiver;
import wwmm.pubcrawler.controller.URITask;
import wwmm.pubcrawler.crawlers.BasicHttpCrawlTask;
import wwmm.pubcrawler.crawlers.JournalHandler;
import wwmm.pubcrawler.crawlers.springer.SpringerIssueIndexCrawlTaskFactory;
import wwmm.pubcrawler.crawlers.springer.parsers.SpringerPublicationListParser;
import wwmm.pubcrawler.model.Journal;
import wwmm.pubcrawler.utils.HtmlUtils;
import wwmm.pubcrawler.v2.crawler.CrawlTask;
import wwmm.pubcrawler.v2.crawler.TaskData;
import wwmm.pubcrawler.v2.crawler.TaskQueue;

import javax.inject.Inject;
import java.net.URI;

/**
 * @author Sam Adams
 */
public class SpringerIssueIndexCrawlTask extends BasicHttpCrawlTask {

    private final SpringerPublicationListParserFactory publicationListParserFactory;
    private final SpringerIssueIndexCrawlTaskFactory issueIndexCrawlTaskFactory;
    private final JournalArchiver journalArchiver;
    private final JournalHandler journalHandler;
    private final TaskQueue taskQueue;

    @Inject
    public SpringerIssueIndexCrawlTask(final Fetcher<URITask, CrawlerResponse> fetcher, final SpringerPublicationListParserFactory publicationListParserFactory, final JournalHandler journalHandler, final JournalArchiver journalArchiver, final SpringerIssueIndexCrawlTaskFactory issueIndexCrawlTaskFactory, final TaskQueue taskQueue) {
        super(fetcher);
        this.publicationListParserFactory = publicationListParserFactory;
        this.journalHandler = journalHandler;
        this.journalArchiver = journalArchiver;
        this.issueIndexCrawlTaskFactory = issueIndexCrawlTaskFactory;
        this.taskQueue = taskQueue;
    }

    @Override
    protected void handleResponse(final String id, final TaskData data, final CrawlerResponse response) throws Exception {
        Document html = HtmlUtils.readHtmlDocument(response);

        SpringerPublicationListParser parser = publicationListParserFactory.createPublicationListParser(html, URI.create(html.getBaseURI()));
        for (Journal journal : parser.findJournals()) {
            archiveJournal(journal);
            journalHandler.handleJournal(journal);
        }

        URI url = parser.getNextPage();
        if (url != null) {
            int page = Integer.parseInt(data.getString("page"));
            String key = data.getString("key");
            CrawlTask crawlTask = issueIndexCrawlTaskFactory.createIssueListCrawlTask(url, key, page+1);
            taskQueue.queueTask(crawlTask);
        }
    }

    private void archiveJournal(final Journal journal) {
        journalArchiver.archive(journal);
    }
    
}
