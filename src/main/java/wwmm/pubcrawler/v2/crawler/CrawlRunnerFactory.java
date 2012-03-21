package wwmm.pubcrawler.v2.crawler;

import com.google.inject.Injector;

import javax.inject.Inject;

/**
 * @author Sam Adams
 */
public class CrawlRunnerFactory {
    
    private final Injector injector;

    @Inject
    public CrawlRunnerFactory(final Injector injector) {
        this.injector = injector;
    }
    
    public CrawlRunner createCrawlRunner(final CrawlTask task) {
        return injector.getInstance(task.getTaskClass());
    }

}
