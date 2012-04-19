package wwmm.pubcrawler.crawlers.acta.tasks;

import nu.xom.Document;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import wwmm.pubcrawler.archivers.JournalArchiver;
import wwmm.pubcrawler.controller.Fetcher;
import wwmm.pubcrawler.controller.URITask;
import wwmm.pubcrawler.crawlers.BasicPublicationListCrawlTask;
import wwmm.pubcrawler.crawlers.acta.IucrJournalHandler;
import wwmm.pubcrawler.crawlers.acta.IucrPublicationListParserFactory;
import wwmm.pubcrawler.utils.HtmlUtils;

import javax.inject.Inject;
import java.io.IOException;

/**
 * @author Sam Adams
 */
public class IucrPublicationListCrawlTask extends BasicPublicationListCrawlTask {

    @Inject
    public IucrPublicationListCrawlTask(final Fetcher<URITask, CrawlerResponse> fetcher, final IucrPublicationListParserFactory parserFactory, final JournalArchiver journalArchiver, final IucrJournalHandler journalHandler) {
        super(fetcher, parserFactory, journalArchiver, journalHandler);
    }

    @Override
    protected Document readResponse(final CrawlerResponse response) throws IOException {
        return HtmlUtils.readHtmlDocument(response);
    }

}