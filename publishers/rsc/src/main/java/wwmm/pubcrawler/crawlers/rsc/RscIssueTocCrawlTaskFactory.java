package wwmm.pubcrawler.crawlers.rsc;

import wwmm.pubcrawler.crawlers.AbstractIssueTocCrawlTaskFactory;
import wwmm.pubcrawler.crawlers.rsc.tasks.RscIssueTocCrawlTask;
import wwmm.pubcrawler.tasks.IssueTocCrawlTaskData;
import wwmm.pubcrawler.tasks.TaskSpecification;

import javax.inject.Singleton;

/**
 * @author Sam Adams
 */
@Singleton
public class RscIssueTocCrawlTaskFactory extends AbstractIssueTocCrawlTaskFactory {

    @Override
    protected TaskSpecification<IssueTocCrawlTaskData> getCrawlerType() {
        return RscIssueTocCrawlTask.INSTANCE;
    }

}
