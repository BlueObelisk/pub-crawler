package wwmm.pubcrawler.crawlers.rsc.tasks;

import nu.xom.Document;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import wwmm.pubcrawler.archivers.JournalArchiver;
import wwmm.pubcrawler.controller.Fetcher;
import wwmm.pubcrawler.controller.URITask;
import wwmm.pubcrawler.crawlers.BasicPublicationListCrawlTask;
import wwmm.pubcrawler.crawlers.JournalHandler;
import wwmm.pubcrawler.crawlers.rsc.RscPublicationListParserFactory;
import wwmm.pubcrawler.utils.HtmlUtils;

import javax.inject.Inject;
import java.io.IOException;

/**
 * @author Sam Adams
 */
public class RscPublicationListCrawlTask extends BasicPublicationListCrawlTask {

    @Inject
    public RscPublicationListCrawlTask(final Fetcher<URITask, CrawlerResponse> fetcher, final RscPublicationListParserFactory parserFactory, final JournalArchiver journalArchiver, final JournalHandler journalHandler) {
        super(fetcher, parserFactory, journalArchiver, journalHandler);
    }

    @Override
    protected Document readResponse(final CrawlerResponse response) throws IOException {
        return HtmlUtils.readXmlDocument(response);
    }

}
