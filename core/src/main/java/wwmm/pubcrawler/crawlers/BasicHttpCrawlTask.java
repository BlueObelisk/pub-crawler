package wwmm.pubcrawler.crawlers;

import org.joda.time.Duration;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import wwmm.pubcrawler.controller.Fetcher;
import wwmm.pubcrawler.controller.URITask;
import wwmm.pubcrawler.crawler.CrawlRunner;
import wwmm.pubcrawler.crawler.TaskData;

import javax.inject.Inject;
import java.net.URI;

/**
 * @author Sam Adams
 */
public abstract class BasicHttpCrawlTask extends HttpCrawlTask implements CrawlRunner {

    @Inject
    public BasicHttpCrawlTask(final Fetcher<URITask,CrawlerResponse> fetcher) {
        super(fetcher);
    }

    @Override
    public final void run(final String id, final TaskData data) throws Exception {
        final CrawlerResponse response = fetchResource(id, data);
        handleResponse(id, data, response);
    }

    protected abstract void handleResponse(final String id, final TaskData data, final CrawlerResponse response) throws Exception;

    protected CrawlerResponse fetchResource(final String taskId, final TaskData data) throws Exception {
        final Duration maxAge = data.containsKey("maxAge") ? new Duration(Long.valueOf(data.getString("maxAge"))) : null;
        final URI url = URI.create(data.getString("url"));
        final URI referrer = data.containsKey("referrer") ? URI.create(data.getString("referrer")) : null;
        return fetchResource(taskId, data.getString("fileId"), url, referrer, maxAge);
    }

}