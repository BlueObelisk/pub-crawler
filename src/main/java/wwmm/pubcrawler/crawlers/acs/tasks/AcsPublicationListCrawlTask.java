package wwmm.pubcrawler.crawlers.acs.tasks;

import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import wwmm.pubcrawler.controller.Fetcher;
import wwmm.pubcrawler.controller.JournalArchiver;
import wwmm.pubcrawler.controller.URITask;
import wwmm.pubcrawler.crawlers.BasicPublicationListCrawlTask;
import wwmm.pubcrawler.crawlers.acs.Acs;
import wwmm.pubcrawler.crawlers.acs.AcsPublicationListParserFactory;
import wwmm.pubcrawler.model.Journal;
import wwmm.pubcrawler.v2.crawler.CrawlTask;
import wwmm.pubcrawler.v2.crawler.TaskQueue;

import javax.inject.Inject;

/**
 * @author Sam Adams
 */
public class AcsPublicationListCrawlTask extends BasicPublicationListCrawlTask {

    @Inject
    public AcsPublicationListCrawlTask(final Fetcher<URITask, CrawlerResponse> fetcher, final TaskQueue taskQueue, final AcsPublicationListParserFactory parserFactory, final JournalArchiver journalArchiver) {
        super(fetcher, taskQueue, parserFactory, journalArchiver);
    }

    @Override
    protected CrawlTask createCurrentIssueTocTask(final Journal journal) {
        return Acs.createIssueTocTask(journal.getUrl(), journal.getAbbreviation(), "current");
    }
}
