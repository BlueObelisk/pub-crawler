package wwmm.pubcrawler.main;

import com.google.inject.Module;
import wwmm.pubcrawler.crawlers.acta.IucrCrawlerModule;
import wwmm.pubcrawler.crawlers.acta.tasks.IucrBibliographyCrawlSeedTask;

/**
 * @author Sam Adams
 */
public class IucrBibliographyCrawlerApplication extends CrawlerApplication {

    @Override
    protected Class<? extends Runnable> getSeederType() {
        return IucrBibliographyCrawlSeedTask.class;
    }

    @Override
    protected Module getPublisherModule() {
        return new IucrCrawlerModule();
    }

    public static void main(final String[] args) throws Exception {
        new IucrBibliographyCrawlerApplication().run();
    }
}
