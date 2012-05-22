package wwmm.pubcrawler.crawlers.acs.tasks;

import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import wwmm.pubcrawler.archivers.ArticleArchiver;
import wwmm.pubcrawler.archivers.IssueArchiver;
import wwmm.pubcrawler.crawlers.BasicIssueTocCrawlerTaskRunner;
import wwmm.pubcrawler.crawlers.IssueHandler;
import wwmm.pubcrawler.crawlers.acs.AcsIssueTocParserFactory;
import wwmm.pubcrawler.http.DocumentResource;
import wwmm.pubcrawler.http.Fetcher;
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
public class AcsIssueTocCrawlTask implements TaskSpecification<IssueTocCrawlTaskData> {

    public static final AcsIssueTocCrawlTask INSTANCE = new AcsIssueTocCrawlTask();

    @Override
    public Class<Runner> getRunnerClass() {
        return Runner.class;
    }

    @Override
    public Marshaller<IssueTocCrawlTaskData> getDataMarshaller() {
        return new IssueTocCrawlTaskDataMarshaller();
    }

    public static class Runner extends BasicIssueTocCrawlerTaskRunner {

        @Inject
        public Runner(final Fetcher<UriRequest, CrawlerResponse> fetcher, final AcsIssueTocParserFactory parserFactory, final ArticleArchiver articleArchiver, final IssueArchiver issueArchiver, final IssueHandler issueHandler) {
            super(fetcher, new IssueTocProcessor<DocumentResource>(issueArchiver, articleArchiver, issueHandler, parserFactory));
        }

    }
}
