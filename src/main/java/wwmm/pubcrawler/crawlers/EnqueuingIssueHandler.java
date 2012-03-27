package wwmm.pubcrawler.crawlers;

import wwmm.pubcrawler.model.Issue;
import wwmm.pubcrawler.v2.crawler.CrawlTask;
import wwmm.pubcrawler.v2.crawler.TaskQueue;

import javax.inject.Inject;

/**
 * @author Sam Adams
 */
public class EnqueuingIssueHandler implements IssueHandler {

    private final TaskQueue taskQueue;
    private final IssueTocCrawlTaskFactory taskFactory;

    @Inject
    public EnqueuingIssueHandler(final TaskQueue taskQueue, final IssueTocCrawlTaskFactory taskFactory) {
        this.taskQueue = taskQueue;
        this.taskFactory = taskFactory;
    }

    @Override
    public void handleIssue(final String journal, final Issue issue) {
        final Issue prev = issue.getPreviousIssue();
        if (prev != null) {
            enqueueIssue(journal, prev);
        }
    }

    private void enqueueIssue(final String journal, final Issue issue) {
        final CrawlTask task = taskFactory.createIssueTocCrawlTask(journal, issue.getUrl(), issue.getId().getValue());
        if (task != null) {
            taskQueue.queueTask(task);
        }
    }

}
