package wwmm.pubcrawler.crawlers.wiley;

import com.google.inject.Guice;
import com.google.inject.Injector;
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

    @Before
    public void setUp() throws Exception {
        injector = Guice.createInjector(
            new WileyCrawlerModule(),
            new MockRepositoryModule(),
            new MockPubCrawlerModule()
        );
    }

    private Injector injector;

    @Test
    public void testCanCreateIssueTocParserFactory() {
        WileyIssueTocParserFactory factory = injector.getInstance(WileyIssueTocParserFactory.class);
        assertNotNull(factory);
    }

    @Test
    public void testCanCreatePublicationListParserFactory() {
        WileyPublicationListParserFactory factory = injector.getInstance(WileyPublicationListParserFactory.class);
        assertNotNull(factory);
    }

    @Test
    public void testCanCreateBibliographyCrawlSeedTask() {
        WileyBibliographyCrawlSeedTask crawlTask = injector.getInstance(WileyBibliographyCrawlSeedTask.class);
        assertNotNull(crawlTask);
    }

    @Test
    public void testCanCreatePublicationListCrawlTask() {
        WileyPublicationListCrawlTask crawlTask = injector.getInstance(WileyPublicationListCrawlTask.class);
        assertNotNull(crawlTask);
    }

    @Test
    public void testCanCreateIssueCrawlTask() {
        WileyIssueTocCrawlTask crawlTask = injector.getInstance(WileyIssueTocCrawlTask.class);
        assertNotNull(crawlTask);
    }
    
}
