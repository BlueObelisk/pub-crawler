package wwmm.pubcrawler.crawlers.springer.tasks;

import wwmm.pubcrawler.crawlers.CrawlTaskRunner;
import wwmm.pubcrawler.crawlers.springer.SpringerUriRequestFactory;
import wwmm.pubcrawler.http.DocumentResource;
import wwmm.pubcrawler.http.HtmlDocumentResourceHttpFetcher;
import wwmm.pubcrawler.http.UriRequest;
import wwmm.pubcrawler.processors.IssueTocProcessor;
import wwmm.pubcrawler.tasks.IssueTocCrawlTaskData;
import wwmm.pubcrawler.tasks.IssueTocCrawlTaskDataMarshaller;
import wwmm.pubcrawler.tasks.Marshaller;
import wwmm.pubcrawler.tasks.TaskSpecification;

import javax.inject.Inject;

/**
 * @author Sam Adams
 */
public class SpringerIssueTocCrawlTask implements TaskSpecification<IssueTocCrawlTaskData> {

    public static final SpringerIssueTocCrawlTask INSTANCE = new SpringerIssueTocCrawlTask();

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
        public Runner(final HtmlDocumentResourceHttpFetcher fetcher, final SpringerUriRequestFactory requestFactory, final IssueTocProcessor<DocumentResource> issueTocProcessor) {
            super(fetcher, requestFactory, issueTocProcessor);
        }

    }
}
