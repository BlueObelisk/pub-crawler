package wwmm.pubcrawler.crawler;

/**
 * @author Sam Adams
 */
public interface TaskQueue {

    CrawlTask nextTask();

    void queueTask(CrawlTask task);

    void resumeTasks(String filter);
}
