package wwmm.pubcrawler.crawlers.acta;

import org.joda.time.Duration;
import wwmm.pubcrawler.crawler.Task;
import wwmm.pubcrawler.crawler.TaskBuilder;
import wwmm.pubcrawler.crawlers.acta.tasks.IucrPublicationListCrawlTask;
import wwmm.pubcrawler.tasks.HttpCrawlTaskData;

import static wwmm.pubcrawler.Config.PUBLICATION_LIST_CACHE_MAX_AGE;

public class IucrPublicationListCrawlTaskFactory {

    public Task createCrawlTask() {
        final HttpCrawlTaskData data = new HttpCrawlTaskData(Iucr.JOURNALS_URL, "index.html", PUBLICATION_LIST_CACHE_MAX_AGE, null);
        return TaskBuilder.newTask(IucrPublicationListCrawlTask.INSTANCE)
                .withId("iucr:journal-list")
                .withInterval(Duration.standardDays(1))
                .withData(data)
                .build();
    }
}