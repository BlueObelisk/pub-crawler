package wwmm.pubcrawler.crawlers.elsevier;

import wwmm.pubcrawler.crawlers.AbstractIssueTocCrawlTaskFactory;
import wwmm.pubcrawler.crawlers.elsevier.tasks.ElsevierIssueTocCrawlTask;
import wwmm.pubcrawler.tasks.IssueTocCrawlTaskData;
import wwmm.pubcrawler.tasks.TaskSpecification;

import javax.inject.Singleton;

/**
 * @author Sam Adams
 */
@Singleton
public class ElsevierIssueTocCrawlTaskFactory extends AbstractIssueTocCrawlTaskFactory {

    @Override
    protected TaskSpecification<IssueTocCrawlTaskData> getCrawlerType() {
        return ElsevierIssueTocCrawlTask.INSTANCE;
    }

}
