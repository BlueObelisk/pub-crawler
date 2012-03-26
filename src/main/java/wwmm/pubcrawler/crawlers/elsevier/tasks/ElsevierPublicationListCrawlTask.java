package wwmm.pubcrawler.crawlers.elsevier.tasks;

import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import wwmm.pubcrawler.controller.Fetcher;
import wwmm.pubcrawler.controller.JournalArchiver;
import wwmm.pubcrawler.controller.URITask;
import wwmm.pubcrawler.crawlers.BasicPublicationListCrawlTask;
import wwmm.pubcrawler.crawlers.elsevier.Elsevier;
import wwmm.pubcrawler.crawlers.elsevier.ElsevierPublicationListParserFactory;
import wwmm.pubcrawler.model.Journal;
import wwmm.pubcrawler.v2.crawler.CrawlTask;
import wwmm.pubcrawler.v2.crawler.TaskQueue;

import javax.inject.Inject;

/**
 * @author Sam Adams
 */
public class ElsevierPublicationListCrawlTask extends BasicPublicationListCrawlTask {

    @Inject
    public ElsevierPublicationListCrawlTask(final Fetcher<URITask, CrawlerResponse> fetcher, final TaskQueue taskQueue, final ElsevierPublicationListParserFactory parserFactory, final JournalArchiver journalArchiver) {
        super(fetcher, taskQueue, parserFactory, journalArchiver);
    }

    @Override
    protected CrawlTask createCurrentIssueTocTask(final Journal journal) {
        return Elsevier.createIssueTocTask(journal.getUrl(), journal.getAbbreviation(), "current");
    }
}
