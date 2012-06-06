package wwmm.pubcrawler.crawlers.acs;

import wwmm.pubcrawler.crawlers.AbstractIssueTocCrawlTaskFactory;
import wwmm.pubcrawler.crawlers.acs.tasks.AcsIssueTocCrawlTask;
import wwmm.pubcrawler.tasks.IssueTocCrawlTaskData;
import wwmm.pubcrawler.tasks.TaskSpecification;

import javax.inject.Singleton;

/**
 * @author Sam Adams
 */
@Singleton
public class AcsIssueTocCrawlTaskFactory extends AbstractIssueTocCrawlTaskFactory {
    
    @Override
    protected TaskSpecification<IssueTocCrawlTaskData> getCrawlerType() {
        return AcsIssueTocCrawlTask.INSTANCE;
    }
}
