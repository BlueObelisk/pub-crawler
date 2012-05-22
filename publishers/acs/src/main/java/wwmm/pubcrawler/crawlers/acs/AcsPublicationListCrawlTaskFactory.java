package wwmm.pubcrawler.crawlers.acs;

import wwmm.pubcrawler.crawler.Task;
import wwmm.pubcrawler.crawler.TaskBuilder;
import wwmm.pubcrawler.crawlers.acs.tasks.AcsPublicationListCrawlTask;
import wwmm.pubcrawler.tasks.HttpCrawlTaskData;

import static wwmm.pubcrawler.Config.PUBLICATION_LIST_CACHE_MAX_AGE;
import static wwmm.pubcrawler.Config.PUBLICATION_LIST_CRAWL_INTERVAL;

/**
 * @author Sam Adams
 */
public class AcsPublicationListCrawlTaskFactory {
    
    public Task createCrawlTask() {
        return TaskBuilder.newTask(AcsPublicationListCrawlTask.INSTANCE)
                .withId("acs:journal-list")
                .withInterval(PUBLICATION_LIST_CRAWL_INTERVAL)
                .withData(new HttpCrawlTaskData(Acs.JOURNAL_LIST_URL, "index.html", PUBLICATION_LIST_CACHE_MAX_AGE, null))
                .build();
    }

}
