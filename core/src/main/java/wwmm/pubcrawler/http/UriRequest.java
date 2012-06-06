package wwmm.pubcrawler.http;

import org.joda.time.Duration;

import java.net.URI;

/**
 * @author Sam Adams
 */
public class UriRequest {
    
    private URI uri;
    private String id;
    private Duration maxAge;
    private URI referrer;

    public UriRequest(final URI uri, final String id, final Duration maxAge, final URI referrer) {
        this.uri = uri;
        this.id = id;
        this.maxAge = maxAge;
        this.referrer = referrer;
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

    public URI getReferrer() {
        return referrer;
    }
}
