package wwmm.pubcrawler.model.id;

/**
 * @author Sam Adams
 */
public class JournalId extends Id<JournalId> {

    public JournalId(String value) {
        super(value);
    }

    public JournalId(PublisherId publisherId, String abbreviation) {
        super(publisherId.getValue()+"/"+abbreviation);
    }

}
