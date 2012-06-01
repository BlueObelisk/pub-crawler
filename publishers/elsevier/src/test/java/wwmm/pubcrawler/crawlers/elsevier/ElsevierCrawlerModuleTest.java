package wwmm.pubcrawler.crawlers.elsevier;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
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

    private Injector injector;

    @Before
    public void setUp() throws Exception {
        injector = Guice.createInjector(Stage.PRODUCTION,
                                        new ElsevierCrawlerModule(),
                                        new MockRepositoryModule(),
                                        new MockPubCrawlerModule()
        );
    }

    @Test
    public void testCanCreateBibliographyCrawlSeedTask() {
        ElsevierBibliographyCrawlSeedTask crawlTask = injector.getInstance(ElsevierBibliographyCrawlSeedTask.class);
        assertNotNull(crawlTask);
    }

    @Test
    public void testCanCreatePublicationListCrawlTaskRunner() {
        ElsevierPublicationListCrawlTask.Runner crawlTask = injector.getInstance(ElsevierPublicationListCrawlTask.Runner.class);
        assertNotNull(crawlTask);
    }

    @Test
    public void testCanCreateIssueTocCrawlTaskRunner() {
        ElsevierIssueTocCrawlTask.Runner crawlTask = injector.getInstance(ElsevierIssueTocCrawlTask.Runner.class);
        assertNotNull(crawlTask);
    }

}
