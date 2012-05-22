package wwmm.pubcrawler.crawlers.elsevier;

import wwmm.pubcrawler.crawler.Task;
import wwmm.pubcrawler.crawler.TaskBuilder;
import wwmm.pubcrawler.crawlers.elsevier.tasks.ElsevierPublicationListCrawlTask;
import wwmm.pubcrawler.tasks.HttpCrawlTaskData;

import static wwmm.pubcrawler.Config.PUBLICATION_LIST_CACHE_MAX_AGE;
import static wwmm.pubcrawler.Config.PUBLICATION_LIST_CRAWL_INTERVAL;

/**
 * @author Sam Adams
 */
public class ElsevierPublicationListCrawlTaskFactory {

    public Task<HttpCrawlTaskData> createCrawlTask() {
        final HttpCrawlTaskData data = new HttpCrawlTaskData(Elsevier.OPML_URL, "opml.xml", PUBLICATION_LIST_CACHE_MAX_AGE, null);
        return TaskBuilder.newTask(ElsevierPublicationListCrawlTask.INSTANCE)
                .withId("elsevier:journal-list")
                .withInterval(PUBLICATION_LIST_CRAWL_INTERVAL)
                .withData(data)
                .build();
    }

}
