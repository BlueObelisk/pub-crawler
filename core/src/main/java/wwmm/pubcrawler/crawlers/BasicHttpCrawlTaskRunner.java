package wwmm.pubcrawler.crawlers;

import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import wwmm.pubcrawler.http.Fetcher;
import wwmm.pubcrawler.http.UriRequest;
import wwmm.pubcrawler.tasks.HttpCrawlTaskData;
import wwmm.pubcrawler.tasks.TaskRunner;

import javax.inject.Inject;

/**
 * @author Sam Adams
 */
public abstract class BasicHttpCrawlTaskRunner<T extends HttpCrawlTaskData> extends HttpCrawlTask implements TaskRunner<T> {

    @Inject
    public BasicHttpCrawlTaskRunner(final Fetcher<UriRequest, CrawlerResponse> fetcher) {
        super(fetcher);
    }

    @Override
    public final void run(final String id, final T data) throws Exception {
        final CrawlerResponse response = fetchResource(id, data);
        handleResponse(id, data, response);
    }

    protected abstract void handleResponse(final String id, final T data, final CrawlerResponse response) throws Exception;

    protected CrawlerResponse fetchResource(final String taskId, final T data) throws Exception {
        return fetchResource(taskId, data.getFileId(), data.getUrl(), data.getReferrer(), data.getMaxAge());
    }
}
