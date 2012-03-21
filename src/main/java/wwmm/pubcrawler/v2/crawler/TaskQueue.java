package wwmm.pubcrawler.v2.crawler;

/**
 * @author Sam Adams
 */
public interface TaskQueue {

    CrawlTask nextTask();

    void queueTask(CrawlTask task);

}
