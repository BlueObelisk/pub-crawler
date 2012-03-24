package wwmm.pubcrawler.crawlers.acs.tasks;

import wwmm.pubcrawler.controller.ArticleArchiver;
import wwmm.pubcrawler.controller.BasicHttpFetcher;
import wwmm.pubcrawler.crawlers.BasicIssueTocCrawlerTask;
import wwmm.pubcrawler.crawlers.acs.Acs;
import wwmm.pubcrawler.crawlers.acs.AcsIssueTocParserFactory;
import wwmm.pubcrawler.crawlers.elsevier.Elsevier;
import wwmm.pubcrawler.crawlers.elsevier.ElsevierIssueTocParserFactory;
import wwmm.pubcrawler.model.Issue;
import wwmm.pubcrawler.v2.crawler.CrawlTask;
import wwmm.pubcrawler.v2.crawler.TaskQueue;

import javax.inject.Inject;

/**
 * @author Sam Adams
 */
public class AcsIssueTocCrawlTask extends BasicIssueTocCrawlerTask {

    @Inject
    public AcsIssueTocCrawlTask(final BasicHttpFetcher fetcher, final AcsIssueTocParserFactory parserFactory, final TaskQueue taskQueue, final ArticleArchiver archiver) {
        super(fetcher, parserFactory, taskQueue, archiver);
    }

    @Override
    protected CrawlTask createIssueTocTask(final String journal, final Issue prev) {
        return Acs.createIssueTocTask(prev.getUrl(), journal, prev.getVolume() + '/' + prev.getNumber());
    }

}
