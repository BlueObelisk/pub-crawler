package wwmm.pubcrawler.model.id;

/**
 * @author Sam Adams
 */
public class PublisherId extends Id<PublisherId> {

    public PublisherId(final String value) {
        super(value);
        if (value.contains("/") || value.length() == 0) {
            throw new IllegalArgumentException("Bad publisher ID: ["+value+"]");
        }
    }

}
