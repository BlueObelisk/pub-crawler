package wwmm.pubcrawler.v2.fetcher;

import java.net.URI;

/**
 * @author Sam Adams
 */
public class HttpResource {
    
    private final String id;
    private final URI url;

    public HttpResource(final String id, final URI url) {
        this.id = id;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public URI getUrl() {
        return url;
    }

}
