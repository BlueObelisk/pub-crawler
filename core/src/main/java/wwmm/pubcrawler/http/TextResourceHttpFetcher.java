package wwmm.pubcrawler.http;

import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerRequest;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import uk.ac.cam.ch.wwmm.httpcrawler.GetRequestBuilder;
import uk.ac.cam.ch.wwmm.httpcrawler.HttpFetcher;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author Sam Adams
 */
@Singleton
public class TextResourceHttpFetcher implements Fetcher<UriRequest, TextResource> {

    private final HttpFetcher httpFetcher;

    @Inject
    public TextResourceHttpFetcher(final HttpFetcher httpFetcher) {
        this.httpFetcher = httpFetcher;
    }

    @Override
    public TextResource fetch(final UriRequest task) throws Exception {
        final CrawlerRequest request = createRequest(task);
        final CrawlerResponse response = httpFetcher.execute(request);
        final String text = readText(response);
        return new TextResource(response.getUrl(), text);
    }

    private String readText(final CrawlerResponse response) {
        return null;  //To change body of created methods use File | Settings | File Templates.
    }

    protected CrawlerRequest createRequest(final UriRequest request) {
        return new GetRequestBuilder()
            .withKey(request.getId())
            .withUrl(request.getUri())
            .withMaxAge(request.getMaxAge())
            .withReferrer(request.getReferrer())
            .withCookies(request.getCookies())
            .build();
    }

}
