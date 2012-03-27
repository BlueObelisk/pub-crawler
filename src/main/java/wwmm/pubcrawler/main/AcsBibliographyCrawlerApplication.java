package wwmm.pubcrawler.main;

import com.google.inject.Module;
import wwmm.pubcrawler.crawlers.acs.tasks.AcsBibliographyCrawlSeedTask;
import wwmm.pubcrawler.v2.inject.crawlers.AcsCrawlerModule;

/**
 * @author Sam Adams
 */
public class AcsBibliographyCrawlerApplication extends CrawlerApplication {

    @Override
    protected Class<? extends Runnable> getSeederType() {
        return AcsBibliographyCrawlSeedTask.class;
    }

    @Override
    protected Module getPublisherModule() {
        return new AcsCrawlerModule();
    }

    public static void main(final String[] args) throws Exception {
        new AcsBibliographyCrawlerApplication().run();
    }
}
