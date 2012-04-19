package wwmm.pubcrawler.crawlers.elsevier;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Before;
import org.junit.Test;
import wwmm.pubcrawler.MockPubCrawlerModule;
import wwmm.pubcrawler.MockRepositoryModule;
import wwmm.pubcrawler.crawlers.elsevier.tasks.ElsevierBibliographyCrawlSeedTask;
import wwmm.pubcrawler.crawlers.elsevier.tasks.ElsevierIssueTocCrawlTask;
import wwmm.pubcrawler.crawlers.elsevier.tasks.ElsevierPublicationListCrawlTask;

import static org.junit.Assert.assertNotNull;

/**
 * @author Sam Adams
 */
public class ElsevierCrawlerModuleTest {

    @Before
    public void setUp() throws Exception {
        injector = Guice.createInjector(
            new ElsevierCrawlerModule(),
            new MockRepositoryModule(),
            new MockPubCrawlerModule()
        );
    }

    private Injector injector;

    @Test
    public void testCanCreateIssueTocParserFactory() {
        ElsevierIssueTocParserFactory factory = injector.getInstance(ElsevierIssueTocParserFactory.class);
        assertNotNull(factory);
    }

    @Test
    public void testCanCreatePublicationListParserFactory() {
        ElsevierPublicationListParserFactory factory = injector.getInstance(ElsevierPublicationListParserFactory.class);
        assertNotNull(factory);
    }

    @Test
    public void testCanCreateBibliographyCrawlSeedTask() {
        ElsevierBibliographyCrawlSeedTask crawlTask = injector.getInstance(ElsevierBibliographyCrawlSeedTask.class);
        assertNotNull(crawlTask);
    }

    @Test
    public void testCanCreatePublicationListCrawlTask() {
        ElsevierPublicationListCrawlTask crawlTask = injector.getInstance(ElsevierPublicationListCrawlTask.class);
        assertNotNull(crawlTask);
    }

    @Test
    public void testCanCreateIssueCrawlTask() {
        ElsevierIssueTocCrawlTask crawlTask = injector.getInstance(ElsevierIssueTocCrawlTask.class);
        assertNotNull(crawlTask);
    }
    
}
