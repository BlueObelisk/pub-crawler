package wwmm.pubcrawler.crawlers.acs.tasks;

import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import wwmm.pubcrawler.archivers.ArticleArchiver;
import wwmm.pubcrawler.archivers.IssueArchiver;
import wwmm.pubcrawler.http.Fetcher;
import wwmm.pubcrawler.http.URITask;
import wwmm.pubcrawler.crawlers.BasicIssueTocCrawlerTask;
import wwmm.pubcrawler.crawlers.IssueHandler;
import wwmm.pubcrawler.crawlers.acs.AcsIssueTocParserFactory;
import wwmm.pubcrawler.http.DocumentResource;
import wwmm.pubcrawler.processors.IssueTocProcessor;

import javax.inject.Inject;

/**
 * @author Sam Adams
 */
public class AcsIssueTocCrawlTask extends BasicIssueTocCrawlerTask {

    @Inject
    public AcsIssueTocCrawlTask(final Fetcher<URITask, CrawlerResponse> fetcher, final AcsIssueTocParserFactory parserFactory, final ArticleArchiver articleArchiver, final IssueArchiver issueArchiver, final IssueHandler issueHandler) {
        super(fetcher, new IssueTocProcessor<DocumentResource>(issueArchiver, articleArchiver, issueHandler, parserFactory));
    }

}
