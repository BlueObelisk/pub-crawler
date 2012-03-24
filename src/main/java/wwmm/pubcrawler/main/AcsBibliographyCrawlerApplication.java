package wwmm.pubcrawler.main;

import wwmm.pubcrawler.crawlers.acs.tasks.AcsBibliographyCrawlSeedTask;

/**
 * @author Sam Adams
 */
public class AcsBibliographyCrawlerApplication extends CrawlerApplication {

    @Override
    protected Class<? extends Runnable> getSeederType() {
        return AcsBibliographyCrawlSeedTask.class;
    }

    public static void main(final String[] args) throws Exception {
        new AcsBibliographyCrawlerApplication().run();
    }
}
