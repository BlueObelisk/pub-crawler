package wwmm.pubcrawler.tasks;

import org.joda.time.Duration;

import java.net.URI;

/**
 * @author Sam Adams
 */
public class IssueListCrawlTaskData extends HttpCrawlTaskData {

    private final String publisher;
    private final String journal;

    public IssueListCrawlTaskData(final URI url, final String fileId, final Duration maxAge, final String publisher, final String journal) {
        super(url, fileId, maxAge, url);
        this.publisher = publisher;
        this.journal = journal;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getJournal() {
        return journal;
    }
}
