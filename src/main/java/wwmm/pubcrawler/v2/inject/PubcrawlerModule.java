package wwmm.pubcrawler.v2.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.mongodb.DB;
import com.mongodb.Mongo;
import uk.ac.cam.ch.wwmm.httpcrawler.DefaultHttpFetcher;
import uk.ac.cam.ch.wwmm.httpcrawler.HttpFetcher;
import uk.ac.cam.ch.wwmm.httpcrawler.HttpFetcherBuilder;
import uk.ac.cam.ch.wwmm.httpcrawler.audit.RequestAuditor;
import uk.ac.cam.ch.wwmm.httpcrawler.cache.HttpCache;
import uk.ac.cam.ch.wwmm.httpcrawler.mongodb.MongoCache;
import uk.ac.cam.ch.wwmm.httpcrawler.mongodb.MongoRequestAuditor;
import wwmm.pubcrawler.v2.crawler.DefaultTaskQueue;
import wwmm.pubcrawler.v2.crawler.TaskQueue;

import javax.inject.Singleton;
import java.net.UnknownHostException;

/**
 * @author Sam Adams
 */
public class PubcrawlerModule extends AbstractModule {

    private final DB database;

    public PubcrawlerModule(final DB database) {
        this.database = database;
    }

    @Override
    protected void configure() {
        bind(TaskQueue.class).to(DefaultTaskQueue.class);
    }
    
    @Provides @Singleton
    public HttpFetcher getHttpFetcher() throws UnknownHostException {
        HttpFetcher fetcher = new HttpFetcherBuilder()
                                .withRequestAuditor(getAuditor())
                                .withUserAgent("pubcrawler")
                                .withCache(getCache())
                                .build();
        return fetcher;
    }

    private HttpCache getCache() {
        return new MongoCache(database, "cache");
    }

    private RequestAuditor getAuditor() throws UnknownHostException {
        return new MongoRequestAuditor(database.getCollection("audit"));
    }
    
}
