package wwmm.pubcrawler.crawlers.rsc;

import wwmm.pubcrawler.crawler.CrawlRunner;
import wwmm.pubcrawler.crawlers.AbstractIssueTocCrawlTaskFactory;
import wwmm.pubcrawler.crawlers.elsevier.tasks.ElsevierIssueTocCrawlTask;
import wwmm.pubcrawler.crawlers.rsc.tasks.RscIssueTocCrawlTask;

import javax.inject.Singleton;

/**
 * @author Sam Adams
 */
@Singleton
public class RscIssueTocCrawlTaskFactory extends AbstractIssueTocCrawlTaskFactory {

    @Override
    protected Class<? extends CrawlRunner> getCrawlerType() {
        return RscIssueTocCrawlTask.class;
    }

}
