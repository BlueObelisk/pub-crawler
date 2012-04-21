package wwmm.pubcrawler.repositories;

import wwmm.pubcrawler.crawler.CrawlTask;

import java.util.List;

/**
 * @author Sam Adams
 */
public interface TaskRepository {

    CrawlTask getTask(String taskId);

    boolean updateTask(CrawlTask task);

    List<String> getWaitingTaskIds(final String filter);

    List<CrawlTask> getNextQueuedTaskBatch(long now, int batchSize);
}
