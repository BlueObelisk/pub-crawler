package wwmm.pubcrawler.crawlers.acta.tasks;

import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import wwmm.pubcrawler.crawlers.BasicIssueListHttpCrawlTaskRunner;
import wwmm.pubcrawler.crawlers.IssueHandler;
import wwmm.pubcrawler.crawlers.acta.IucrIssueListParserFactory;
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

    public static class Runner extends BasicIssueListHttpCrawlTaskRunner {

        @Inject
        public Runner(final Fetcher<UriRequest, CrawlerResponse> fetcher, final IucrIssueListParserFactory parserFactory, final IssueHandler issueHandler) {
            super(fetcher, new IssueListProcessor<DocumentResource>(parserFactory, issueHandler));
        }
    }

}
