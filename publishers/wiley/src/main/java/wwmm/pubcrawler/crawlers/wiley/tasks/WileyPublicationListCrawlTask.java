package wwmm.pubcrawler.crawlers.wiley.tasks;

import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import wwmm.pubcrawler.archivers.JournalArchiver;
import wwmm.pubcrawler.http.Fetcher;
import wwmm.pubcrawler.http.URITask;
import wwmm.pubcrawler.crawlers.BasicPublicationListCrawlTask;
import wwmm.pubcrawler.crawlers.JournalHandler;
import wwmm.pubcrawler.crawlers.wiley.WileyPublicationListParserFactory;
import wwmm.pubcrawler.http.DocumentResource;
import wwmm.pubcrawler.processors.PublicationListProcessor;

import javax.inject.Inject;

/**
 * @author Sam Adams
 */
public class WileyPublicationListCrawlTask extends BasicPublicationListCrawlTask {

    @Inject
    public WileyPublicationListCrawlTask(final Fetcher<URITask, CrawlerResponse> fetcher, final WileyPublicationListParserFactory parserFactory, final JournalArchiver journalArchiver, final JournalHandler journalHandler) {
        super(fetcher, new PublicationListProcessor<DocumentResource>(parserFactory, journalArchiver, journalHandler));
    }

}
