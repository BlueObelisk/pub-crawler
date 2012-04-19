package wwmm.pubcrawler.crawlers;

import org.joda.time.Duration;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import wwmm.pubcrawler.controller.Fetcher;
import wwmm.pubcrawler.controller.URITask;
import wwmm.pubcrawler.crawler.CrawlRunner;

import javax.inject.Inject;
import java.net.URI;

/**
 * @author Sam Adams
 */
public abstract class HttpCrawlTask implements CrawlRunner {

    protected final Fetcher<URITask,CrawlerResponse> fetcher;

    @Inject
    public HttpCrawlTask(final Fetcher<URITask, CrawlerResponse> fetcher) {
        this.fetcher = fetcher;
    }

    protected CrawlerResponse fetchResource(final String taskId, final String fileId, final URI url, final URI referrer, final Duration maxAge) throws Exception {
        final String id = taskId + "/" + fileId;
        return fetcher.fetch(new URITask(url, id, maxAge, referrer));
    }

}
