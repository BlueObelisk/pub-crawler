package wwmm.pubcrawler.controller;

import wwmm.pubcrawler.crawler.Task;

/**
 * @author Sam Adams
 */
public interface TaskReceiver {

    void task(Task task);

}
