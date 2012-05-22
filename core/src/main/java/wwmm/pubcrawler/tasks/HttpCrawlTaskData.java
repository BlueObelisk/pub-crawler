package wwmm.pubcrawler.tasks;

import org.joda.time.Duration;

import java.net.URI;

/**
 * @author Sam Adams
 */
public class HttpCrawlTaskData {

    private final URI url;
    private final String fileId;
    private final Duration maxAge;
    private final URI referrer;

    public HttpCrawlTaskData(final URI url, final String fileId, final Duration maxAge, final URI referrer) {
        this.url = url;
        this.fileId = fileId;
        this.maxAge = maxAge;
        this.referrer = referrer;
    }

    public URI getUrl() {
        return url;
    }

    public String getFileId() {
        return fileId;
    }

    public Duration getMaxAge() {
        return maxAge;
    }

    public URI getReferrer() {
        return referrer;
    }
}
