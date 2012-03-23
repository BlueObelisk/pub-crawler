package wwmm.pubcrawler.controller;

import uk.ac.cam.ch.wwmm.httpcrawler.*;

import javax.inject.Inject;

/**
 * @author Sam Adams
 */
public class SingleResourceHttpFetcher implements Fetcher<URITask,CrawlerResponse> {

    private final HttpFetcher httpFetcher;

    @Inject
    public SingleResourceHttpFetcher(final HttpFetcher httpFetcher) {
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
