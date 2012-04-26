package wwmm.pubcrawler.crawlers.elsevier;

import com.google.inject.Provides;
import wwmm.pubcrawler.crawlers.IssueTocCrawlRunner;
import wwmm.pubcrawler.crawlers.IssueTocCrawlTaskFactory;
import wwmm.pubcrawler.crawlers.PublicationListCrawlRunner;
import wwmm.pubcrawler.http.DocumentResource;
import wwmm.pubcrawler.http.Fetcher;
import wwmm.pubcrawler.http.RequestFactory;
import wwmm.pubcrawler.http.UriRequest;
import wwmm.pubcrawler.parsers.IssueTocParserFactory;
import wwmm.pubcrawler.inject.AbstractPublisherCrawlerModule;
import wwmm.pubcrawler.parsers.PublicationListParserFactory;
import wwmm.pubcrawler.processors.IssueTocProcessor;
import wwmm.pubcrawler.processors.PublicationListProcessor;

/**
 * @author Sam Adams
 */
public class ElsevierCrawlerModule extends AbstractPublisherCrawlerModule {

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
        return new ElsevierPublicationListParserFactory();
    }

    @Provides
    public IssueTocParserFactory<DocumentResource> provideIssueTocParserFactory() {
        return new ElsevierIssueTocParserFactory();
    }

    @Override
    protected Class<? extends IssueTocParserFactory> getIssueTocParserFactoryType() {
        return ElsevierIssueTocParserFactory.class;
    }

    @Override
    protected Class<? extends IssueTocCrawlTaskFactory> getIssueTocCrawlTaskFactoryType() {
        return ElsevierIssueTocCrawlTaskFactory.class;
    }

}
