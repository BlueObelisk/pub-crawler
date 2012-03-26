package wwmm.pubcrawler.main;

import wwmm.pubcrawler.crawlers.wiley.tasks.WileyBibliographyCrawlSeedTask;

/**
 * @author Sam Adams
 */
public class WileyBibliographyCrawlerApplication extends CrawlerApplication {

    @Override
    protected Class<? extends Runnable> getSeederType() {
        return WileyBibliographyCrawlSeedTask.class;
    }

    public static void main(final String[] args) throws Exception {
        new WileyBibliographyCrawlerApplication().run();
    }
}
