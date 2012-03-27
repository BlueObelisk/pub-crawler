package wwmm.pubcrawler.crawlers;

import nu.xom.Document;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import wwmm.pubcrawler.controller.Fetcher;
import wwmm.pubcrawler.controller.JournalArchiver;
import wwmm.pubcrawler.controller.URITask;
import wwmm.pubcrawler.model.Journal;
import wwmm.pubcrawler.utils.HtmlUtils;
import wwmm.pubcrawler.v2.crawler.TaskData;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;

/**
 * @author Sam Adams
 */
public abstract class BasicPublicationListCrawlTask extends BasicHttpCrawlTask {

    private final PublicationListParserFactory parserFactory;
    private final JournalArchiver journalArchiver;
    private final JournalHandler journalHandler;

    @Inject
    public BasicPublicationListCrawlTask(final Fetcher<URITask, CrawlerResponse> fetcher, final PublicationListParserFactory parserFactory, final JournalArchiver journalArchiver, final JournalHandler journalHandler) {
        super(fetcher);
        this.parserFactory = parserFactory;
        this.journalArchiver = journalArchiver;
        this.journalHandler = journalHandler;
    }

    @Override
    protected void handleResponse(final String id, final TaskData data, final CrawlerResponse response) throws Exception {
        final Document html = readResponse(response);
        final PublicationListParser parser = parserFactory.createPublicationListParser(html);
        final List<Journal> journals = parser.findJournals();
        for (final Journal journal : journals) {
            archiveJournal(journal);
            journalHandler.handleJournal(journal);
        }
    }

    private void archiveJournal(final Journal journal) {
        journalArchiver.archive(journal);
    }

    protected Document readResponse(final CrawlerResponse response) throws IOException {
        return HtmlUtils.readHtmlDocument(response);
    }

}
