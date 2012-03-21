package wwmm.pubcrawler.v2.crawler;

/**
 * @author Sam Adams
 */
public interface CrawlRunner {

    void run(String id, TaskData data) throws Exception;

}
