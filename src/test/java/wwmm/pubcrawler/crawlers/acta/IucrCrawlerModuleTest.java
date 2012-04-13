package wwmm.pubcrawler.crawlers.acta;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Before;
import org.junit.Test;
import wwmm.pubcrawler.MockPubCrawlerModule;
import wwmm.pubcrawler.MockRepositoryModule;
import wwmm.pubcrawler.crawlers.acta.tasks.IucrBibliographyCrawlSeedTask;
import wwmm.pubcrawler.crawlers.acta.tasks.IucrIssueTocCrawlTask;
import wwmm.pubcrawler.crawlers.acta.tasks.IucrPublicationListCrawlTask;

import static org.junit.Assert.assertNotNull;

/**
 * @author Sam Adams
 */
public class IucrCrawlerModuleTest {

    @Before
    public void setUp() throws Exception {
        injector = Guice.createInjector(
            new IucrCrawlerModule(),
            new MockRepositoryModule(),
            new MockPubCrawlerModule()
        );
    }

    private Injector injector;

    @Test
    public void testCanCreateIssueTocParserFactory() {
        IucrIssueTocParserFactory factory = injector.getInstance(IucrIssueTocParserFactory.class);
        assertNotNull(factory);
    }

    @Test
    public void testCanCreatePublicationListParserFactory() {
        IucrPublicationListParserFactory factory = injector.getInstance(IucrPublicationListParserFactory.class);
        assertNotNull(factory);
    }

    @Test
    public void testCanCreateBibliographyCrawlSeedTask() {
        IucrBibliographyCrawlSeedTask crawlTask = injector.getInstance(IucrBibliographyCrawlSeedTask.class);
        assertNotNull(crawlTask);
    }

    @Test
    public void testCanCreatePublicationListCrawlTask() {
        IucrPublicationListCrawlTask crawlTask = injector.getInstance(IucrPublicationListCrawlTask.class);
        assertNotNull(crawlTask);
    }

    @Test
    public void testCanCreateIssueCrawlTask() {
        IucrIssueTocCrawlTask crawlTask = injector.getInstance(IucrIssueTocCrawlTask.class);
        assertNotNull(crawlTask);
    }
    
}
