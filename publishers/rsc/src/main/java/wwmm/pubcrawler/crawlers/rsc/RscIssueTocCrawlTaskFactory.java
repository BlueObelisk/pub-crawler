package wwmm.pubcrawler.crawlers.rsc;

import org.joda.time.Duration;
import wwmm.pubcrawler.crawler.Task;
import wwmm.pubcrawler.crawler.TaskBuilder;
import wwmm.pubcrawler.crawlers.IssueTocCrawlTaskFactory;
import wwmm.pubcrawler.crawlers.rsc.tasks.RscIssueTocCrawlTask;
import wwmm.pubcrawler.model.IssueLink;
import wwmm.pubcrawler.model.Journal;
import wwmm.pubcrawler.model.id.IssueId;
import wwmm.pubcrawler.tasks.TaskSpecification;

import javax.inject.Singleton;
import java.net.URI;

import static wwmm.pubcrawler.Config.ISSUE_TOC_CACHE_MAX_AGE;

/**
 * @author Sam Adams
 */
@Singleton
public class RscIssueTocCrawlTaskFactory implements IssueTocCrawlTaskFactory<RscIssueTocCrawlTaskData> {

    @Override
    public Task<RscIssueTocCrawlTaskData> createCurrentIssueTocCrawlTask(final Journal journal) {
        return createIssueTocCrawlTask(new IssueId(journal.getId(), "current"), journal.getUrl(), journal.getAbbreviation(), journal.getTitle(), "");
    }

    @Override
    public Task<RscIssueTocCrawlTaskData> createIssueTocCrawlTask(final IssueLink issueLink) {
        return createIssueTocCrawlTask(issueLink.getIssueId(), issueLink.getUrl(), issueLink.getIssueId().getJournalPart(), issueLink.getJournalTitle(), issueLink.getIssueRef());
    }

    private Task<RscIssueTocCrawlTaskData> createIssueTocCrawlTask(final IssueId issueId, final URI url, final String journalId, final String journalTitle, final String issueRef) {
        final String publisher = issueId.getPublisherPart();
        final String issue = issueId.getIssuePart();

        final RscIssueTocCrawlTaskData data = new RscIssueTocCrawlTaskData(url, "toc.html", ISSUE_TOC_CACHE_MAX_AGE, publisher, journalId, journalTitle, issueRef);

        final TaskSpecification<RscIssueTocCrawlTaskData> type = getCrawlerType();

        return TaskBuilder.newTask(type)
                .withInterval(Duration.standardDays(1))
                .withId(publisher + ":issue-toc:" + journalId + "/" + issue)
                .withData(data)
                .build();
    }


    protected TaskSpecification<RscIssueTocCrawlTaskData> getCrawlerType() {
        return RscIssueTocCrawlTask.INSTANCE;
    }

}
