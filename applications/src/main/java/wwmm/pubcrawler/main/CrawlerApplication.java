package wwmm.pubcrawler.main;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.mongodb.DB;
import com.mongodb.Mongo;
import wwmm.pubcrawler.controller.CrawlerExecutor;
import wwmm.pubcrawler.controller.ResumeTask;
import wwmm.pubcrawler.inject.HttpFetcherModule;
import wwmm.pubcrawler.inject.MongoRepositoryModule;
import wwmm.pubcrawler.inject.PubcrawlerModule;

import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Sam Adams
 */
public abstract class CrawlerApplication {
    
    public void run() throws UnknownHostException {
        final String host = System.getProperty("pubcrawler.mongo.host", "localhost");
        final String pubDbName = System.getProperty("pubcrawler.mongo.bibdb", "bibdata");
        final String taskDbName = System.getProperty("pubcrawler.mongo.taskdb", "bibdata");
        final String httpDbName = System.getProperty("pubcrawler.mongo.httpdb", "http");

        final Mongo mongo = new Mongo(host);
        final DB pubdb = mongo.getDB(pubDbName);
        final DB taskdb = mongo.getDB(taskDbName);
        final DB httpdb = mongo.getDB(httpDbName);

        final Injector injector = Guice.createInjector(
            new PubcrawlerModule(),
            new HttpFetcherModule(httpdb),
            new MongoRepositoryModule(pubdb, taskdb),
            getPublisherModule()
        );

        final ExecutorService executorService = Executors.newSingleThreadExecutor();

        if (System.getProperty("resume") != null) {
            final Runnable seeder = injector.getInstance(ResumeTask.class);
            executorService.execute(seeder);
        } else {
            final Class<? extends Runnable> seederType = getSeederType();
            final Runnable seeder = injector.getInstance(seederType);
            executorService.execute(seeder);
        }

        final CrawlerExecutor crawlRunner = injector.getInstance(CrawlerExecutor.class);
        executorService.execute(crawlRunner);

        // Wait for CrawlRunner to complete and stop executor service
        executorService.shutdown();
    }

    protected abstract Module getPublisherModule();

    protected abstract Class<? extends Runnable> getSeederType();

}
