package wwmm.pubcrawler.crawlers;

import org.joda.time.Duration;
import wwmm.pubcrawler.model.id.IssueId;
import wwmm.pubcrawler.model.id.JournalId;
import wwmm.pubcrawler.crawler.CrawlRunner;
import wwmm.pubcrawler.crawler.CrawlTask;
import wwmm.pubcrawler.crawler.CrawlTaskBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sam Adams
 */
public abstract class AbstractIssueTocCrawlTaskFactory implements IssueTocCrawlTaskFactory {
    
    @Override
    public CrawlTask createCurrentIssueTocCrawlTask(final JournalId journalId, final URI url) {
        return createIssueTocCrawlTask(new IssueId(journalId, "current"), url);
    }

    @Override
    public CrawlTask createIssueTocCrawlTask(final IssueId issueId, final URI url) {
        final String publisher = issueId.getPublisherPart();
        final String journal = issueId.getJournalPart();
        final String issue = issueId.getIssuePart();
        
        final Map<String, String> map = new HashMap<String, String>();
        map.put("url", url.toString());
        map.put("fileId", "toc.html");
        map.put("publisher", publisher);
        map.put("journal", journal);

        final Class<? extends CrawlRunner> type = getCrawlerType();
        
        return new CrawlTaskBuilder()
            .ofType(type)
            .withInterval(Duration.standardDays(1))
            .withId(publisher + ":issue-toc:" + journal + "/" + issue)
            .withData(map)
            .build();
    }

    protected abstract Class<? extends CrawlRunner> getCrawlerType();
    
}
