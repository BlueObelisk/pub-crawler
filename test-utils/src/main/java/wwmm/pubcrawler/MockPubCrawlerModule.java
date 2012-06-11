package wwmm.pubcrawler;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.mockito.Mockito;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import uk.ac.cam.ch.wwmm.httpcrawler.HttpFetcher;
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
    public HttpFetcher provideHttpFetcher() {
        return Mockito.mock(HttpFetcher.class);
    }
    
    @Provides
    public Fetcher<UriRequest, CrawlerResponse> provideFetcher() {
        return Mockito.mock(Fetcher.class);
    }

    @Provides
    public TaskQueue provideTaskQueue() {
        return Mockito.mock(TaskQueue.class);
    }

}
