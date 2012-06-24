package wwmm.pubcrawler.crawlers.wiley.tasks;

import wwmm.pubcrawler.crawlers.CrawlTaskRunner;
import wwmm.pubcrawler.crawlers.wiley.WileyIssueListIndexProcessor;
import wwmm.pubcrawler.http.DocumentResource;
import wwmm.pubcrawler.http.HtmlDocumentResourceHttpFetcher;
import wwmm.pubcrawler.http.RequestFactory;
import wwmm.pubcrawler.http.UriRequest;
import wwmm.pubcrawler.tasks.*;

import javax.inject.Inject;

/**
 * @author Sam Adams
 */
public class WileyIssueListIndexCrawlTask implements TaskSpecification<IssueListCrawlTaskData> {

    public static final WileyIssueListIndexCrawlTask INSTANCE = new WileyIssueListIndexCrawlTask();

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
        public Runner(final HtmlDocumentResourceHttpFetcher fetcher, final RequestFactory<HttpCrawlTaskData, UriRequest> requestFactory, final WileyIssueListIndexProcessor processor) {
            super(fetcher, requestFactory, processor);
        }

    }

}
