package wwmm.pubcrawler.main;

import org.joda.time.Duration;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import wwmm.pubcrawler.controller.SingleResourceHttpFetcher;
import wwmm.pubcrawler.controller.URITask;
import wwmm.pubcrawler.v2.crawler.CrawlRunner;
import wwmm.pubcrawler.v2.crawler.TaskData;

import javax.inject.Inject;
import java.net.URI;

/**
 * @author Sam Adams
 */
public abstract class SingleResourceHttpCrawlTask implements CrawlRunner {

    private final SingleResourceHttpFetcher fetcher;

    @Inject
    public SingleResourceHttpCrawlTask(final SingleResourceHttpFetcher fetcher) {
        this.fetcher = fetcher;
    }

    @Override
    public void run(final String id, final TaskData data) throws Exception {
        final CrawlerResponse response = fetchResource(id, data);
        handleResponse(id, data, response);
    }

    protected abstract void handleResponse(final String id, final TaskData data, final CrawlerResponse response) throws Exception;

    private CrawlerResponse fetchResource(final String taskId, final TaskData data) throws Exception {
        final String id = taskId + "/" + data.getString("fileId");
        final Duration maxAge = data.containsKey("maxAge") ? new Duration(Long.valueOf(data.getString("maxAge"))) : null;
        final URI url = URI.create(data.getString("url"));
        final URI referrer = data.containsKey("referrer") ? URI.create(data.getString("referrer")) : null;
        return fetcher.fetch(new URITask(url, id, maxAge, referrer));
    }

}
