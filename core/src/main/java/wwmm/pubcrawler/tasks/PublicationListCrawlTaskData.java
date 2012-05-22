package wwmm.pubcrawler.tasks;

import org.joda.time.Duration;

import java.net.URI;

/**
 * @author Sam Adams
 */
public class PublicationListCrawlTaskData extends HttpCrawlTaskData {

    public PublicationListCrawlTaskData(final URI url, final String fileId, final Duration maxAge) {
        super(url, fileId, maxAge, url);
    }

}
