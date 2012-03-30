package wwmm.pubcrawler.model.id;

/**
 * @author Sam Adams
 */
public class ResourceId extends Id<ResourceId> {

    public ResourceId(final String value) {
        super(value);
    }

    public ResourceId(final ArticleId articleId, final String path) {
        super(articleId, path);
    }

}
