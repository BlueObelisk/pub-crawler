package wwmm.pubcrawler.main;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.ParsingException;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import wwmm.pubcrawler.controller.SingleResourceHttpFetcher;
import wwmm.pubcrawler.crawlers.elsevier.Elsevier;
import wwmm.pubcrawler.crawlers.elsevier.parsers.ElsevierPublicationListParser;
import wwmm.pubcrawler.model.Journal;
import wwmm.pubcrawler.v2.crawler.CrawlTask;
import wwmm.pubcrawler.v2.crawler.TaskData;
import wwmm.pubcrawler.v2.crawler.TaskQueue;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;

/**
 * @author Sam Adams
 */
public class ElsevierPublicationListCrawlTask extends SingleResourceHttpCrawlTask {

    private final TaskQueue taskQueue;

    @Inject
    public ElsevierPublicationListCrawlTask(final SingleResourceHttpFetcher fetcher, final TaskQueue taskQueue) {
        super(fetcher);
        this.taskQueue = taskQueue;
    }

    @Override
    protected void handleResponse(final String id, final TaskData data, final CrawlerResponse response) throws IOException, ParsingException {
        final Document xml = new Builder().build(response.getContent());
        final ElsevierPublicationListParser parser = new ElsevierPublicationListParser(xml);
        final List<Journal> journals = parser.findJournals();
        for (Journal journal : journals) {

            CrawlTask task = Elsevier.createIssueTocTask(journal.getUrl(), journal.getAbbreviation(), "current");

            taskQueue.queueTask(task);
        }
    }

}
