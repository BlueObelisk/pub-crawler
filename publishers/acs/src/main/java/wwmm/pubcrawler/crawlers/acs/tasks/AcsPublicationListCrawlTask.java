package wwmm.pubcrawler.crawlers.acs.tasks;

import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import wwmm.pubcrawler.archivers.JournalArchiver;
import wwmm.pubcrawler.controller.Fetcher;
import wwmm.pubcrawler.controller.URITask;
import wwmm.pubcrawler.crawlers.BasicPublicationListCrawlTask;
import wwmm.pubcrawler.crawlers.JournalHandler;
import wwmm.pubcrawler.crawlers.acs.AcsPublicationListParserFactory;
import wwmm.pubcrawler.http.HtmlDocument;
import wwmm.pubcrawler.processors.PublicationListProcessor;

import javax.inject.Inject;

/**
 * @author Sam Adams
 */
public class AcsPublicationListCrawlTask extends BasicPublicationListCrawlTask {

    @Inject
    public AcsPublicationListCrawlTask(final Fetcher<URITask, CrawlerResponse> fetcher, final AcsPublicationListParserFactory parserFactory, final JournalArchiver journalArchiver, final JournalHandler journalHandler) {
        super(fetcher, new PublicationListProcessor<HtmlDocument>(parserFactory, journalArchiver, journalHandler));
    }

}
