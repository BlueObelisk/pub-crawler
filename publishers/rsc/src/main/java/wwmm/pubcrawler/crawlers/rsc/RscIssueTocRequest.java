package wwmm.pubcrawler.crawlers.rsc;

import org.joda.time.Duration;
import wwmm.pubcrawler.http.UriRequest;

import java.net.URI;

/**
 * @author Sam Adams
 */
public class RscIssueTocRequest extends UriRequest {

    private final String journal;
    private final String journalName;
    private final String issue;

    public RscIssueTocRequest(final URI uri, final String id, final Duration maxAge, final URI referrer, final String journal, final String journalName, final String issue) {
        super(uri, id, maxAge, referrer, null);
        this.journal = journal;
        this.journalName = journalName;
        this.issue = issue;
    }

    public String getJournal() {
        return journal;
    }

    public String getJournalName() {
        return journalName;
    }

    public String getIssue() {
        return issue;
    }
}
