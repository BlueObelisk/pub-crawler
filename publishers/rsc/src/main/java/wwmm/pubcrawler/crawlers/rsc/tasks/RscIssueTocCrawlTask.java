package wwmm.pubcrawler.crawlers.rsc.tasks;

import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import wwmm.pubcrawler.archivers.ArticleArchiver;
import wwmm.pubcrawler.archivers.IssueArchiver;
import wwmm.pubcrawler.crawlers.BasicIssueTocCrawlerTaskRunner;
import wwmm.pubcrawler.crawlers.IssueHandler;
import wwmm.pubcrawler.crawlers.rsc.RscIssueTocParserFactory;
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

    public static class Runner extends BasicIssueTocCrawlerTaskRunner {

        // curl -v -d "name=SC&issueid=&jname=Chemical Science&pageno=1&issnprint=2041-6520&issnonline=2041-6539&iscontentavailable=True" http://pubs.rsc.org/en/journals/issues > issues.html

        // curl -d "name=SC&issueid=sc003004&jname=Chemical Science&iscontentavailable=True" http://pubs.rsc.org/en/journals/issues

        @Inject
        public Runner(final Fetcher<UriRequest, CrawlerResponse> fetcher, final RscIssueTocParserFactory parserFactory, final ArticleArchiver articleArchiver, final IssueArchiver issueArchiver, final IssueHandler issueHandler) {
            super(fetcher, new IssueTocProcessor<DocumentResource>(issueArchiver, articleArchiver, issueHandler, parserFactory));
        }

        @Override
        protected CrawlerResponse fetchResource(final String taskId, final IssueTocCrawlTaskData data) throws Exception {
            final String id = taskId + "/" + data.getFileId();
            return fetcher.fetch(new UriRequest(data.getUrl(), id, data.getMaxAge(), data.getReferrer()));
        }

    }
}
