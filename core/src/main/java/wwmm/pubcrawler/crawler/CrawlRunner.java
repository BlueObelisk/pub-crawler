package wwmm.pubcrawler.crawler;

/**
 * @author Sam Adams
 */
public interface CrawlRunner<Data> {

    void run(String id, Data data) throws Exception;

}
