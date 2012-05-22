package wwmm.pubcrawler.tasks;

import org.joda.time.Duration;

import java.net.URI;

/**
 * @author Sam Adams
 */
public class HttpCrawlTaskDataMarshaller extends AbstractCrawlTaskDataMarshaller implements Marshaller<HttpCrawlTaskData> {

    private static final URI DEFAULT_REFERRER = null;

    @Override
    public void marshall(final HttpCrawlTaskData data, final DataSink target) {
        marshallCommonFields(data, target);
    }

    @Override
    public HttpCrawlTaskData unmarshall(final DataSource source) {
        final URI url = source.readUri(AbstractCrawlTaskDataMarshaller.URL);
        final String fileId = source.readString(AbstractCrawlTaskDataMarshaller.FILE_ID);
        final Duration maxAge = source.readDuration(AbstractCrawlTaskDataMarshaller.MAX_AGE);
        return new HttpCrawlTaskData(url, fileId, maxAge, DEFAULT_REFERRER);
    }
}
