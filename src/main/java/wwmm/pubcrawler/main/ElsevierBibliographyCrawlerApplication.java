package wwmm.pubcrawler.main;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mongodb.DB;
import com.mongodb.Mongo;
import wwmm.pubcrawler.controller.CrawlerExecutor;
import wwmm.pubcrawler.v2.inject.HttpFetcherModule;
import wwmm.pubcrawler.v2.inject.MongoRepositoryModule;
import wwmm.pubcrawler.v2.inject.PubcrawlerModule;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Sam Adams
 */
public class ElsevierBibliographyCrawlerApplication {

    public static void main(final String[] args) throws Exception {

        final String host = System.getProperty("pubcrawler.mongo.host", "localhost");
        final String dbname = System.getProperty("pubcrawler.mongo.dbname", "pctest");

        final Mongo mongo = new Mongo(host);
        final DB db = mongo.getDB(dbname);

        final Injector injector = Guice.createInjector(
            new PubcrawlerModule(),
            new HttpFetcherModule(db),
            new MongoRepositoryModule(db)
        );

        final ExecutorService executorService = Executors.newSingleThreadExecutor();

        final ElsevierBibliographyCrawlSeeder seeder = injector.getInstance(ElsevierBibliographyCrawlSeeder.class);
        executorService.execute(seeder);

        final CrawlerExecutor crawlRunner = injector.getInstance(CrawlerExecutor.class);
        executorService.execute(crawlRunner);

        // Wait for CrawlRunner to complete and stop executor service
        executorService.shutdown();
    }
    
}
