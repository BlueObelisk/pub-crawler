package wwmm.pubcrawler.crawlers.acs.tasks;

import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import wwmm.pubcrawler.archivers.ArticleArchiver;
import wwmm.pubcrawler.archivers.IssueArchiver;
import wwmm.pubcrawler.controller.*;
import wwmm.pubcrawler.crawlers.BasicIssueTocCrawlerTask;
import wwmm.pubcrawler.crawlers.IssueHandler;
import wwmm.pubcrawler.crawlers.acs.AcsIssueTocParserFactory;

import javax.inject.Inject;

/**
 * @author Sam Adams
 */
public class AcsIssueTocCrawlTask extends BasicIssueTocCrawlerTask {

    @Inject
    public AcsIssueTocCrawlTask(final Fetcher<URITask, CrawlerResponse> fetcher, final AcsIssueTocParserFactory parserFactory, final ArticleArchiver archiver, final IssueArchiver issueArchiver, final IssueHandler issueHandler) {
        super(fetcher, parserFactory, archiver, issueArchiver, issueHandler);
    }

}