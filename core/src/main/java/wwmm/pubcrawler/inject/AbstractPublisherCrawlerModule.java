package wwmm.pubcrawler.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import uk.ac.cam.ch.wwmm.httpcrawler.HttpFetcher;
import wwmm.pubcrawler.crawlers.IssueTocCrawlRunner;
import wwmm.pubcrawler.crawlers.IssueTocCrawlTaskFactory;
import wwmm.pubcrawler.http.*;
import wwmm.pubcrawler.parsers.IssueTocParserFactory;

/**
 * @author Sam Adams
 */
public abstract class AbstractPublisherCrawlerModule extends AbstractModule {

    @Override
    protected final void configure() {
        if (getIssueTocParserFactoryType() != null) {
            bind(IssueTocParserFactory.class).to(getIssueTocParserFactoryType());
        }
        if (getIssueTocCrawlTaskFactoryType() != null) {
            bind(IssueTocCrawlTaskFactory.class).to(getIssueTocCrawlTaskFactoryType());
        }
    }

    @Provides
    public RequestFactory<UriRequest> provideUriRequestFactory() {
        return new UriRequestFactory();
    }

    @Provides
    public Fetcher<UriRequest,DocumentResource> provideDocumentResourceFetcher(final HttpFetcher httpFetcher) {
        return new HtmlDocumentResourceHttpFetcher(httpFetcher);
    }


    protected abstract Class<? extends IssueTocParserFactory> getIssueTocParserFactoryType();

    protected abstract Class<? extends IssueTocCrawlTaskFactory> getIssueTocCrawlTaskFactoryType();

}
