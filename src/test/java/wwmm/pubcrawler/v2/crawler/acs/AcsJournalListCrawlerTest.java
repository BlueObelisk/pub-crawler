package wwmm.pubcrawler.v2.crawler.acs;

import org.junit.Before;
import org.junit.Test;
import uk.ac.cam.ch.wwmm.httpcrawler.HttpFetcher;
import wwmm.pubcrawler.crawlers.acs.tasks.AcsJournalListCrawler;
import wwmm.pubcrawler.crawlers.acs.tasks.AcsParserFactory;
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
    private AcsParserFactory parserFactory;
    private HttpFetcher fetcher;

    private AcsJournalListCrawler crawler;

    @Before
    public void setUp() throws Exception {
        taskQueue = mock(TaskQueue.class);
        journalRepo = mock(JournalRepository.class);
        parserFactory = mock(AcsParserFactory.class);
        fetcher = mock(HttpFetcher.class);

        crawler = new AcsJournalListCrawler(taskQueue, journalRepo, fetcher);
    }

    @Test
    public void testCrawlJournalList() throws Exception {
        HttpResource data = new HttpResource("acs:journal-list.html", URI.create(""));
//        crawler.run(data);
    }
}
