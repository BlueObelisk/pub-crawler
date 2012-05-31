package wwmm.pubcrawler.crawlers.acs.tasks;

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
import javax.inject.Singleton;

/**
 * @author Sam Adams
 */
@Singleton
public class AcsPublicationListCrawlTask implements TaskSpecification<HttpCrawlTaskData> {

    public static final AcsPublicationListCrawlTask INSTANCE = new AcsPublicationListCrawlTask();

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
