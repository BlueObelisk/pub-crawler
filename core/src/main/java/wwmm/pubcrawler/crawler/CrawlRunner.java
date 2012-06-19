package wwmm.pubcrawler.crawler;

/**
 * @author Sam Adams
 */
public interface CrawlRunner<T> {

    void run(String id, T data) throws Exception;

}
