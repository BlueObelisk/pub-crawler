package wwmm.pubcrawler.tasks;

import org.joda.time.Duration;

import java.net.URI;

/**
 * @author Sam Adams
 */
public class IssueListCrawlTaskDataMarshaller extends AbstractCrawlTaskDataMarshaller implements Marshaller<IssueListCrawlTaskData> {

    private static final String PUBLISHER = "publisher";
    private static final String JOURNAL = "journal";

    @Override
    public void marshall(final IssueListCrawlTaskData data, final DataSink target) {
        marshallCommonFields(data, target);
        target.writeString(PUBLISHER, data.getPublisher());
        target.writeString(JOURNAL, data.getJournal());
    }

    @Override
    public IssueListCrawlTaskData unmarshall(final DataSource source) {
        final URI url = source.readUri(AbstractCrawlTaskDataMarshaller.URL);
        final String fileId = source.readString(AbstractCrawlTaskDataMarshaller.FILE_ID);
        final Duration maxAge = source.readDuration(AbstractCrawlTaskDataMarshaller.MAX_AGE);
        final String publisher = source.readString(PUBLISHER);
        final String journal = source.readString(JOURNAL);
        return new IssueListCrawlTaskData(url, fileId, maxAge, publisher, journal);
    }
}
