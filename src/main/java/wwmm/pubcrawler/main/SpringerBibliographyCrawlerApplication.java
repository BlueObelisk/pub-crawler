package wwmm.pubcrawler.main;

import com.google.inject.Module;
import wwmm.pubcrawler.crawlers.springer.SpringerCrawlerModule;
import wwmm.pubcrawler.crawlers.springer.tasks.SpringerBibliographyCrawlSeedTask;

/**
 * @author Sam Adams
 */
public class SpringerBibliographyCrawlerApplication extends CrawlerApplication {

    @Override
    protected Class<? extends Runnable> getSeederType() {
        return SpringerBibliographyCrawlSeedTask.class;
    }

    @Override
    protected Module getPublisherModule() {
        return new SpringerCrawlerModule();
    }

    public static void main(final String[] args) throws Exception {
        new SpringerBibliographyCrawlerApplication().run();
    }
}
