package wwmm.pubcrawler.crawlers.rsc.tasks;

import wwmm.pubcrawler.crawlers.CrawlTaskRunner;
import wwmm.pubcrawler.crawlers.rsc.RscIssueTocCrawlTaskData;
import wwmm.pubcrawler.crawlers.rsc.RscIssueTocCrawlTaskDataMarshaller;
import wwmm.pubcrawler.crawlers.rsc.RscIssueTocFetcher;
import wwmm.pubcrawler.crawlers.rsc.RscIssueTocRequest;
import wwmm.pubcrawler.http.DocumentResource;
import wwmm.pubcrawler.http.RequestFactory;
import wwmm.pubcrawler.processors.IssueTocProcessor;
import wwmm.pubcrawler.tasks.IssueTocCrawlTaskData;
import wwmm.pubcrawler.tasks.IssueTocCrawlTaskDataMarshaller;
import wwmm.pubcrawler.tasks.Marshaller;
import wwmm.pubcrawler.tasks.TaskSpecification;

import javax.inject.Inject;

/**
 * @author Sam Adams
 */
public class RscIssueTocCrawlTask implements TaskSpecification<RscIssueTocCrawlTaskData> {

    public static final RscIssueTocCrawlTask INSTANCE = new RscIssueTocCrawlTask();

    @Override
    public Class<Runner> getRunnerClass() {
        return Runner.class;
    }

    @Override
    public Marshaller<RscIssueTocCrawlTaskData> getDataMarshaller() {
        return new RscIssueTocCrawlTaskDataMarshaller();
    }

    public static class Runner extends CrawlTaskRunner<RscIssueTocCrawlTaskData, RscIssueTocRequest, DocumentResource> {

        // curl -v -d "name=SC&issueid=&jname=Chemical Science&pageno=1&issnprint=2041-6520&issnonline=2041-6539&iscontentavailable=True" http://pubs.rsc.org/en/journals/issues > issues.html

        // curl -d "name=SC&issueid=sc003004&jname=Chemical Science&iscontentavailable=True" http://pubs.rsc.org/en/journals/issues

        @Inject
        public Runner(final RscIssueTocFetcher fetcher, final RequestFactory<RscIssueTocCrawlTaskData, RscIssueTocRequest> requestFactory, final IssueTocProcessor<DocumentResource> issueTocProcessor) {
            super(fetcher, requestFactory, issueTocProcessor);
        }

    }

}
