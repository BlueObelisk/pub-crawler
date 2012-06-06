package wwmm.pubcrawler.crawlers.nature.tasks;

import wwmm.pubcrawler.crawlers.CrawlTaskRunner;
import wwmm.pubcrawler.http.DocumentResource;
import wwmm.pubcrawler.http.HtmlDocumentResourceHttpFetcher;
import wwmm.pubcrawler.http.RequestFactory;
import wwmm.pubcrawler.http.UriRequest;
import wwmm.pubcrawler.processors.IssueTocProcessor;
import wwmm.pubcrawler.tasks.*;

import javax.inject.Inject;

/**
 * @author Sam Adams
 */
public class NatureIssueTocCrawlTask implements TaskSpecification<IssueTocCrawlTaskData> {

    public static final NatureIssueTocCrawlTask INSTANCE = new NatureIssueTocCrawlTask();

    @Override
    public Class<Runner> getRunnerClass() {
        return Runner.class;
    }

    @Override
    public Marshaller<IssueTocCrawlTaskData> getDataMarshaller() {
        return new IssueTocCrawlTaskDataMarshaller();
    }

    public static class Runner extends CrawlTaskRunner<IssueTocCrawlTaskData, UriRequest, DocumentResource> {

        @Inject
        public Runner(final HtmlDocumentResourceHttpFetcher fetcher, final RequestFactory<HttpCrawlTaskData, UriRequest> requestFactory, final IssueTocProcessor<DocumentResource> issueTocProcessor) {
            super(fetcher, requestFactory, issueTocProcessor);
        }

    }
}
