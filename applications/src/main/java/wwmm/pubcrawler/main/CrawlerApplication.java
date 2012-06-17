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
import wwmm.pubserver.archiver.ArchiveProcessor;
import wwmm.pubserver.archiver.PubserverArchiverModule;

import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Sam Adams
 */
public abstract class CrawlerApplication {
    
    public void run() throws UnknownHostException {
        final String host = System.getProperty("pubcrawler.mongo.host", "localhost");
        final String pubDbName = System.getProperty("pubcrawler.mongo.bibdb", "bib");
        final String taskDbName = System.getProperty("pubcrawler.mongo.taskdb", "task");
        final String httpDbName = System.getProperty("pubcrawler.mongo.httpdb", "http");

        final Mongo mongo = new Mongo(host);
        final DB pubdb = mongo.getDB(pubDbName);
        final DB taskdb = mongo.getDB(taskDbName);
        final DB httpdb = mongo.getDB(httpDbName);

        final Injector injector = Guice.createInjector(
            new PubcrawlerModule(),
            new HttpFetcherModule(httpdb),
            new MongoRepositoryModule(pubdb, taskdb),
            new PubserverArchiverModule(),
            getPublisherModule()
        );

        final ExecutorService taskExecutor = Executors.newSingleThreadExecutor();
        final ExecutorService archiveExecutor = Executors.newSingleThreadExecutor();

        final ArchiveProcessor archiveProcessor = injector.getInstance(ArchiveProcessor.class);
        archiveExecutor.submit(archiveProcessor);

        final Class<? extends Runnable> seederType = getSeederType();
        final Runnable seedRunner = injector.getInstance(seederType);
        taskExecutor.submit(seedRunner);

        final Runnable resumeRunner = injector.getInstance(ResumeTask.class);
        taskExecutor.submit(resumeRunner);

        final CrawlerExecutor crawlRunner = injector.getInstance(CrawlerExecutor.class);
        taskExecutor.submit(crawlRunner);

        // Wait for CrawlRunner to complete and stop executor service
        taskExecutor.shutdown();
    }

    protected abstract Module getPublisherModule();

    protected abstract Class<? extends Runnable> getSeederType();
}
