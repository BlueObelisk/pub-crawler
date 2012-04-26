package wwmm.pubcrawler.crawlers.elsevier.tasks;

import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import wwmm.pubcrawler.archivers.ArticleArchiver;
import wwmm.pubcrawler.archivers.IssueArchiver;
import wwmm.pubcrawler.http.Fetcher;
import wwmm.pubcrawler.http.UriRequest;
import wwmm.pubcrawler.crawlers.BasicIssueTocCrawlerTask;
import wwmm.pubcrawler.crawlers.IssueHandler;
import wwmm.pubcrawler.crawlers.elsevier.ElsevierIssueTocParserFactory;
import wwmm.pubcrawler.http.DocumentResource;
import wwmm.pubcrawler.processors.IssueTocProcessor;

import javax.inject.Inject;

/**
 * @author Sam Adams
 */
public class ElsevierIssueTocCrawlTask extends BasicIssueTocCrawlerTask {

    @Inject
    public ElsevierIssueTocCrawlTask(final Fetcher<UriRequest, CrawlerResponse> fetcher, final ElsevierIssueTocParserFactory parserFactory, final ArticleArchiver articleArchiver, final IssueArchiver issueArchiver, final IssueHandler issueHandler) {
        super(fetcher, new IssueTocProcessor<DocumentResource>(issueArchiver, articleArchiver, issueHandler, parserFactory));
    }

}
