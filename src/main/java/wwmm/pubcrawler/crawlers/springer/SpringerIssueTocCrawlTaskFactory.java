package wwmm.pubcrawler.crawlers.springer;

import wwmm.pubcrawler.crawlers.AbstractIssueTocCrawlTaskFactory;
import wwmm.pubcrawler.crawlers.springer.tasks.SpringerIssueTocCrawlTask;
import wwmm.pubcrawler.v2.crawler.CrawlRunner;

/**
 * @author Sam Adams
 */
public class SpringerIssueTocCrawlTaskFactory extends AbstractIssueTocCrawlTaskFactory {

    @Override
    protected Class<? extends CrawlRunner> getCrawlerType() {
        return SpringerIssueTocCrawlTask.class;
    }

}
