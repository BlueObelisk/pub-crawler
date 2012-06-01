package wwmm.pubcrawler.crawlers;

import org.joda.time.Duration;
import wwmm.pubcrawler.crawler.Task;
import wwmm.pubcrawler.crawler.TaskBuilder;
import wwmm.pubcrawler.model.id.IssueId;
import wwmm.pubcrawler.model.id.JournalId;
import wwmm.pubcrawler.tasks.IssueTocCrawlTaskData;
import wwmm.pubcrawler.tasks.TaskSpecification;

import java.net.URI;

import static wwmm.pubcrawler.Config.ISSUE_TOC_CACHE_MAX_AGE;

/**
 * @author Sam Adams
 */
public abstract class AbstractIssueTocCrawlTaskFactory implements IssueTocCrawlTaskFactory {
    
    @Override
    public Task<IssueTocCrawlTaskData> createCurrentIssueTocCrawlTask(final JournalId journalId, final URI url) {
        return createIssueTocCrawlTask(new IssueId(journalId, "current"), url);
    }

    @Override
    public Task<IssueTocCrawlTaskData> createIssueTocCrawlTask(final IssueId issueId, final URI url) {
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
