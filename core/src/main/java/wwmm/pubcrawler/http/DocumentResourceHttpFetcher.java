package wwmm.pubcrawler.http;

import nu.xom.Document;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerRequest;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import uk.ac.cam.ch.wwmm.httpcrawler.GetRequestBuilder;
import uk.ac.cam.ch.wwmm.httpcrawler.HttpFetcher;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URI;

/**
 * @author Sam Adams
 */
public abstract class DocumentResourceHttpFetcher implements Fetcher<UriRequest, DocumentResource> {

    private final HttpFetcher httpFetcher;

    @Inject
    public DocumentResourceHttpFetcher(final HttpFetcher httpFetcher) {
        this.httpFetcher = httpFetcher;
    }

    @Override
    public DocumentResource fetch(final UriRequest task) throws Exception {
        final CrawlerRequest request = createRequest(task);
        final CrawlerResponse response = httpFetcher.execute(request);
        final Document document = readDocument(response);
        return new DocumentResource(URI.create(document.getBaseURI()), document);
    }

    protected abstract Document readDocument(final CrawlerResponse response) throws IOException;

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
