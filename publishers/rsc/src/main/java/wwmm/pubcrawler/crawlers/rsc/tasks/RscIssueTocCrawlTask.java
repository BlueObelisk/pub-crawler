package wwmm.pubcrawler.crawlers.rsc.tasks;

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
public class RscIssueTocCrawlTask implements TaskSpecification<IssueTocCrawlTaskData> {

    public static final RscIssueTocCrawlTask INSTANCE = new RscIssueTocCrawlTask();

    @Override
    public Class<Runner> getRunnerClass() {
        return Runner.class;
    }

    @Override
    public Marshaller<IssueTocCrawlTaskData> getDataMarshaller() {
        return new IssueTocCrawlTaskDataMarshaller();
    }

    public static class Runner extends CrawlTaskRunner<IssueTocCrawlTaskData, UriRequest, DocumentResource> {

        // curl -v -d "name=SC&issueid=&jname=Chemical Science&pageno=1&issnprint=2041-6520&issnonline=2041-6539&iscontentavailable=True" http://pubs.rsc.org/en/journals/issues > issues.html

        // curl -d "name=SC&issueid=sc003004&jname=Chemical Science&iscontentavailable=True" http://pubs.rsc.org/en/journals/issues

        @Inject
        public Runner(final HtmlDocumentResourceHttpFetcher fetcher, final RequestFactory<UriRequest> requestFactory, final IssueTocProcessor<DocumentResource> issueTocProcessor) {
            super(fetcher, requestFactory, issueTocProcessor);
        }

    }

}
