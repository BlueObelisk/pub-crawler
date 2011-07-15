package wwmm.pubcrawler.model.id;

/**
 * @author Sam Adams
 */
public class ArticleId extends Id<ArticleId> {

    public ArticleId(String value) {
        super(value);
    }

    public ArticleId(IssueId issueId, String value) {
        super(issueId, value);
    }

}
