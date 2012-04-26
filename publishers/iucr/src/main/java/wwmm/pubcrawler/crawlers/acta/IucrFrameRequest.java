package wwmm.pubcrawler.crawlers.acta;

import org.joda.time.Duration;

import java.net.URI;

/**
 * @author Sam Adams
 */
public class IucrFrameRequest {

    private final String id;
    private final URI bodyUrl;
    private final URI headUrl;
    private final Duration maxAge;
    private final URI referrer;

    public IucrFrameRequest(final String id, final URI bodyUrl, final URI headUrl, final Duration maxAge, final URI referrer) {
        this.id = id;
        this.bodyUrl = bodyUrl;
        this.headUrl = headUrl;
        this.maxAge = maxAge;
        this.referrer = referrer;
    }

    public String getId() {
        return id;
    }

    public URI getBodyUrl() {
        return bodyUrl;
    }

    public URI getHeadUrl() {
        return headUrl;
    }

    public Duration getMaxAge() {
        return maxAge;
    }

    public URI getReferrer() {
        return referrer;
    }

    @Override
    public String toString() {
        return "IucrFrameTask{" +
                "id='" + id + '\'' +
                '}';
    }
}
