package wwmm.pubcrawler.model.id;

/**
 * @author Sam Adams
 */
public class ResourceId extends Id<ResourceId> {

    public ResourceId(String value) {
        super(value);
    }

    public ResourceId(ArticleId articleId, String path) {
        super(articleId.getValue()+"/"+path);
    }

}
