package wwmm.pubcrawler.v2.fetcher;

/**
 * @author Sam Adams
 */
public class Content {
    
    private final String key;

    private final byte[] content;

    public Content(final String key, final byte[] content) {
        this.key = key;
        this.content = content;
    }

    public String getKey() {
        return key;
    }

    public byte[] getContent() {
        return content;
    }

}
