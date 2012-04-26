package wwmm.pubcrawler.http;

import wwmm.pubcrawler.crawler.TaskData;

/**
 * @author Sam Adams
 */
public interface RequestFactory<T> {

    T createFetchTask(String taskId, TaskData data);

}
