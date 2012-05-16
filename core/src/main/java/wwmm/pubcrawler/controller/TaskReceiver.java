package wwmm.pubcrawler.controller;

import wwmm.pubcrawler.crawler.CrawlTask;

/**
 * @author Sam Adams
 */
public interface TaskReceiver {

    void task(CrawlTask task);

}
