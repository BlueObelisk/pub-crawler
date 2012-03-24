package wwmm.pubcrawler.v2.crawler.acs;

import org.junit.Before;
import org.junit.Test;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import wwmm.pubcrawler.controller.BasicHttpFetcher;
import wwmm.pubcrawler.controller.Fetcher;
import wwmm.pubcrawler.controller.URITask;
import wwmm.pubcrawler.crawlers.acs.tasks.AcsPublicationListCrawlTask;
import wwmm.pubcrawler.v2.crawler.TaskQueue;
import wwmm.pubcrawler.v2.fetcher.HttpResource;
import wwmm.pubcrawler.v2.repositories.JournalRepository;

import java.net.URI;

import static org.mockito.Mockito.mock;

/**
 * @author Sam Adams
 */
public class AcsJournalListCrawlerTest {

    private TaskQueue taskQueue;
    private JournalRepository journalRepo;
    private Fetcher<URITask,CrawlerResponse> fetcher;

    private AcsPublicationListCrawlTask crawler;

    @Before
    public void setUp() throws Exception {
        taskQueue = mock(TaskQueue.class);
        journalRepo = mock(JournalRepository.class);
        fetcher = mock(BasicHttpFetcher.class);

        crawler = new AcsPublicationListCrawlTask(fetcher, taskQueue);
    }

    @Test
    public void testCrawlJournalList() throws Exception {
        HttpResource data = new HttpResource("acs:journal-list.html", URI.create(""));
//        crawler.run(data);
    }
}
