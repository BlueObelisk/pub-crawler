package wwmm.pubcrawler.crawlers.wiley;

import wwmm.pubcrawler.crawler.Task;
import wwmm.pubcrawler.crawler.TaskBuilder;
import wwmm.pubcrawler.crawlers.wiley.tasks.WileyPublicationListCrawlTask;
import wwmm.pubcrawler.tasks.HttpCrawlTaskData;

import static wwmm.pubcrawler.Config.PUBLICATION_LIST_CACHE_MAX_AGE;
import static wwmm.pubcrawler.Config.PUBLICATION_LIST_CRAWL_INTERVAL;

public class WileyPublicationListCrawlTaskFactory {

    public Task<HttpCrawlTaskData> createCrawlTask() {
        final HttpCrawlTaskData data = new HttpCrawlTaskData(Wiley.PUBLICATION_LIST_URL, "journals.html", PUBLICATION_LIST_CACHE_MAX_AGE, null);

        return TaskBuilder.newTask(WileyPublicationListCrawlTask.INSTANCE)
                .withId("wiley:journal-list")
                .withData(data)
                .withInterval(PUBLICATION_LIST_CRAWL_INTERVAL)
                .build();
    }
}