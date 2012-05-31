package wwmm.pubcrawler.crawlers.acta.tasks;

import wwmm.pubcrawler.crawlers.CrawlTaskRunner;
import wwmm.pubcrawler.http.DocumentResource;
import wwmm.pubcrawler.http.Fetcher;
import wwmm.pubcrawler.http.RequestFactory;
import wwmm.pubcrawler.http.UriRequest;
import wwmm.pubcrawler.processors.IssueListProcessor;
import wwmm.pubcrawler.tasks.IssueListCrawlTaskData;
import wwmm.pubcrawler.tasks.IssueListCrawlTaskDataMarshaller;
import wwmm.pubcrawler.tasks.Marshaller;
import wwmm.pubcrawler.tasks.TaskSpecification;

import javax.inject.Inject;

/**
 * @author Sam Adams
 */
public class IucrIssueListCrawlerTask implements TaskSpecification<IssueListCrawlTaskData> {

    public static final IucrIssueListCrawlerTask INSTANCE = new IucrIssueListCrawlerTask();

    @Override
    public Class<Runner> getRunnerClass() {
        return Runner.class;
    }

    @Override
    public Marshaller<IssueListCrawlTaskData> getDataMarshaller() {
        return new IssueListCrawlTaskDataMarshaller();
    }

    public static class Runner extends CrawlTaskRunner<IssueListCrawlTaskData, UriRequest, DocumentResource> {

        @Inject
        public Runner(final Fetcher<UriRequest, DocumentResource> fetcher, final RequestFactory<UriRequest> requestFactory, final IssueListProcessor<DocumentResource, IssueListCrawlTaskData> issueListProcessor) {
            super(fetcher, requestFactory, issueListProcessor);
        }
    }

}
