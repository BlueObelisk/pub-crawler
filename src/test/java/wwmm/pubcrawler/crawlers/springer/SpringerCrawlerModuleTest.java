package wwmm.pubcrawler.crawlers.springer;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Before;
import org.junit.Test;
import wwmm.pubcrawler.MockPubCrawlerModule;
import wwmm.pubcrawler.MockRepositoryModule;
import wwmm.pubcrawler.crawlers.springer.tasks.SpringerBibliographyCrawlSeedTask;
import wwmm.pubcrawler.crawlers.springer.tasks.SpringerIssueTocCrawlTask;
import wwmm.pubcrawler.crawlers.springer.tasks.SpringerPublicationListCrawlTask;

import static org.junit.Assert.assertNotNull;

/**
 * @author Sam Adams
 */
public class SpringerCrawlerModuleTest {

    @Before
    public void setUp() throws Exception {
        injector = Guice.createInjector(
            new SpringerCrawlerModule(),
            new MockRepositoryModule(),
            new MockPubCrawlerModule()
        );
    }

    private Injector injector;

    @Test
    public void testCanCreateIssueTocParserFactory() {
        SpringerIssueTocParserFactory factory = injector.getInstance(SpringerIssueTocParserFactory.class);
        assertNotNull(factory);
    }

    @Test
    public void testCanCreatePublicationListParserFactory() {
        SpringerPublicationListParserFactory factory = injector.getInstance(SpringerPublicationListParserFactory.class);
        assertNotNull(factory);
    }

    @Test
    public void testCanCreateBibliographyCrawlSeedTask() {
        SpringerBibliographyCrawlSeedTask crawlTask = injector.getInstance(SpringerBibliographyCrawlSeedTask.class);
        assertNotNull(crawlTask);
    }

    @Test
    public void testCanCreatePublicationListCrawlTask() {
        SpringerPublicationListCrawlTask crawlTask = injector.getInstance(SpringerPublicationListCrawlTask.class);
        assertNotNull(crawlTask);
    }

    @Test
    public void testCanCreateIssueCrawlTask() {
        SpringerIssueTocCrawlTask crawlTask = injector.getInstance(SpringerIssueTocCrawlTask.class);
        assertNotNull(crawlTask);
    }
    
}
