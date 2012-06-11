package wwmm.pubcrawler.main;

import com.google.inject.Module;
import wwmm.pubcrawler.crawlers.nature.NatureCrawlerModule;
import wwmm.pubcrawler.crawlers.nature.tasks.NatureBibliographyCrawlSeedTask;

/**
 * @author Sam Adams
 */
public class NatureBibliographyCrawlerApplication extends CrawlerApplication {

    @Override
    protected Class<? extends Runnable> getSeederType() {
        return NatureBibliographyCrawlSeedTask.class;
    }

    @Override
    protected Module getPublisherModule() {
        return new NatureCrawlerModule();
    }

    public static void main(final String[] args) throws Exception {
        new NatureBibliographyCrawlerApplication().run();
    }
}