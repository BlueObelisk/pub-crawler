package wwmm.pubcrawler.crawlers.wiley;

import wwmm.pubcrawler.crawler.Task;
import wwmm.pubcrawler.crawler.TaskBuilder;
import wwmm.pubcrawler.crawlers.AbstractIssueTocCrawlTaskFactory;
import wwmm.pubcrawler.crawlers.wiley.tasks.WileyIssueListCrawlTask;
import wwmm.pubcrawler.crawlers.wiley.tasks.WileyIssueTocCrawlTask;
import wwmm.pubcrawler.model.Journal;
import wwmm.pubcrawler.model.id.IssueId;
import wwmm.pubcrawler.tasks.IssueListCrawlTaskData;
import wwmm.pubcrawler.tasks.IssueTocCrawlTaskData;
import wwmm.pubcrawler.tasks.TaskSpecification;

import javax.inject.Singleton;
import java.net.URI;

import static wwmm.pubcrawler.Config.ISSUE_LIST_CACHE_MAX_AGE;
import static wwmm.pubcrawler.Config.ISSUE_LIST_CRAWL_INTERVAL;

/**
 * @author Sam Adams
 */
@Singleton
public class WileyIssueListCrawlTaskFactory {

    public Task createIssueListCrawlTask(final String journal, final String year) {
        final URI uri = URI.create("http://onlinelibrary.wiley.com/journal/" + journal + "/issues/fragment?activeYear=" + year + "&SKIP_DECORATION=true");
        final IssueListCrawlTaskData data = new IssueListCrawlTaskData(uri, "issues-" + year + ".html", ISSUE_LIST_CACHE_MAX_AGE, "wiley", journal);

        return TaskBuilder.newTask(WileyIssueListCrawlTask.INSTANCE)
                .withId("wiley:issue-list:" + journal + "/" + year)
                .withData(data)
                .withInterval(ISSUE_LIST_CRAWL_INTERVAL)
                .build();
    }

}
