package wwmm.pubcrawler.crawlers;

import wwmm.pubcrawler.crawler.Task;
import wwmm.pubcrawler.crawler.TaskQueue;
import wwmm.pubcrawler.model.Issue;
import wwmm.pubcrawler.model.IssueLink;

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
    public void handleIssueLink(final IssueLink issueLink) {
        if (issueLink != null) {
            enqueueIssue(issueLink);
        }
    }

    private void enqueueIssue(final IssueLink issueLink) {
        final Task task = taskFactory.createIssueTocCrawlTask(issueLink);
        if (task != null) {
            taskQueue.queueTask(task);
        }
    }

}
