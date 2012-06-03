package wwmm.pubcrawler.crawler;

import wwmm.pubcrawler.tasks.repository.TaskRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Deque;
import java.util.List;
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
    public Task nextTask() {
        final String taskId = queue.poll();
        return taskId != null ? fetchTask(taskId) : null;
    }

    private Task fetchTask(final String taskId) {
        return taskRepository.getTask(taskId);
    }

    @Override
    public void queueTask(final Task task) {
        if (taskRepository.updateTask(task)) {
            queue.add(task.getId());
        }
    }

    @Override
    public void resumeTasks(final String filter) {
        final List<String> taskIds = taskRepository.getWaitingTaskIds(filter);
        System.err.println("Queuing " + taskIds.size() + " tasks");
        queue.addAll(taskIds);
    }
}
