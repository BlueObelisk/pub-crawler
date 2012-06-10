package wwmm.pubcrawler.tasks;

import org.joda.time.Duration;

import java.net.URI;

/**
 * @author Sam Adams
 */
public class ArticleCrawlTaskData extends HttpCrawlTaskData {

    private final String publisher;
    private final String journal;
    private final String issue;

    public ArticleCrawlTaskData(final URI url, final String fileId, final Duration maxAge, final String publisher, final String journal, final String issue) {
        super(url, fileId, maxAge, url);
        this.publisher = publisher;
        this.journal = journal;
        this.issue = issue;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getJournal() {
        return journal;
    }

    public String getIssue() {
        return issue;
    }
}
