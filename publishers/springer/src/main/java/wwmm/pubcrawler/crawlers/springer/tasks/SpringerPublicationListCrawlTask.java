package wwmm.pubcrawler.crawlers.springer.tasks;

import wwmm.pubcrawler.archivers.JournalArchiver;
import wwmm.pubcrawler.crawlers.CrawlTaskRunner;
import wwmm.pubcrawler.crawlers.JournalHandler;
import wwmm.pubcrawler.crawlers.springer.SpringerPublicationListCrawlTaskData;
import wwmm.pubcrawler.crawlers.springer.SpringerPublicationListCrawlTaskDataMarshaller;
import wwmm.pubcrawler.crawlers.springer.SpringerPublicationListParserFactory;
import wwmm.pubcrawler.http.DocumentResource;
import wwmm.pubcrawler.http.Fetcher;
import wwmm.pubcrawler.http.RequestFactory;
import wwmm.pubcrawler.http.UriRequest;
import wwmm.pubcrawler.tasks.Marshaller;
import wwmm.pubcrawler.tasks.TaskSpecification;

import javax.inject.Inject;

/**
 * @author Sam Adams
 */
public class SpringerPublicationListCrawlTask implements TaskSpecification<SpringerPublicationListCrawlTaskData> {

    public static final SpringerPublicationListCrawlTask INSTANCE = new SpringerPublicationListCrawlTask();

    @Override
    public Class<Runner> getRunnerClass() {
        return Runner.class;
    }

    @Override
    public Marshaller<SpringerPublicationListCrawlTaskData> getDataMarshaller() {
        return new SpringerPublicationListCrawlTaskDataMarshaller();
    }

    public static class Runner extends CrawlTaskRunner<SpringerPublicationListCrawlTaskData, UriRequest, DocumentResource> {

        @Inject
        public Runner(final Fetcher<UriRequest, DocumentResource> fetcher, final RequestFactory<UriRequest> requestFactory, final SpringerIssueListProcessor processor) {
            super(fetcher, requestFactory, processor);
        }

    }

}
