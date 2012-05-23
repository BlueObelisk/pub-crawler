package wwmm.pubcrawler.crawlers.springer;

import wwmm.pubcrawler.crawler.Task;
import wwmm.pubcrawler.crawler.TaskBuilder;
import wwmm.pubcrawler.crawlers.springer.tasks.SpringerIssueListCrawlTask;
import wwmm.pubcrawler.tasks.IssueListCrawlTaskData;

import java.net.URI;

import static wwmm.pubcrawler.Config.ISSUE_LIST_CACHE_MAX_AGE;
import static wwmm.pubcrawler.Config.ISSUE_LIST_CRAWL_INTERVAL;

/**
 * @author Sam Adams
 */
public class SpringerIssueListCrawlTaskFactory {

    public Task createIssueListCrawlTask(final String journal, final URI url) {
        final URI uri = URI.create("http://www.springerlink.com/content/" + journal + "/");
        final IssueListCrawlTaskData data = new IssueListCrawlTaskData(uri, "issues.html", ISSUE_LIST_CACHE_MAX_AGE, "springer", journal);

        return TaskBuilder.newTask(SpringerIssueListCrawlTask.INSTANCE)
                .withId("springer:issue-list:" + journal + "/current")
                .withData(data)
                .withInterval(ISSUE_LIST_CRAWL_INTERVAL)
                .build();
    }

}
