package wwmm.pubcrawler.crawlers;

import nu.xom.Document;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import wwmm.pubcrawler.controller.Fetcher;
import wwmm.pubcrawler.controller.JournalArchiver;
import wwmm.pubcrawler.controller.URITask;
import wwmm.pubcrawler.model.Journal;
import wwmm.pubcrawler.utils.HtmlUtils;
import wwmm.pubcrawler.v2.crawler.CrawlTask;
import wwmm.pubcrawler.v2.crawler.TaskData;
import wwmm.pubcrawler.v2.crawler.TaskQueue;

import javax.inject.Inject;
import java.util.List;

/**
 * @author Sam Adams
 */
public abstract class BasicPublicationListCrawlTask extends BasicHttpCrawlTask {

    private final TaskQueue taskQueue;
    private final PublicationListParserFactory parserFactory;
    private final JournalArchiver journalArchiver;

    @Inject
    public BasicPublicationListCrawlTask(final Fetcher<URITask, CrawlerResponse> fetcher, final TaskQueue taskQueue, final PublicationListParserFactory parserFactory, final JournalArchiver journalArchiver) {
        super(fetcher);
        this.taskQueue = taskQueue;
        this.parserFactory = parserFactory;
        this.journalArchiver = journalArchiver;
    }

    @Override
    protected void handleResponse(final String id, final TaskData data, final CrawlerResponse response) throws Exception {
        final Document html = HtmlUtils.readDocument(response);
        final PublicationListParser parser = parserFactory.createPublicationListParser(html);
        final List<Journal> journals = parser.findJournals();
        for (Journal journal : journals) {
            journalArchiver.archive(journal);

            final CrawlTask task = createCurrentIssueTocTask(journal);
            taskQueue.queueTask(task);
        }
    }

    protected abstract CrawlTask createCurrentIssueTocTask(final Journal journal);

}
