package wwmm.pubcrawler.model.id;

import org.apache.commons.lang.Validate;

/**
 * @author Sam Adams
 */
public class IssueId extends Id<IssueId> {

    public IssueId(String value) {
        super(value);
    }

    public IssueId(JournalId journalId, String volume, String number) {
        super(journalId.getValue()+"/"+volume+"/"+number);
    }

}
