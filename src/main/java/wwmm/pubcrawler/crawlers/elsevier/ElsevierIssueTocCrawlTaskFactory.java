package wwmm.pubcrawler.crawlers.elsevier;

import wwmm.pubcrawler.crawlers.AbstractIssueTocCrawlTaskFactory;
import wwmm.pubcrawler.crawlers.elsevier.tasks.ElsevierIssueTocCrawlTask;
import wwmm.pubcrawler.crawler.CrawlRunner;

import javax.inject.Singleton;

/**
 * @author Sam Adams
 */
@Singleton
public class ElsevierIssueTocCrawlTaskFactory extends AbstractIssueTocCrawlTaskFactory {

    @Override
    protected Class<? extends CrawlRunner> getCrawlerType() {
        return ElsevierIssueTocCrawlTask.class;
    }

}
