package wwmm.pubcrawler.crawlers.nature;

import wwmm.pubcrawler.crawler.Task;
import wwmm.pubcrawler.crawler.TaskBuilder;
import wwmm.pubcrawler.crawlers.nature.tasks.NaturePublicationListCrawlTask;
import wwmm.pubcrawler.tasks.HttpCrawlTaskData;

import javax.inject.Singleton;

import static wwmm.pubcrawler.Config.PUBLICATION_LIST_CACHE_MAX_AGE;
import static wwmm.pubcrawler.Config.PUBLICATION_LIST_CRAWL_INTERVAL;

/**
 * @author Sam Adams
 */
@Singleton
public class NaturePublicationListCrawlTaskFactory {
    
    public Task createCrawlTask() {
        return TaskBuilder.newTask(NaturePublicationListCrawlTask.INSTANCE)
                .withId("nature:journal-list")
                .withInterval(PUBLICATION_LIST_CRAWL_INTERVAL)
                .withData(new HttpCrawlTaskData(Nature.OPML_URI, "index.html", PUBLICATION_LIST_CACHE_MAX_AGE, null))
                .build();
    }

}
