package wwmm.pubcrawler.main;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mongodb.DB;
import com.mongodb.Mongo;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import uk.ac.cam.ch.wwmm.httpcrawler.HttpFetcher;
import wwmm.pubcrawler.inject.HttpFetcherModule;

import java.io.*;
import java.net.UnknownHostException;

/**
 * @author Sam Adams
 */
public class Export {

    public static HttpFetcher getFetcher() throws UnknownHostException {
        final String host = System.getProperty("pubcrawler.mongo.host", "localhost");
        final String httpDbName = System.getProperty("pubcrawler.mongo.httpdb", "http");

        final Mongo mongo = new Mongo(host);
        final DB httpdb = mongo.getDB(httpDbName);

        final Injector injector = Guice.createInjector(
            new HttpFetcherModule(httpdb)
        );

        return injector.getInstance(HttpFetcher.class);
    }

    public static void main(final String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String key = in.readLine();

        HttpFetcher fetcher = getFetcher();
        CrawlerResponse response = fetcher.fetchFromCache(key);
        if (response != null) {
            PrintStream out = new PrintStream(new File("export.html"), "UTF-8");
            out.print(response.getEntityAsString());
            out.close();
        }
    }
    
}
