package wwmm.pubcrawler.model;

import wwmm.pubcrawler.model.id.IssueId;

import java.net.URI;

/**
 * @author Sam Adams
 */
public class IssueLink {

    private final IssueId issueId;
    private final URI url;
    private final String journalTitle;
    private final String volume;
    private final String number;

    public IssueLink(final IssueId issueId, final URI url, final String journalTitle, final String volume, final String number) {
        this.issueId = issueId;
        this.url = url;
        this.journalTitle = journalTitle;
        this.volume = volume;
        this.number = number;
    }

    public IssueId getIssueId() {
        return issueId;
    }

    public URI getUrl() {
        return url;
    }

    public String getJournalTitle() {
        return journalTitle;
    }

    public String getVolume() {
        return volume;
    }

    public String getNumber() {
        return number;
    }
}
