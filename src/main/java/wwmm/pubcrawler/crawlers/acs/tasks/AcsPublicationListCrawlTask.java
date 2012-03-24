package wwmm.pubcrawler.crawlers.acs.tasks;

import nu.xom.Document;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import wwmm.pubcrawler.controller.Fetcher;
import wwmm.pubcrawler.controller.URITask;
import wwmm.pubcrawler.crawlers.BasicHttpCrawlTask;
import wwmm.pubcrawler.crawlers.acs.Acs;
import wwmm.pubcrawler.crawlers.acs.parsers.AcsPublicationListParser;
import wwmm.pubcrawler.model.Journal;
import wwmm.pubcrawler.utils.HtmlUtils;
import wwmm.pubcrawler.v2.crawler.CrawlTask;
import wwmm.pubcrawler.v2.crawler.TaskData;
import wwmm.pubcrawler.v2.crawler.TaskQueue;

import javax.inject.Inject;
import java.util.List;

/**
 * @author Sam Adams
 */
public class AcsPublicationListCrawlTask extends BasicHttpCrawlTask {

    private final TaskQueue taskQueue;

    @Inject
    public AcsPublicationListCrawlTask(final Fetcher<URITask, CrawlerResponse> fetcher, final TaskQueue taskQueue) {
        super(fetcher);
        this.taskQueue = taskQueue;
    }

    @Override
    protected void handleResponse(final String id, final TaskData data, final CrawlerResponse response) throws Exception {
        final Document html = HtmlUtils.readDocument(response);
        final AcsPublicationListParser parser = new AcsPublicationListParser(Acs.PUBLISHER_ID, html);
        final List<Journal> journals = parser.findJournals();
        for (Journal journal : journals) {
            final CrawlTask task = Acs.createIssueTocTask(journal.getUrl(), journal.getAbbreviation(), "current");
            taskQueue.queueTask(task);
        }
    }
}
