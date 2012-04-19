package wwmm.pubcrawler.model;

import wwmm.pubcrawler.model.id.IssueId;
import wwmm.pubcrawler.model.id.JournalId;
import wwmm.pubcrawler.model.id.PublisherId;

import java.net.URI;

import static org.apache.commons.lang.Validate.notNull;

/**
 * @author Sam Adams
 */
public class IssueBuilder {

    private JournalId journalId;
    private String journalTitle;
    private String volume;
    private String number;
    private String year;
    
    private URI url;

    public IssueBuilder withPublisherId(final PublisherId publisherId) {
        return this;
    }

    public IssueBuilder withJournalId(final JournalId journalId) {
        this.journalId = journalId;
        return this;
    }

    public IssueBuilder withJournalTitle(final String journalTitle) {
        this.journalTitle = journalTitle;
        return this;
    }

    public IssueBuilder withVolume(final String volume) {
        this.volume = volume;
        return this;
    }

    public IssueBuilder withNumber(final String number) {
        this.number = number;
        return this;
    }

    public IssueBuilder withYear(final String year) {
        this.year = year;
        return this;
    }
    
    public IssueBuilder withUrl(final URI url) {
        this.url = url;
        return this;
    }
    
    public Issue build() {
        notNull(journalId);
        notNull(volume);
        notNull(number);
//        notNull(journalTitle);
//        notNull(year);

        final IssueId issueId = new IssueId(journalId, volume, number);
        return new Issue(issueId, journalTitle, volume, number, year, url);
    }
    
}
