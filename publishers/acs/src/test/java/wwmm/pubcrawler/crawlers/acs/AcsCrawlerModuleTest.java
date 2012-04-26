package wwmm.pubcrawler.crawlers.acs;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Before;
import org.junit.Test;
import wwmm.pubcrawler.MockPubCrawlerModule;
import wwmm.pubcrawler.MockRepositoryModule;
import wwmm.pubcrawler.crawlers.IssueTocCrawlRunner;
import wwmm.pubcrawler.crawlers.PublicationListCrawlRunner;
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
    public void testCanCreatePublicationListCrawlRunner() {
        PublicationListCrawlRunner crawlRunner = injector.getInstance(PublicationListCrawlRunner.class);
        assertNotNull(crawlRunner);
    }

    @Test
    public void testCanCreateIssueTocCrawlRunner() {
        IssueTocCrawlRunner crawlRunner = injector.getInstance(IssueTocCrawlRunner.class);
        assertNotNull(crawlRunner);
    }
}
