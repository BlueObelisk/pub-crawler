package wwmm.pubcrawler.v2.crawler;

import com.google.inject.Injector;

/**
 * @author Sam Adams
 */
public class CrawlerFactory {

    private final Injector injector;

    public CrawlerFactory(final Injector injector) {
        this.injector = injector;
    }

    public CrawlRunner createCrawler(final CrawlTask crawlTask) {
        final CrawlRunner crawler = injector.getInstance(crawlTask.getTaskClass());
        return crawler;
    }

}
