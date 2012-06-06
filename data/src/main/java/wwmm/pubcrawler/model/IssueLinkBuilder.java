package wwmm.pubcrawler.model;

import wwmm.pubcrawler.model.id.IssueId;
import wwmm.pubcrawler.model.id.JournalId;

import java.net.URI;

import static org.apache.commons.lang.Validate.notNull;

/**
 * @author Sam Adams
 */
public class IssueLinkBuilder {

    private JournalId journalId;
    private String journalTitle;
    private String volume;
    private String number;
    private String year;
    private String issueRef;
    
    private URI url;

    public IssueLinkBuilder withJournalId(final JournalId journalId) {
        this.journalId = journalId;
        return this;
    }

    public IssueLinkBuilder withJournalTitle(final String journalTitle) {
        this.journalTitle = journalTitle;
        return this;
    }

    public IssueLinkBuilder withVolume(final String volume) {
        this.volume = volume;
        return this;
    }

    public IssueLinkBuilder withNumber(final String number) {
        this.number = number;
        return this;
    }

    public IssueLinkBuilder withUrl(final URI url) {
        this.url = url;
        return this;
    }

    public IssueLinkBuilder withIssueRef(final String issueRef) {
        this.issueRef = issueRef;
        return this;
    }

    public IssueLink build() {
        notNull(journalId);
        notNull(volume);
        notNull(number);

        final IssueId issueId = new IssueId(journalId, volume, number);
        return new IssueLink(issueId, url, journalTitle, volume, number, issueRef);
    }
    
}
