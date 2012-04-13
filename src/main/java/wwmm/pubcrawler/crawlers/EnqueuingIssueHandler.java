package wwmm.pubcrawler.crawlers;

import wwmm.pubcrawler.model.Issue;
import wwmm.pubcrawler.crawler.CrawlTask;
import wwmm.pubcrawler.crawler.TaskQueue;

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
    public void handleIssue(final Issue issue) {

    }

    @Override
    public void handleIssueLink(final Issue issue) {
        if (issue != null) {
            enqueueIssue(issue);
        }
    }

    private void enqueueIssue(final Issue issue) {
        final CrawlTask task = taskFactory.createIssueTocCrawlTask(issue.getId(), issue.getUrl());
        if (task != null) {
            taskQueue.queueTask(task);
        }
    }

}
