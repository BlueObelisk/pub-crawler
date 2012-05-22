package wwmm.pubcrawler.crawlers.springer;

import org.joda.time.Duration;
import wwmm.pubcrawler.tasks.*;

import java.net.URI;

/**
 * @author Sam Adams
 */
public class SpringerPublicationListCrawlTaskDataMarshaller extends AbstractCrawlTaskDataMarshaller implements Marshaller<SpringerPublicationListCrawlTaskData> {

    private static final String KEY = "key";
    private static final String PAGE = "page";

    @Override
    public void marshall(final SpringerPublicationListCrawlTaskData data, final DataSink target) {
        marshallCommonFields(data, target);
        target.writeString(KEY, data.getKey());
        target.writeInt(PAGE, data.getPage());
    }

    @Override
    public SpringerPublicationListCrawlTaskData unmarshall(final DataSource source) {
        final URI url = source.readUri(AbstractCrawlTaskDataMarshaller.URL);
        final String fileId = source.readString(AbstractCrawlTaskDataMarshaller.FILE_ID);
        final Duration maxAge = source.readDuration(AbstractCrawlTaskDataMarshaller.MAX_AGE);
        final String key = source.readString(KEY);
        final int page = source.readInt(PAGE);
        return new SpringerPublicationListCrawlTaskData(url, maxAge, key, page);
    }
}
