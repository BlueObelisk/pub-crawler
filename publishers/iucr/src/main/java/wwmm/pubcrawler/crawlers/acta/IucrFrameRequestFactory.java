package wwmm.pubcrawler.crawlers.acta;

import wwmm.pubcrawler.http.RequestFactory;
import wwmm.pubcrawler.tasks.HttpCrawlTaskData;

import javax.inject.Singleton;
import java.net.URI;

/**
 * @author Sam Adams
 */
@Singleton
public class IucrFrameRequestFactory implements RequestFactory<HttpCrawlTaskData, IucrFrameRequest> {

    @Override
    public IucrFrameRequest createFetchTask(final String taskId, final HttpCrawlTaskData data) {
        final URI url = data.getUrl();
        final URI bodyUrl = url.resolve("isscontsbdy.html");
        final URI headUrl = url.resolve("isscontshdr.html");
        return new IucrFrameRequest(taskId, bodyUrl, headUrl, data.getMaxAge(), null);
    }
}
