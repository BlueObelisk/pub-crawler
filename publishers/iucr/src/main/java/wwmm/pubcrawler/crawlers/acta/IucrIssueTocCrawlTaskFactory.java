package wwmm.pubcrawler.crawlers.acta;

import wwmm.pubcrawler.crawler.Task;
import wwmm.pubcrawler.crawlers.AbstractIssueTocCrawlTaskFactory;
import wwmm.pubcrawler.crawlers.acta.tasks.IucrIssueTocCrawlTask;
import wwmm.pubcrawler.model.Journal;
import wwmm.pubcrawler.tasks.IssueTocCrawlTaskData;
import wwmm.pubcrawler.tasks.TaskSpecification;

/**
 * @author Sam Adams
 */
public class IucrIssueTocCrawlTaskFactory extends AbstractIssueTocCrawlTaskFactory {

    @Override
    public Task<IssueTocCrawlTaskData> createCurrentIssueTocCrawlTask(final Journal journal) {
        // Have to use issue list to find current issue
        throw new UnsupportedOperationException();
    }

    @Override
    protected TaskSpecification<IssueTocCrawlTaskData> getCrawlerType() {
        return IucrIssueTocCrawlTask.INSTANCE;
    }

}
