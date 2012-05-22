package wwmm.pubcrawler.http;

import wwmm.pubcrawler.tasks.HttpCrawlTaskData;

/**
 * @author Sam Adams
 */
public interface RequestFactory<T> {

    T createFetchTask(String taskId, HttpCrawlTaskData data);

}
