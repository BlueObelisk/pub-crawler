package wwmm.pubcrawler.crawlers.rsc;

import org.joda.time.Duration;
import wwmm.pubcrawler.tasks.IssueTocCrawlTaskData;

import java.net.URI;

/**
 * @author Sam Adams
 */
public class RscIssueTocCrawlTaskData extends IssueTocCrawlTaskData {

    private final String journalName;
    private final String issue;

    public RscIssueTocCrawlTaskData(final URI url, final String fileId, final Duration maxAge, final String publisher, final String journal, final String journalName, final String issue) {
        super(url, fileId, maxAge, publisher, journal);
        this.journalName = journalName;
        this.issue = issue;
    }

    public String getJournalName() {
        return journalName;
    }

    public String getIssue() {
        return issue;
    }
}
