package wwmm.pubcrawler.crawlers.wiley.tasks;

import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import wwmm.pubcrawler.controller.Fetcher;
import wwmm.pubcrawler.controller.JournalArchiver;
import wwmm.pubcrawler.controller.URITask;
import wwmm.pubcrawler.crawlers.BasicPublicationListCrawlTask;
import wwmm.pubcrawler.crawlers.wiley.Wiley;
import wwmm.pubcrawler.crawlers.wiley.WileyPublicationListParserFactory;
import wwmm.pubcrawler.model.Journal;
import wwmm.pubcrawler.v2.crawler.CrawlTask;
import wwmm.pubcrawler.v2.crawler.TaskQueue;

import javax.inject.Inject;

/**
 * @author Sam Adams
 */
public class WileyPublicationListCrawlTask extends BasicPublicationListCrawlTask {

    @Inject
    public WileyPublicationListCrawlTask(final Fetcher<URITask, CrawlerResponse> fetcher, final TaskQueue taskQueue, final WileyPublicationListParserFactory parserFactory, final JournalArchiver journalArchiver) {
        super(fetcher, taskQueue, parserFactory, journalArchiver);
    }

    @Override
    protected CrawlTask createCurrentIssueTocTask(final Journal journal) {
        return Wiley.createIssueTocTask(journal.getAbbreviation(), "current");
    }

}
