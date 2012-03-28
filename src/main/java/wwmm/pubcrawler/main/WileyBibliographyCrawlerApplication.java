package wwmm.pubcrawler.main;

import com.google.inject.Module;
import wwmm.pubcrawler.crawlers.wiley.WileyCrawlerModule;
import wwmm.pubcrawler.crawlers.wiley.tasks.WileyBibliographyCrawlSeedTask;

/**
 * @author Sam Adams
 */
public class WileyBibliographyCrawlerApplication extends CrawlerApplication {

    @Override
    protected Class<? extends Runnable> getSeederType() {
        return WileyBibliographyCrawlSeedTask.class;
    }

    @Override
    protected Module getPublisherModule() {
        return new WileyCrawlerModule();
    }

    public static void main(final String[] args) throws Exception {
        new WileyBibliographyCrawlerApplication().run();
    }
}
