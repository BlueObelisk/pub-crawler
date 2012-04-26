package wwmm.pubcrawler.http;

import org.joda.time.Duration;

import java.net.URI;

/**
 * @author Sam Adams
 */
public class URITask {
    
    private URI uri;
    private String id;
    private Duration maxAge;
    private URI referer;

    public URITask(final URI uri, final String id, final Duration maxAge, final URI referer) {
        this.uri = uri;
        this.id = id;
        this.maxAge = maxAge;
        this.referer = referer;
    }

    public URI getUri() {
        return uri;
    }

    public String getId() {
        return id;
    }

    public Duration getMaxAge() {
        return maxAge;
    }

    public URI getReferer() {
        return referer;
    }
}
