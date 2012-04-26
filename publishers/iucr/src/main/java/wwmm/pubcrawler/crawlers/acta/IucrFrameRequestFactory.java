package wwmm.pubcrawler.crawlers.acta;

import org.joda.time.Duration;
import wwmm.pubcrawler.crawler.TaskData;
import wwmm.pubcrawler.http.RequestFactory;

import javax.inject.Singleton;
import java.net.URI;

/**
 * @author Sam Adams
 */
@Singleton
public class IucrFrameRequestFactory implements RequestFactory<IucrFrameRequest> {

    @Override
    public IucrFrameRequest createFetchTask(final String taskId, final TaskData data) {
        final Duration maxAge = data.containsKey("maxAge") ? new Duration(Long.valueOf(data.getString("maxAge"))) : null;
        final URI url = URI.create(data.getString("url"));
        final URI bodyUrl = url.resolve("isscontsbdy.html");
        final URI headUrl = url.resolve("isscontshdr.html");
        return new IucrFrameRequest(taskId, bodyUrl, headUrl, maxAge, null);
    }
}
