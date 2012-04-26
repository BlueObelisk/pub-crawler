package wwmm.pubcrawler.controller;

import uk.ac.cam.ch.wwmm.httpcrawler.*;
import wwmm.pubcrawler.http.Fetcher;
import wwmm.pubcrawler.http.URITask;

import javax.inject.Inject;

/**
 * @author Sam Adams
 */
public class BasicHttpFetcher implements Fetcher<URITask,CrawlerResponse> {

    private final HttpFetcher httpFetcher;

    @Inject
    public BasicHttpFetcher(final HttpFetcher httpFetcher) {
        this.httpFetcher = httpFetcher;
    }

    @Override
    public CrawlerResponse fetch(final URITask task) throws Exception {
        final CrawlerRequest request = createRequest(task);
        return httpFetcher.execute(request);
    }

    private CrawlerRequest createRequest(final URITask task) {
        return new GetRequestBuilder()
            .withKey(task.getId())
            .withUrl(task.getUri())
            .withMaxAge(task.getMaxAge())
            .withReferrer(task.getReferer())
            .build();
    }

}
