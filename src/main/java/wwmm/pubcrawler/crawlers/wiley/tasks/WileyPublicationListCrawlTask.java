package wwmm.pubcrawler.crawlers.wiley.tasks;

import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import wwmm.pubcrawler.archivers.JournalArchiver;
import wwmm.pubcrawler.controller.Fetcher;
import wwmm.pubcrawler.controller.URITask;
import wwmm.pubcrawler.crawlers.BasicPublicationListCrawlTask;
import wwmm.pubcrawler.crawlers.JournalHandler;
import wwmm.pubcrawler.crawlers.wiley.WileyPublicationListParserFactory;

import javax.inject.Inject;

/**
 * @author Sam Adams
 */
public class WileyPublicationListCrawlTask extends BasicPublicationListCrawlTask {

    @Inject
    public WileyPublicationListCrawlTask(final Fetcher<URITask, CrawlerResponse> fetcher, final WileyPublicationListParserFactory parserFactory, final JournalArchiver journalArchiver, final JournalHandler journalHandler) {
        super(fetcher, parserFactory, journalArchiver, journalHandler);
    }

}
