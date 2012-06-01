package wwmm.pubcrawler.crawlers.springer;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import org.junit.Before;
import org.junit.Test;
import wwmm.pubcrawler.MockPubCrawlerModule;
import wwmm.pubcrawler.MockRepositoryModule;
import wwmm.pubcrawler.crawlers.springer.tasks.SpringerBibliographyCrawlSeedTask;
import wwmm.pubcrawler.crawlers.springer.tasks.SpringerIssueListCrawlTask;
import wwmm.pubcrawler.crawlers.springer.tasks.SpringerIssueTocCrawlTask;
import wwmm.pubcrawler.crawlers.springer.tasks.SpringerPublicationListCrawlTask;

import static org.junit.Assert.assertNotNull;

/**
 * @author Sam Adams
 */
public class SpringerCrawlerModuleTest {

    @Before
    public void setUp() throws Exception {
        injector = Guice.createInjector(Stage.PRODUCTION,
                                        new SpringerCrawlerModule(),
                                        new MockRepositoryModule(),
                                        new MockPubCrawlerModule()
        );
    }

    private Injector injector;

    @Test
    public void testCanCreateBibliographyCrawlSeedTask() {
        SpringerBibliographyCrawlSeedTask crawlTask = injector.getInstance(SpringerBibliographyCrawlSeedTask.class);
        assertNotNull(crawlTask);
    }

    @Test
    public void testCanCreatePublicationListCrawlRunner() {
        SpringerPublicationListCrawlTask.Runner crawlRunner = injector.getInstance(SpringerPublicationListCrawlTask.Runner.class);
        assertNotNull(crawlRunner);
    }

    @Test
    public void testCanCreateIssueListCrawlRunner() {
        SpringerIssueListCrawlTask.Runner crawlRunner = injector.getInstance(SpringerIssueListCrawlTask.Runner.class);
        assertNotNull(crawlRunner);
    }

    @Test
    public void testCanCreateIssueTocCrawlRunner() {
        SpringerIssueTocCrawlTask.Runner crawlRunner = injector.getInstance(SpringerIssueTocCrawlTask.Runner.class);
        assertNotNull(crawlRunner);
    }
}
