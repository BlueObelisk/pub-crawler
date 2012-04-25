package wwmm.pubcrawler.crawlers.rsc.tasks;

import org.joda.time.Duration;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import wwmm.pubcrawler.archivers.ArticleArchiver;
import wwmm.pubcrawler.archivers.IssueArchiver;
import wwmm.pubcrawler.controller.Fetcher;
import wwmm.pubcrawler.controller.URITask;
import wwmm.pubcrawler.crawler.TaskData;
import wwmm.pubcrawler.crawlers.BasicIssueTocCrawlerTask;
import wwmm.pubcrawler.crawlers.IssueHandler;
import wwmm.pubcrawler.crawlers.rsc.RscIssueTocParserFactory;
import wwmm.pubcrawler.http.HtmlDocument;
import wwmm.pubcrawler.processors.IssueTocProcessor;

import javax.inject.Inject;
import java.net.URI;

/**
 * @author Sam Adams
 */
public class RscIssueTocCrawlTask extends BasicIssueTocCrawlerTask {

    // curl -v -d "name=SC&issueid=&jname=Chemical Science&pageno=1&issnprint=2041-6520&issnonline=2041-6539&iscontentavailable=True" http://pubs.rsc.org/en/journals/issues > issues.html

    // curl -d "name=SC&issueid=sc003004&jname=Chemical Science&iscontentavailable=True" http://pubs.rsc.org/en/journals/issues

    @Inject
    public RscIssueTocCrawlTask(final Fetcher<URITask, CrawlerResponse> fetcher, final RscIssueTocParserFactory parserFactory, final ArticleArchiver articleArchiver, final IssueArchiver issueArchiver, final IssueHandler issueHandler) {
        super(fetcher, new IssueTocProcessor<HtmlDocument>(issueArchiver, articleArchiver, issueHandler, parserFactory));
    }

    @Override
    protected CrawlerResponse fetchResource(final String taskId, final TaskData data) throws Exception {
        final Duration maxAge = data.containsKey("maxAge") ? new Duration(Long.valueOf(data.getString("maxAge"))) : null;
        final URI url = URI.create(data.getString("url"));
        final URI referrer = data.containsKey("referrer") ? URI.create(data.getString("referrer")) : null;
        final String id = taskId + "/" + data.getString("fileId");
        return fetcher.fetch(new URITask(url, id, maxAge, referrer));
    }

}
