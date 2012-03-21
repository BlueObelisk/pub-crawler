package wwmm.pubcrawler.v2.crawler;

import wwmm.pubcrawler.v2.repositories.TaskRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Deque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author Sam Adams
 */
@Singleton
public class DefaultTaskQueue implements TaskQueue {

    private final Deque<String> queue = new LinkedBlockingDeque<String>();
    private final TaskRepository taskRepository;

    @Inject
    public DefaultTaskQueue(final TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public CrawlTask nextTask() {
        final String taskId = queue.poll();
        return taskId != null ? fetchTask(taskId) : null;
    }

    private CrawlTask fetchTask(final String taskId) {
        return taskRepository.getTask(taskId);
    }

    @Override
    public void queueTask(final CrawlTask task) {
        taskRepository.updateTask(task);
        queue.add(task.getId());
    }
    
}
