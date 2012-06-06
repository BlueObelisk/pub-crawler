package wwmm.pubcrawler.crawlers;

import org.joda.time.Duration;
import wwmm.pubcrawler.crawler.Task;
import wwmm.pubcrawler.crawler.TaskBuilder;
import wwmm.pubcrawler.model.IssueLink;
import wwmm.pubcrawler.model.Journal;
import wwmm.pubcrawler.model.id.IssueId;
import wwmm.pubcrawler.tasks.IssueTocCrawlTaskData;
import wwmm.pubcrawler.tasks.TaskSpecification;

import java.net.URI;

import static wwmm.pubcrawler.Config.ISSUE_TOC_CACHE_MAX_AGE;

/**
 * @author Sam Adams
 */
public abstract class AbstractIssueTocCrawlTaskFactory implements IssueTocCrawlTaskFactory<IssueTocCrawlTaskData> {
    
    @Override
    public Task<IssueTocCrawlTaskData> createCurrentIssueTocCrawlTask(final Journal journal) {
        return createIssueTocCrawlTask(new IssueId(journal.getId(), "current"), journal.getUrl());
    }

    @Override
    public Task<IssueTocCrawlTaskData> createIssueTocCrawlTask(final IssueLink issueLink) {
        return createIssueTocCrawlTask(issueLink.getIssueId(), issueLink.getUrl());
    }

    protected Task<IssueTocCrawlTaskData> createIssueTocCrawlTask(final IssueId issueId, final URI url) {
        final String publisher = issueId.getPublisherPart();
        final String journal = issueId.getJournalPart();
        final String issue = issueId.getIssuePart();

        final IssueTocCrawlTaskData data = new IssueTocCrawlTaskData(url, "toc.html", ISSUE_TOC_CACHE_MAX_AGE, publisher, journal);

        final TaskSpecification<IssueTocCrawlTaskData> type = getCrawlerType();

        return TaskBuilder.newTask(type)
                .withInterval(Duration.standardDays(1))
                .withId(publisher + ":issue-toc:" + journal + "/" + issue)
                .withData(data)
                .build();
    }

    protected abstract TaskSpecification<IssueTocCrawlTaskData> getCrawlerType();
    
}
