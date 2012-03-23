package wwmm.pubcrawler.main;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mongodb.DB;
import com.mongodb.Mongo;
import wwmm.pubcrawler.controller.CrawlerExecutor;
import wwmm.pubcrawler.crawlers.elsevier.tasks.ElsevierBibliographyCrawlSeedTask;
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
        final String pubDbName = System.getProperty("pubcrawler.mongo.bibdb", "bibdata");
        final String httpDbName = System.getProperty("pubcrawler.mongo.httpdb", "http");

        final Mongo mongo = new Mongo(host);
        final DB pubdb = mongo.getDB(pubDbName);
        final DB httpdb = mongo.getDB(httpDbName);

        final Injector injector = Guice.createInjector(
            new PubcrawlerModule(),
            new HttpFetcherModule(httpdb),
            new MongoRepositoryModule(pubdb)
        );

        final ExecutorService executorService = Executors.newSingleThreadExecutor();

        final ElsevierBibliographyCrawlSeedTask seeder = injector.getInstance(ElsevierBibliographyCrawlSeedTask.class);
        executorService.execute(seeder);

        final CrawlerExecutor crawlRunner = injector.getInstance(CrawlerExecutor.class);
        executorService.execute(crawlRunner);

        // Wait for CrawlRunner to complete and stop executor service
        executorService.shutdown();
    }
    
}
