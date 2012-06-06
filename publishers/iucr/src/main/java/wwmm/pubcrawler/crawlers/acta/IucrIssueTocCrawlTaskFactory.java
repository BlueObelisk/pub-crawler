package wwmm.pubcrawler.crawlers.acta;

import wwmm.pubcrawler.crawlers.AbstractIssueTocCrawlTaskFactory;
import wwmm.pubcrawler.crawlers.acta.tasks.IucrIssueTocCrawlTask;
import wwmm.pubcrawler.tasks.IssueTocCrawlTaskData;
import wwmm.pubcrawler.tasks.TaskSpecification;

/**
 * @author Sam Adams
 */
public class IucrIssueTocCrawlTaskFactory extends AbstractIssueTocCrawlTaskFactory {

    @Override
    protected TaskSpecification<IssueTocCrawlTaskData> getCrawlerType() {
        return IucrIssueTocCrawlTask.INSTANCE;
    }

}
