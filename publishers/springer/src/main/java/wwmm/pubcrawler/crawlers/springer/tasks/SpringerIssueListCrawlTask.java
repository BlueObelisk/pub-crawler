package wwmm.pubcrawler.crawlers.springer.tasks;

import wwmm.pubcrawler.crawlers.CrawlTaskRunner;
import wwmm.pubcrawler.crawlers.IssueHandler;
import wwmm.pubcrawler.crawlers.springer.SpringerIssueListParserFactory;
import wwmm.pubcrawler.crawlers.springer.SpringerUriRequestFactory;
import wwmm.pubcrawler.http.DocumentResource;
import wwmm.pubcrawler.http.Fetcher;
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
public class SpringerIssueListCrawlTask implements TaskSpecification<IssueListCrawlTaskData> {

    public static final SpringerIssueListCrawlTask INSTANCE = new SpringerIssueListCrawlTask();

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
        public Runner(final Fetcher<UriRequest, DocumentResource> fetcher, final SpringerUriRequestFactory requestFactory, final SpringerIssueListParserFactory parserFactory, final IssueHandler issueHandler) {
            super(fetcher, requestFactory, new IssueListProcessor<DocumentResource, IssueListCrawlTaskData>(parserFactory, issueHandler));
        }

    }
}
