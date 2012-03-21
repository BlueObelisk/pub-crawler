package wwmm.pubcrawler.v2;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mongodb.DB;
import com.mongodb.Mongo;
import wwmm.pubcrawler.crawlers.acs.tasks.AcsCrawlerApplication;
import wwmm.pubcrawler.v2.inject.MongoRepositoryModule;
import wwmm.pubcrawler.v2.inject.PubcrawlerModule;

import java.net.UnknownHostException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author Sam Adams
 */
public class CrawlerApplication {

    public static void main(final String[] args) throws UnknownHostException {

        final DB db = new Mongo("localhost").getDB("testdb");
        final Injector injector = Guice.createInjector(
            new MongoRepositoryModule(db), new PubcrawlerModule(db)
        );

        final Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(injector.getInstance(AcsCrawlerApplication.class));

    }
    
}
