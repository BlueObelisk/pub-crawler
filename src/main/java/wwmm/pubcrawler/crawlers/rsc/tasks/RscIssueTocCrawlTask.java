package wwmm.pubcrawler.crawlers.rsc.tasks;

import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import wwmm.pubcrawler.archivers.ArticleArchiver;
import wwmm.pubcrawler.archivers.IssueArchiver;
import wwmm.pubcrawler.controller.Fetcher;
import wwmm.pubcrawler.controller.URITask;
import wwmm.pubcrawler.crawlers.BasicIssueTocCrawlerTask;
import wwmm.pubcrawler.crawlers.IssueHandler;
import wwmm.pubcrawler.crawlers.rsc.RscIssueTocParserFactory;

import javax.inject.Inject;

/**
 * @author Sam Adams
 */
public class RscIssueTocCrawlTask extends BasicIssueTocCrawlerTask {

    @Inject
    public RscIssueTocCrawlTask(final Fetcher<URITask, CrawlerResponse> fetcher, final RscIssueTocParserFactory parserFactory, final ArticleArchiver archiver, final IssueArchiver issueArchiver, final IssueHandler issueHandler) {
        super(fetcher, parserFactory, archiver, issueArchiver, issueHandler);
    }

}
