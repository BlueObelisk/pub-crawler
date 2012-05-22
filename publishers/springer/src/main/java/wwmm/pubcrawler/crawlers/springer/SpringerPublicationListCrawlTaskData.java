package wwmm.pubcrawler.crawlers.springer;

import org.joda.time.Duration;
import wwmm.pubcrawler.tasks.HttpCrawlTaskData;

import java.net.URI;

import static java.lang.String.format;

/**
 * @author Sam Adams
 */
public class SpringerPublicationListCrawlTaskData extends HttpCrawlTaskData {

    private static final String FILE_ID = "journals_%s_%d.html";

    private final String key;
    private final int page;

    public SpringerPublicationListCrawlTaskData(final URI url, final Duration maxAge, final String key, final int page) {
        super(url, format(FILE_ID, key, page), maxAge, url);
        this.key = key;
        this.page = page;
    }

    public String getKey() {
        return key;
    }

    public int getPage() {
        return page;
    }
}
