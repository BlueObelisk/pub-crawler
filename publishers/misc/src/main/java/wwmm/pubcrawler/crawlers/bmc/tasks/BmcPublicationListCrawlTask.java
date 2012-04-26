package wwmm.pubcrawler.crawlers.bmc.tasks;

import nu.xom.Document;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import wwmm.pubcrawler.archivers.JournalArchiver;
import wwmm.pubcrawler.http.Fetcher;
import wwmm.pubcrawler.http.URITask;
import wwmm.pubcrawler.crawlers.BasicPublicationListCrawlTask;
import wwmm.pubcrawler.crawlers.JournalHandler;
import wwmm.pubcrawler.http.DocumentResource;
import wwmm.pubcrawler.parsers.PublicationListParserFactory;
import wwmm.pubcrawler.processors.PublicationListProcessor;
import wwmm.pubcrawler.utils.HtmlUtils;

import javax.inject.Inject;
import java.io.IOException;

/**
 * @author Sam Adams
 */
public class BmcPublicationListCrawlTask extends BasicPublicationListCrawlTask {

    @Inject
    public BmcPublicationListCrawlTask(final Fetcher<URITask, CrawlerResponse> fetcher, final PublicationListParserFactory parserFactory, final JournalArchiver journalArchiver, final JournalHandler journalHandler) {
        super(fetcher, new PublicationListProcessor<DocumentResource>(parserFactory, journalArchiver, journalHandler));
    }

    @Override
    protected Document readResponse(final CrawlerResponse response) throws IOException {
        return HtmlUtils.readXmlDocument(response);
    }

}
