package wwmm.pubcrawler.model.id;

/**
 * @author Sam Adams
 */
public class IssueId extends Id<IssueId> {

    private JournalId journalId;

    public IssueId(String value) {
        super(value);
    }

    public IssueId(JournalId journalId, String... parts) {
        super(journalId, parts);
        this.journalId = journalId;
    }

    public String getPublisherPart() {
        return journalId.getPublisherPart();
    }
    
    public String getJournalPart() {
        return journalId.getJournalPart();
    }
    
    public String getIssuePart() {
        return getValue();
    }

}
