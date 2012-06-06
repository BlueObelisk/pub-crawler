package wwmm.pubcrawler.main;

import com.google.inject.Module;
import wwmm.pubcrawler.crawlers.nature.NatureCrawlerModule;
import wwmm.pubcrawler.crawlers.nature.tasks.NatureBibliographyCrawlSeedTask;
import wwmm.pubcrawler.crawlers.rsc.RscCrawlerModule;
import wwmm.pubcrawler.crawlers.rsc.tasks.RscBibliographyCrawlSeedTask;

/**
 * @author Sam Adams
 */
public class RscBibliographyCrawlerApplication extends CrawlerApplication {

    @Override
    protected Class<? extends Runnable> getSeederType() {
        return RscBibliographyCrawlSeedTask.class;
    }

    @Override
    protected Module getPublisherModule() {
        return new RscCrawlerModule();
    }

    public static void main(final String[] args) throws Exception {
        new RscBibliographyCrawlerApplication().run();
    }
}
