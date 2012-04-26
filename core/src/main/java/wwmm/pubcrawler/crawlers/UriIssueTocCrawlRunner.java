package wwmm.pubcrawler.crawlers;

import wwmm.pubcrawler.http.DocumentResource;
import wwmm.pubcrawler.http.Fetcher;
import wwmm.pubcrawler.http.RequestFactory;
import wwmm.pubcrawler.http.UriRequest;
import wwmm.pubcrawler.processors.IssueTocProcessor;

import javax.inject.Inject;

/**
 * @author Sam Adams
 */
public class UriIssueTocCrawlRunner extends IssueTocCrawlRunner<UriRequest,DocumentResource> {

    @Inject
    public UriIssueTocCrawlRunner(final RequestFactory<UriRequest> requestFactory, final Fetcher<UriRequest, DocumentResource> fetcher, final IssueTocProcessor<DocumentResource> processor) {
        super(requestFactory, fetcher, processor);
    }

}
