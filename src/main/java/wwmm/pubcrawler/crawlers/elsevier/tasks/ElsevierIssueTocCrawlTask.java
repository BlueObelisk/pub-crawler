package wwmm.pubcrawler.crawlers.elsevier.tasks;

import wwmm.pubcrawler.controller.ArticleArchiver;
import wwmm.pubcrawler.controller.BasicHttpFetcher;
import wwmm.pubcrawler.controller.IssueArchiver;
import wwmm.pubcrawler.crawlers.BasicIssueTocCrawlerTask;
import wwmm.pubcrawler.crawlers.IssueHandler;
import wwmm.pubcrawler.crawlers.elsevier.ElsevierIssueTocParserFactory;

import javax.inject.Inject;

/**
 * @author Sam Adams
 */
public class ElsevierIssueTocCrawlTask extends BasicIssueTocCrawlerTask {

    @Inject
    public ElsevierIssueTocCrawlTask(final BasicHttpFetcher fetcher, final ElsevierIssueTocParserFactory parserFactory, final ArticleArchiver archiver, final IssueArchiver issueArchiver, final IssueHandler issueHandler) {
        super(fetcher, parserFactory, archiver, issueArchiver, issueHandler);
    }

}
