package wwmm.pubcrawler.main;

import nu.xom.Document;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import wwmm.pubcrawler.controller.SingleResourceHttpFetcher;
import wwmm.pubcrawler.crawlers.elsevier.Elsevier;
import wwmm.pubcrawler.crawlers.elsevier.parsers.ElsevierIssueTocParser;
import wwmm.pubcrawler.model.Issue;
import wwmm.pubcrawler.v2.crawler.CrawlTask;
import wwmm.pubcrawler.v2.crawler.TaskData;
import wwmm.pubcrawler.v2.crawler.TaskQueue;

import javax.inject.Inject;
import java.net.URI;

/**
 * @author Sam Adams
 */
public class ElsevierIssueTocCrawlerTask extends SingleResourceHttpCrawlTask {

    private final TaskQueue taskQueue;

    @Inject
    public ElsevierIssueTocCrawlerTask(final SingleResourceHttpFetcher fetcher, final TaskQueue taskQueue) {
        super(fetcher);
        this.taskQueue = taskQueue;
    }

    @Override
    protected void handleResponse(final String id, final TaskData data, final CrawlerResponse response) throws Exception {
        final Document html = HtmlUtils.readDocument(response);
        final URI url = URI.create(data.getString("url"));
        final String journal = data.getString("journal");
        final ElsevierIssueTocParser parser = new ElsevierIssueTocParser(html, url, journal);
        
        Issue prev = parser.getPreviousIssue();

        CrawlTask task = Elsevier.createIssueTocTask(prev.getUrl(), journal, prev.getVolume() + '/' + prev.getNumber());
        taskQueue.queueTask(task);
    }

}
