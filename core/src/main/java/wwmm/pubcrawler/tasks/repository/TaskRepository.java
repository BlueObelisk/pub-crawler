package wwmm.pubcrawler.tasks.repository;

import wwmm.pubcrawler.crawler.Task;

import java.util.List;

/**
 * @author Sam Adams
 */
public interface TaskRepository {

    Task getTask(String taskId);

    boolean updateTask(Task task);

    List<String> getWaitingTaskIds(final String filter);

    List<Task> getNextQueuedTaskBatch(long now, int batchSize);

    void rescheduleTask(String taskId, long timestamp);
}
