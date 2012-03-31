package wwmm.pubcrawler.controller;

import com.google.inject.Injector;
import wwmm.pubcrawler.v2.crawler.CrawlRunner;
import wwmm.pubcrawler.v2.crawler.CrawlTask;

import javax.inject.Inject;

/**
 * @author Sam Adams
 */
public class CrawlerFactory {
    
    private final Injector injector;

    @Inject
    public CrawlerFactory(final Injector injector) {
        this.injector = injector;
    }

    public CrawlRunner createCrawler(final CrawlTask task) {
        final Class<? extends CrawlRunner> type = task.getTaskClass();
        return injector.getInstance(type);
    }
    
}
