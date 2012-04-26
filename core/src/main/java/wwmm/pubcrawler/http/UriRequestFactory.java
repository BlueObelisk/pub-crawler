package wwmm.pubcrawler.http;

import org.joda.time.Duration;
import wwmm.pubcrawler.crawler.TaskData;

import java.net.URI;

/**
 * @author Sam Adams
 */
public class UriRequestFactory implements RequestFactory<UriRequest> {

    @Override
    public UriRequest createFetchTask(final String taskId, final TaskData data) {
        final Duration maxAge = data.containsKey("maxAge") ? new Duration(Long.valueOf(data.getString("maxAge"))) : null;
        final URI url = URI.create(data.getString("url"));
        final URI referrer = data.containsKey("referrer") ? URI.create(data.getString("referrer")) : null;
        final String id = taskId + "/" + data.getString("fileId");
        return new UriRequest(url, id, maxAge, referrer);
    }
}
