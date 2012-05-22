package wwmm.pubcrawler.crawlers.springer;

import wwmm.pubcrawler.Config;
import wwmm.pubcrawler.crawler.Task;
import wwmm.pubcrawler.crawler.TaskBuilder;
import wwmm.pubcrawler.crawlers.springer.tasks.SpringerPublicationListCrawlTask;

import java.net.URI;

import static wwmm.pubcrawler.Config.PUBLICATION_LIST_CACHE_MAX_AGE;

/**
 * @author Sam Adams
 */
public class SpringerPublicationListCrawlTaskFactory {

    public Task<SpringerPublicationListCrawlTaskData> createIssueListCrawlTask(final URI url, final String key, final int page) {
        final SpringerPublicationListCrawlTaskData data = new SpringerPublicationListCrawlTaskData(url, Config.PUBLICATION_LIST_CACHE_MAX_AGE, key, page);

        return TaskBuilder.newTask(SpringerPublicationListCrawlTask.INSTANCE)
                .withId("springer:journal-list/" + key + "/" + page)
                .withInterval(PUBLICATION_LIST_CACHE_MAX_AGE)
                .withData(data)
                .build();
    }

}
