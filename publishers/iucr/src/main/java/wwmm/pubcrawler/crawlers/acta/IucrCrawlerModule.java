package wwmm.pubcrawler.crawlers.acta;

import com.google.inject.Provides;
import uk.ac.cam.ch.wwmm.httpcrawler.HttpFetcher;
import wwmm.pubcrawler.crawlers.IssueTocCrawlRunner;
import wwmm.pubcrawler.crawlers.IssueTocCrawlTaskFactory;
import wwmm.pubcrawler.crawlers.PublicationListCrawlRunner;
import wwmm.pubcrawler.http.DocumentResource;
import wwmm.pubcrawler.http.Fetcher;
import wwmm.pubcrawler.http.RequestFactory;
import wwmm.pubcrawler.http.UriRequest;
import wwmm.pubcrawler.inject.AbstractPublisherCrawlerModule;
import wwmm.pubcrawler.parsers.IssueTocParserFactory;
import wwmm.pubcrawler.parsers.PublicationListParserFactory;
import wwmm.pubcrawler.processors.IssueTocProcessor;
import wwmm.pubcrawler.processors.PublicationListProcessor;

/**
 * @author Sam Adams
 */
public class IucrCrawlerModule extends AbstractPublisherCrawlerModule {

    @Provides
    public PublicationListCrawlRunner providePublicationListCrawlRunner(final RequestFactory<UriRequest> requestFactory, final Fetcher<UriRequest,DocumentResource> fetcher, final PublicationListProcessor<DocumentResource> processor) {
        return new PublicationListCrawlRunner<UriRequest, DocumentResource>(requestFactory, fetcher, processor);
    }

    @Provides
    public IssueTocCrawlRunner provideIssueTocCrawlRunner(final RequestFactory<IucrFrameRequest> requestFactory, final Fetcher<IucrFrameRequest,IucrFrameResource> fetcher, final IssueTocProcessor<IucrFrameResource> processor) {
        return new IssueTocCrawlRunner<IucrFrameRequest, IucrFrameResource>(requestFactory, fetcher, processor);
    }

    @Provides
    public PublicationListParserFactory<DocumentResource> providePublicationListParserFactory() {
        return new IucrPublicationListParserFactory();
    }

    @Provides
    public IssueTocParserFactory<IucrFrameResource> provideIssueTocParserFactory() {
        return new IucrIssueTocParserFactory();
    }

    @Provides
    public RequestFactory<IucrFrameRequest> providesIucrFrameRequestFactory() {
        return new IucrFrameRequestFactory();
    }

    @Provides
    public Fetcher<IucrFrameRequest,IucrFrameResource> provideIucrFrameResourceFetcher(final HttpFetcher httpFetcher) {
        return new IucrFrameResourceFetcher(httpFetcher);
    }


    @Override
    protected Class<? extends IssueTocParserFactory> getIssueTocParserFactoryType() {
        return null;
    }

    @Override
    protected Class<? extends IssueTocCrawlTaskFactory> getIssueTocCrawlTaskFactoryType() {
        return IucrIssueTocCrawlTaskFactory.class;
    }
}
