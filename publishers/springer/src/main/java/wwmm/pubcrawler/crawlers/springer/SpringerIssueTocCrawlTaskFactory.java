package wwmm.pubcrawler.crawlers.springer;

import wwmm.pubcrawler.crawlers.AbstractIssueTocCrawlTaskFactory;
import wwmm.pubcrawler.crawlers.springer.tasks.SpringerIssueTocCrawlTask;
import wwmm.pubcrawler.crawler.CrawlRunner;
import wwmm.pubcrawler.tasks.IssueTocCrawlTaskData;
import wwmm.pubcrawler.tasks.TaskSpecification;

/**
 * @author Sam Adams
 */
public class SpringerIssueTocCrawlTaskFactory extends AbstractIssueTocCrawlTaskFactory {

    @Override
    protected TaskSpecification<IssueTocCrawlTaskData> getCrawlerType() {
        return SpringerIssueTocCrawlTask.INSTANCE;
    }

}
