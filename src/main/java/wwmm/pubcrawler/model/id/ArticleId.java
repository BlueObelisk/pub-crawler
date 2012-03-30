package wwmm.pubcrawler.model.id;

/**
 * @author Sam Adams
 */
public class ArticleId extends Id<ArticleId> {

    private JournalId journalId;

    public ArticleId(String value) {
        super(value);
    }

    public ArticleId(JournalId journalId, String value) {
        super(journalId, value);
        this.journalId = journalId;
    }

    public String getPublisherPart() {
        return journalId.getPublisherPart();
    }

    public String getJournalPart() {
        return journalId.getJournalPart();
    }

    public String getArticlePart() {
        return getValue();
    }

}
