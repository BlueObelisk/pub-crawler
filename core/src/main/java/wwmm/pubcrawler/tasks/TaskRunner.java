package wwmm.pubcrawler.tasks;

/**
 * @author Sam Adams
 */
public interface TaskRunner<T> {

    void run(String id, T data) throws Exception;

}
