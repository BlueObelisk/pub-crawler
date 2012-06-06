package wwmm.pubcrawler.crawlers.rsc;

import org.joda.time.Duration;
import wwmm.pubcrawler.tasks.*;

import java.net.URI;

/**
 * @author Sam Adams
 */
public class RscIssueTocCrawlTaskDataMarshaller extends AbstractCrawlTaskDataMarshaller implements Marshaller<RscIssueTocCrawlTaskData> {

    private static final String PUBLISHER = "publisher";
    private static final String JOURNAL = "journal";
    private static final String JOURNAL_NAME = "journalName";
    private static final String ISSUE = "issue";

    @Override
    public void marshall(final RscIssueTocCrawlTaskData data, final DataSink target) {
        marshallCommonFields(data, target);
        target.writeString(PUBLISHER, data.getPublisher());
        target.writeString(JOURNAL, data.getJournal());
        target.writeString(JOURNAL_NAME, data.getJournalName());
        target.writeString(ISSUE, data.getIssue());
    }

    @Override
    public RscIssueTocCrawlTaskData unmarshall(final DataSource source) {
        final URI url = source.readUri(AbstractCrawlTaskDataMarshaller.URL);
        final String fileId = source.readString(AbstractCrawlTaskDataMarshaller.FILE_ID);
        final Duration maxAge = source.readDuration(AbstractCrawlTaskDataMarshaller.MAX_AGE);
        final String publisher = source.readString(PUBLISHER);
        final String journal = source.readString(JOURNAL);
        final String journalName = source.readString(JOURNAL_NAME);
        final String issue = source.readString(ISSUE);
        return new RscIssueTocCrawlTaskData(url, fileId, maxAge, publisher, journal, journalName, issue);
    }
}
