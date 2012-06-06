package wwmm.pubcrawler.crawlers.acta;

import com.google.inject.Provides;
import uk.ac.cam.ch.wwmm.httpcrawler.HttpFetcher;
import wwmm.pubcrawler.crawlers.IssueTocCrawlTaskFactory;
import wwmm.pubcrawler.http.DocumentResource;
import wwmm.pubcrawler.http.Fetcher;
import wwmm.pubcrawler.http.RequestFactory;
import wwmm.pubcrawler.inject.AbstractPublisherCrawlerModule;
import wwmm.pubcrawler.model.id.PublisherId;
import wwmm.pubcrawler.parsers.IssueListParserFactory;
import wwmm.pubcrawler.parsers.IssueTocParserFactory;
import wwmm.pubcrawler.parsers.PublicationListParserFactory;
import wwmm.pubcrawler.tasks.HttpCrawlTaskData;

/**
 * @author Sam Adams
 */
public class IucrCrawlerModule extends AbstractPublisherCrawlerModule {

    @Provides
    public PublicationListParserFactory<DocumentResource> providePublicationListParserFactory() {
        return new IucrPublicationListParserFactory();
    }

    @Provides
    public IssueListParserFactory<DocumentResource> provideIssueListParserFactory() {
        return new IucrIssueListParserFactory();
    }


    @Provides
    public IssueTocParserFactory<IucrFrameResource> provideIssueTocParserFactory() {
        return new IucrIssueTocParserFactory();
    }

    @Provides
    public RequestFactory<HttpCrawlTaskData, IucrFrameRequest> providesIucrFrameRequestFactory() {
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

    @Override
    protected PublisherId getPublisherId() {
        return Iucr.PUBLISHER_ID;
    }
}
