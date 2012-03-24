package wwmm.pubcrawler.main;

import wwmm.pubcrawler.crawlers.elsevier.tasks.ElsevierBibliographyCrawlSeedTask;

/**
 * @author Sam Adams
 */
public class ElsevierBibliographyCrawlerApplication extends CrawlerApplication {

    @Override
    protected Class<? extends Runnable> getSeederType() {
        return ElsevierBibliographyCrawlSeedTask.class;
    }

    public static void main(final String[] args) throws Exception {
        new ElsevierBibliographyCrawlerApplication().run();
    }
}
