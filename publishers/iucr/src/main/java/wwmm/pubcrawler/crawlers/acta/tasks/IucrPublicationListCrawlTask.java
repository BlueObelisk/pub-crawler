package wwmm.pubcrawler.crawlers.acta.tasks;

import wwmm.pubcrawler.crawlers.CrawlTaskRunner;
import wwmm.pubcrawler.http.DocumentResource;
import wwmm.pubcrawler.http.Fetcher;
import wwmm.pubcrawler.http.RequestFactory;
import wwmm.pubcrawler.http.UriRequest;
import wwmm.pubcrawler.processors.PublicationListProcessor;
import wwmm.pubcrawler.tasks.HttpCrawlTaskData;
import wwmm.pubcrawler.tasks.HttpCrawlTaskDataMarshaller;
import wwmm.pubcrawler.tasks.Marshaller;
import wwmm.pubcrawler.tasks.TaskSpecification;

import javax.inject.Inject;

/**
 * @author Sam Adams
 */
public class IucrPublicationListCrawlTask implements TaskSpecification<HttpCrawlTaskData> {

    public static final IucrPublicationListCrawlTask INSTANCE = new IucrPublicationListCrawlTask();

    @Override
    public Class<Runner> getRunnerClass() {
        return Runner.class;
    }

    @Override
    public Marshaller<HttpCrawlTaskData> getDataMarshaller() {
        return new HttpCrawlTaskDataMarshaller();
    }

    public static class Runner extends CrawlTaskRunner<HttpCrawlTaskData, UriRequest, DocumentResource> {

        @Inject
        public Runner(final Fetcher<UriRequest, DocumentResource> fetcher, final RequestFactory<UriRequest> requestFactory, final PublicationListProcessor<DocumentResource> publicationListProcessor) {
            super(fetcher, requestFactory, publicationListProcessor);
        }

    }
}
