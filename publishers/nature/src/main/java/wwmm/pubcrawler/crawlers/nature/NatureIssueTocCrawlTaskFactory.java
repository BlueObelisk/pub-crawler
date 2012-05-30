package wwmm.pubcrawler.crawlers.nature;

import wwmm.pubcrawler.crawlers.AbstractIssueTocCrawlTaskFactory;
import wwmm.pubcrawler.crawlers.nature.tasks.NatureIssueTocCrawlTask;
import wwmm.pubcrawler.tasks.IssueTocCrawlTaskData;
import wwmm.pubcrawler.tasks.TaskSpecification;

import javax.inject.Singleton;

/**
 * @author Sam Adams
 */
@Singleton
public class NatureIssueTocCrawlTaskFactory extends AbstractIssueTocCrawlTaskFactory {

    @Override
    protected TaskSpecification<IssueTocCrawlTaskData> getCrawlerType() {
        return NatureIssueTocCrawlTask.INSTANCE;
    }
}
