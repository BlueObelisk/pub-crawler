package wwmm.pubcrawler.crawler;

/**
 * @author Sam Adams
 */
public interface TaskQueue {

    Task nextTask();

    void queueTask(Task task);

    void resumeTasks(String filter);
}
