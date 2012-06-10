package wwmm.pubcrawler.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import uk.ac.cam.ch.wwmm.httpcrawler.HttpFetcher;
import wwmm.pubcrawler.http.Fetcher;
import wwmm.pubcrawler.http.TextResource;
import wwmm.pubcrawler.http.TextResourceHttpFetcher;
import wwmm.pubcrawler.http.UriRequest;
import wwmm.pubcrawler.crawler.DefaultTaskQueue;
import wwmm.pubcrawler.crawler.TaskQueue;

/**
 * @author Sam Adams
 */
public class PubcrawlerModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(TaskQueue.class).to(DefaultTaskQueue.class);
    }

    @Provides
    public Fetcher<UriRequest, TextResource> provideTextResourceFetcher(final HttpFetcher httpFetcher) {
        return new TextResourceHttpFetcher(httpFetcher);
    }
    
}
