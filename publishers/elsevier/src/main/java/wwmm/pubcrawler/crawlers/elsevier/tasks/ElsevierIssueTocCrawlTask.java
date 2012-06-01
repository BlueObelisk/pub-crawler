package wwmm.pubcrawler.crawlers.elsevier.tasks;

import wwmm.pubcrawler.crawlers.CrawlTaskRunner;
import wwmm.pubcrawler.http.DocumentResource;
import wwmm.pubcrawler.http.HtmlDocumentResourceHttpFetcher;
import wwmm.pubcrawler.http.RequestFactory;
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
public class ElsevierIssueTocCrawlTask implements TaskSpecification<IssueTocCrawlTaskData> {

    public static final ElsevierIssueTocCrawlTask INSTANCE = new ElsevierIssueTocCrawlTask();

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
        public Runner(final HtmlDocumentResourceHttpFetcher fetcher, final RequestFactory<UriRequest> requestFactory, final IssueTocProcessor<DocumentResource> issueTocProcessor) {
            super(fetcher, requestFactory, issueTocProcessor);
        }

    }
}
