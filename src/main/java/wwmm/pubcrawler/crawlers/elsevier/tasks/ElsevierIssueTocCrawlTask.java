package wwmm.pubcrawler.crawlers.elsevier.tasks;

import wwmm.pubcrawler.controller.ArticleArchiver;
import wwmm.pubcrawler.controller.BasicHttpFetcher;
import wwmm.pubcrawler.crawlers.BasicIssueTocCrawlerTask;
import wwmm.pubcrawler.crawlers.elsevier.Elsevier;
import wwmm.pubcrawler.crawlers.elsevier.ElsevierIssueTocParserFactory;
import wwmm.pubcrawler.model.Issue;
import wwmm.pubcrawler.v2.crawler.CrawlTask;
import wwmm.pubcrawler.v2.crawler.TaskQueue;

import javax.inject.Inject;

/**
 * @author Sam Adams
 */
public class ElsevierIssueTocCrawlTask extends BasicIssueTocCrawlerTask {

    @Inject
    public ElsevierIssueTocCrawlTask(final BasicHttpFetcher fetcher, final ElsevierIssueTocParserFactory parserFactory, final TaskQueue taskQueue, final ArticleArchiver archiver) {
        super(fetcher, parserFactory, taskQueue, archiver);
    }

    @Override
    protected CrawlTask createIssueTocTask(final String journal, final Issue prev) {
        return Elsevier.createIssueTocTask(prev.getUrl(), journal, prev.getVolume() + '/' + prev.getNumber());
    }

}
