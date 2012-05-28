package wwmm.pubcrawler.crawlers.wiley;

import com.google.inject.Provides;
import wwmm.pubcrawler.crawlers.IssueTocCrawlRunner;
import wwmm.pubcrawler.crawlers.IssueTocCrawlTaskFactory;
import wwmm.pubcrawler.crawlers.PublicationListCrawlRunner;
import wwmm.pubcrawler.http.DocumentResource;
import wwmm.pubcrawler.http.Fetcher;
import wwmm.pubcrawler.http.RequestFactory;
import wwmm.pubcrawler.http.UriRequest;
import wwmm.pubcrawler.model.id.PublisherId;
import wwmm.pubcrawler.parsers.IssueTocParserFactory;
import wwmm.pubcrawler.inject.AbstractPublisherCrawlerModule;
import wwmm.pubcrawler.parsers.PublicationListParserFactory;
import wwmm.pubcrawler.processors.IssueTocProcessor;
import wwmm.pubcrawler.processors.PublicationListProcessor;

/**
 * @author Sam Adams
 */
public class WileyCrawlerModule extends AbstractPublisherCrawlerModule {

    @Provides
    public PublicationListCrawlRunner providePublicationListCrawlRunner(final RequestFactory<UriRequest> requestFactory, final Fetcher<UriRequest,DocumentResource> fetcher, final PublicationListProcessor<DocumentResource> processor) {
        return new PublicationListCrawlRunner<UriRequest, DocumentResource>(requestFactory, fetcher, processor);
    }

    @Provides
    public IssueTocCrawlRunner provideIssueTocCrawlRunner(final RequestFactory<UriRequest> requestFactory, final Fetcher<UriRequest,DocumentResource> fetcher, final IssueTocProcessor<DocumentResource> processor) {
        return new IssueTocCrawlRunner<UriRequest, DocumentResource>(requestFactory, fetcher, processor);
    }

    @Provides
    public PublicationListParserFactory<DocumentResource> providePublicationListParserFactory() {
        return new WileyPublicationListParserFactory();
    }

    @Provides
    public IssueTocParserFactory<DocumentResource> provideIssueTocParserFactory() {
        return new WileyIssueTocParserFactory();
    }

    @Override
    protected Class<? extends IssueTocParserFactory> getIssueTocParserFactoryType() {
        return WileyIssueTocParserFactory.class;
    }

    @Override
    protected Class<? extends IssueTocCrawlTaskFactory> getIssueTocCrawlTaskFactoryType() {
        return WileyIssueTocCrawlTaskFactory.class;
    }

    @Override
    protected PublisherId getPublisherId() {
        return Wiley.PUBLISHER_ID;
    }
}
