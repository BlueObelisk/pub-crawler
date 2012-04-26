package wwmm.pubcrawler.crawlers.acta;

import uk.ac.cam.ch.wwmm.httpcrawler.HttpFetcher;
import wwmm.pubcrawler.http.Fetcher;
import wwmm.pubcrawler.http.UriRequest;

import javax.inject.Inject;

/**
 * @author Sam Adams
 */
public class IucrFrameResourceFetcher implements Fetcher<UriRequest, IucrFrameResource> {

    private final HttpFetcher httpFetcher;

    @Inject
    public IucrFrameResourceFetcher(final HttpFetcher httpFetcher) {
        this.httpFetcher = httpFetcher;
    }

    @Override
    public IucrFrameResource fetch(final UriRequest request) throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

}
