package wwmm.pubcrawler.crawlers.bmc.tasks;

import nu.xom.Document;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import wwmm.pubcrawler.controller.Fetcher;
import wwmm.pubcrawler.controller.JournalArchiver;
import wwmm.pubcrawler.controller.URITask;
import wwmm.pubcrawler.crawlers.BasicPublicationListCrawlTask;
import wwmm.pubcrawler.crawlers.JournalHandler;
import wwmm.pubcrawler.crawlers.PublicationListParserFactory;
import wwmm.pubcrawler.crawlers.elsevier.Elsevier;
import wwmm.pubcrawler.crawlers.elsevier.ElsevierPublicationListParserFactory;
import wwmm.pubcrawler.model.Journal;
import wwmm.pubcrawler.utils.HtmlUtils;
import wwmm.pubcrawler.v2.crawler.CrawlTask;
import wwmm.pubcrawler.v2.crawler.TaskQueue;

import javax.inject.Inject;
import java.io.IOException;

/**
 * @author Sam Adams
 */
public class BmcPublicationListCrawlTask extends BasicPublicationListCrawlTask {

    @Inject
    public BmcPublicationListCrawlTask(final Fetcher<URITask, CrawlerResponse> fetcher, final PublicationListParserFactory parserFactory, final JournalArchiver journalArchiver, final JournalHandler journalHandler) {
        super(fetcher, parserFactory, journalArchiver, journalHandler);
    }

    @Override
    protected Document readResponse(final CrawlerResponse response) throws IOException {
        return HtmlUtils.readXmlDocument(response);
    }

}
