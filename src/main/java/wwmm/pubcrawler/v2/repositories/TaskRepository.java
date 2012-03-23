package wwmm.pubcrawler.v2.repositories;

import wwmm.pubcrawler.v2.crawler.CrawlTask;

/**
 * @author Sam Adams
 */
public interface TaskRepository {

    CrawlTask getTask(String taskId);

    boolean updateTask(CrawlTask task);

}
