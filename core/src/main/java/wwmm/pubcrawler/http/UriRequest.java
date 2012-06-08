package wwmm.pubcrawler.http;

import org.apache.http.cookie.Cookie;
import org.joda.time.Duration;

import java.net.URI;
import java.util.Collection;

/**
 * @author Sam Adams
 */
public class UriRequest {
    
    private final URI uri;
    private final String id;
    private final Duration maxAge;
    private final URI referrer;
    private final Collection<Cookie> cookies;

    public UriRequest(final URI uri, final String id, final Duration maxAge, final URI referrer, final Collection<Cookie> cookies) {
        this.uri = uri;
        this.id = id;
        this.maxAge = maxAge;
        this.referrer = referrer;
        this.cookies = cookies;
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

    public Collection<Cookie> getCookies() {
        return cookies;
    }
}
