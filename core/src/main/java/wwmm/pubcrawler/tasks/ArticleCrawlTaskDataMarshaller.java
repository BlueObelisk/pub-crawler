package wwmm.pubcrawler.tasks;

import org.joda.time.Duration;

import java.net.URI;

/**
 * @author Sam Adams
 */
public class ArticleCrawlTaskDataMarshaller extends AbstractCrawlTaskDataMarshaller implements Marshaller<ArticleCrawlTaskData> {

    private static final String PUBLISHER = "publisher";
    private static final String JOURNAL = "journal";
    private static final String ISSUE = "issue";

    @Override
    public void marshall(final ArticleCrawlTaskData data, final DataSink target) {
        marshallCommonFields(data, target);
        target.writeString(PUBLISHER, data.getPublisher());
        target.writeString(JOURNAL, data.getJournal());
        target.writeString(ISSUE, data.getIssue());
    }

    @Override
    public ArticleCrawlTaskData unmarshall(final DataSource source) {
        final URI url = source.readUri(AbstractCrawlTaskDataMarshaller.URL);
        final String fileId = source.readString(AbstractCrawlTaskDataMarshaller.FILE_ID);
        final Duration maxAge = source.readDuration(AbstractCrawlTaskDataMarshaller.MAX_AGE);
        final String publisher = source.readString(PUBLISHER);
        final String journal = source.readString(JOURNAL);
        final String issue = source.readString(ISSUE);
        return new ArticleCrawlTaskData(url, fileId, maxAge, publisher, journal, issue);
    }
}
