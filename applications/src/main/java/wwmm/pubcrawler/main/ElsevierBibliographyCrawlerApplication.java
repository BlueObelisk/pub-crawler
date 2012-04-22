package wwmm.pubcrawler.main;

import com.google.inject.Module;
import wwmm.pubcrawler.crawlers.elsevier.ElsevierCrawlerModule;
import wwmm.pubcrawler.crawlers.elsevier.tasks.ElsevierBibliographyCrawlSeedTask;

/**
 * @author Sam Adams
 */
public class ElsevierBibliographyCrawlerApplication extends CrawlerApplication {

    @Override
    protected Class<? extends Runnable> getSeederType() {
        return ElsevierBibliographyCrawlSeedTask.class;
    }

    @Override
    protected Module getPublisherModule() {
        return new ElsevierCrawlerModule();
    }

    public static void main(final String[] args) throws Exception {
        new ElsevierBibliographyCrawlerApplication().run();
    }

}
