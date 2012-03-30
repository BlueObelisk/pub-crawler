package wwmm.pubcrawler.v2.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.mongodb.DB;
import uk.ac.cam.ch.wwmm.httpcrawler.HttpFetcher;
import uk.ac.cam.ch.wwmm.httpcrawler.HttpFetcherBuilder;
import uk.ac.cam.ch.wwmm.httpcrawler.audit.RequestAuditor;
import uk.ac.cam.ch.wwmm.httpcrawler.cache.HttpCache;
import uk.ac.cam.ch.wwmm.httpcrawler.mongodb.MongoCache;
import uk.ac.cam.ch.wwmm.httpcrawler.mongodb.MongoRequestAuditor;
import wwmm.pubcrawler.v2.crawler.DefaultTaskQueue;
import wwmm.pubcrawler.v2.crawler.TaskQueue;

import javax.inject.Singleton;
import java.net.URI;
import java.net.UnknownHostException;

/**
 * @author Sam Adams
 */
public class HttpFetcherModule extends AbstractModule {

    private final DB database;

    public HttpFetcherModule(final DB database) {
        this.database = database;
    }

    @Override
    protected void configure() {
    }
    
    @Provides @Singleton
    public HttpFetcher getHttpFetcher() throws UnknownHostException {
        final HttpFetcherBuilder builder = new HttpFetcherBuilder()
                                .withRequestAuditor(getAuditor())
                                .withUserAgent("pubcrawler/1.0")
                                .withCache(getCache());
        
        if (System.getProperty("http.proxy") != null) {
            final URI proxy = URI.create(System.getProperty("http.proxy"));
            System.out.println(" *** HTTP Proxy: " + proxy);
            builder.withProxy(proxy.getHost(), proxy.getPort());
        }
        
        return builder.build();
    }

    private HttpCache getCache() {
        return new MongoCache(database, "cache");
    }

    private RequestAuditor getAuditor() throws UnknownHostException {
        return new MongoRequestAuditor(database.getCollection("audit"));
    }
    
}
