package wwmm.pubcrawler.model.id;

/**
 * @author Sam Adams
 */
public class JournalId extends Id<JournalId> {

    private PublisherId publisherId;

    public JournalId(final String value) {
        super(value);
    }

    public JournalId(final PublisherId publisherId, final String abbreviation) {
        super(publisherId, abbreviation);
        this.publisherId = publisherId;
    }
    
    public String getPublisherPart() {
        return publisherId.getValue();
    }
    
    public String getJournalPart() {
        return getValue();
    }

}
