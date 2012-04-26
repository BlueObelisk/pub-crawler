package wwmm.pubcrawler;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.mockito.Mockito;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import wwmm.pubcrawler.crawlers.IssueHandler;
import wwmm.pubcrawler.crawlers.JournalHandler;
import wwmm.pubcrawler.crawler.TaskQueue;
import wwmm.pubcrawler.http.Fetcher;
import wwmm.pubcrawler.http.UriRequest;

/**
 * @author Sam Adams
 */
public class MockPubCrawlerModule extends AbstractModule {

    @Override
    protected void configure() {
    }
    
    @Provides
    public Fetcher<UriRequest, CrawlerResponse> provideFetcher() {
        return Mockito.mock(Fetcher.class);
    }

    @Provides
    public TaskQueue provideTaskQueue() {
        return Mockito.mock(TaskQueue.class);
    }

    @Provides
    public JournalHandler providesJournalHandler() {
        return Mockito.mock(JournalHandler.class);
    }
    
    @Provides
    public IssueHandler providesIssueHandler() {
        return Mockito.mock(IssueHandler.class);
    }
    
}
