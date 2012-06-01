package wwmm.pubcrawler.crawlers.acs;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import org.junit.Before;
import org.junit.Test;
import wwmm.pubcrawler.MockPubCrawlerModule;
import wwmm.pubcrawler.MockRepositoryModule;
import wwmm.pubcrawler.crawlers.acs.tasks.AcsBibliographyCrawlSeedTask;
import wwmm.pubcrawler.crawlers.acs.tasks.AcsIssueTocCrawlTask;
import wwmm.pubcrawler.crawlers.acs.tasks.AcsPublicationListCrawlTask;

import static org.junit.Assert.assertNotNull;

/**
 * @author Sam Adams
 */
public class AcsCrawlerModuleTest {

    private Injector injector;

    @Before
    public void setUp() throws Exception {
        injector = Guice.createInjector(Stage.PRODUCTION,
                                        new AcsCrawlerModule(),
                                        new MockRepositoryModule(),
                                        new MockPubCrawlerModule()
        );
    }

    @Test
    public void testCanCreateBibliographyCrawlSeedTask() {
        AcsBibliographyCrawlSeedTask crawlTask = injector.getInstance(AcsBibliographyCrawlSeedTask.class);
        assertNotNull(crawlTask);
    }

    @Test
    public void testCanCreatePublicationListCrawlRunner() {
        AcsPublicationListCrawlTask.Runner crawlRunner = injector.getInstance(AcsPublicationListCrawlTask.Runner.class);
        assertNotNull(crawlRunner);
    }

    @Test
    public void testCanCreateIssueTocCrawlRunner() {
        AcsIssueTocCrawlTask.Runner crawlRunner = injector.getInstance(AcsIssueTocCrawlTask.Runner.class);
        assertNotNull(crawlRunner);
    }
}
