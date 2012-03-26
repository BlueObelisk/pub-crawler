package wwmm.pubcrawler.v2.repositories;

import wwmm.pubcrawler.v2.crawler.CrawlTask;

import java.util.List;

/**
 * @author Sam Adams
 */
public interface TaskRepository {

    CrawlTask getTask(String taskId);

    boolean updateTask(CrawlTask task);

    List<String> getWaitingTaskIds(final String filter);
}
