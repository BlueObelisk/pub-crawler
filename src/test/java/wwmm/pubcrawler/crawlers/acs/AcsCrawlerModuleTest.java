package wwmm.pubcrawler.crawlers.acs;

import com.google.inject.Guice;
import com.google.inject.Injector;
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
        injector = Guice.createInjector(
            new AcsCrawlerModule(),
            new MockRepositoryModule(),
            new MockPubCrawlerModule()
        );
    }

    @Test
    public void testCanCreateIssueTocParserFactory() {
        AcsIssueTocParserFactory factory = injector.getInstance(AcsIssueTocParserFactory.class);
        assertNotNull(factory);
    }
    
    @Test
    public void testCanCreatePublicationListParserFactory() {
        AcsPublicationListParserFactory factory = injector.getInstance(AcsPublicationListParserFactory.class);
        assertNotNull(factory);
    }

    @Test
    public void testCanCreateBibliographyCrawlSeedTask() {
        AcsBibliographyCrawlSeedTask crawlTask = injector.getInstance(AcsBibliographyCrawlSeedTask.class);
        assertNotNull(crawlTask);
    }

    @Test
    public void testCanCreatePublicationListCrawlTask() {
        AcsPublicationListCrawlTask crawlTask = injector.getInstance(AcsPublicationListCrawlTask.class);
        assertNotNull(crawlTask);
    }

    @Test
    public void testCanCreateIssueCrawlTask() {
        AcsIssueTocCrawlTask crawlTask = injector.getInstance(AcsIssueTocCrawlTask.class);
        assertNotNull(crawlTask);
    }

}
