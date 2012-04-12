package wwmm.pubcrawler.crawlers.acta.tasks;

import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import wwmm.pubcrawler.archivers.ArticleArchiver;
import wwmm.pubcrawler.archivers.IssueArchiver;
import wwmm.pubcrawler.controller.Fetcher;
import wwmm.pubcrawler.controller.URITask;
import wwmm.pubcrawler.crawlers.BasicIssueTocCrawlerTask;
import wwmm.pubcrawler.crawlers.IssueHandler;
import wwmm.pubcrawler.crawlers.elsevier.ElsevierIssueTocParserFactory;

import javax.inject.Inject;

/**
 * @author Sam Adams
 */
public class IucrIssueTocCrawlTask extends BasicIssueTocCrawlerTask {

    @Inject
    public IucrIssueTocCrawlTask(final Fetcher<URITask, CrawlerResponse> fetcher, final ElsevierIssueTocParserFactory parserFactory, final ArticleArchiver archiver, final IssueArchiver issueArchiver, final IssueHandler issueHandler) {
        super(fetcher, parserFactory, archiver, issueArchiver, issueHandler);
    }

}
