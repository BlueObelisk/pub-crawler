package wwmm.pubcrawler.crawlers.wiley;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import org.junit.Before;
import org.junit.Test;
import wwmm.pubcrawler.MockPubCrawlerModule;
import wwmm.pubcrawler.MockRepositoryModule;
import wwmm.pubcrawler.crawlers.wiley.tasks.WileyBibliographyCrawlSeedTask;
import wwmm.pubcrawler.crawlers.wiley.tasks.WileyIssueTocCrawlTask;
import wwmm.pubcrawler.crawlers.wiley.tasks.WileyPublicationListCrawlTask;

import static org.junit.Assert.assertNotNull;

/**
 * @author Sam Adams
 */
public class WileyCrawlerModuleTest {

    private Injector injector;

    @Before
    public void setUp() throws Exception {
        injector = Guice.createInjector(Stage.PRODUCTION,
                                        new WileyCrawlerModule(),
                                        new MockRepositoryModule(),
                                        new MockPubCrawlerModule()
        );
    }

    @Test
    public void testCanCreateBibliographyCrawlSeedTask() {
        WileyBibliographyCrawlSeedTask crawlTask = injector.getInstance(WileyBibliographyCrawlSeedTask.class);
        assertNotNull(crawlTask);
    }

    @Test
    public void testCanCreatePublicationListCrawlRunner() {
        WileyPublicationListCrawlTask.Runner crawlRunner = injector.getInstance(WileyPublicationListCrawlTask.Runner.class);
        assertNotNull(crawlRunner);
    }

    @Test
    public void testCanCreateIssueTocCrawlRunner() {
        WileyIssueTocCrawlTask.Runner crawlRunner = injector.getInstance(WileyIssueTocCrawlTask.Runner.class);
        assertNotNull(crawlRunner);
    }
}
