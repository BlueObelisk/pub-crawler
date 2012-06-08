package wwmm.pubcrawler.crawlers.springer;

import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;
import wwmm.pubcrawler.http.RequestFactory;
import wwmm.pubcrawler.http.UriRequest;
import wwmm.pubcrawler.tasks.HttpCrawlTaskData;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static java.lang.String.format;

/**
 * @author Sam Adams
 */
public class SpringerUriRequestFactory implements RequestFactory<HttpCrawlTaskData, UriRequest> {

    private static final String FILE_ID = "%s/%s";
    private static final Collection<Cookie> COOKIES;


    static {
        final BasicClientCookie cookie1 = new BasicClientCookie("MUD", "MP");
        final BasicClientCookie cookie2 = new BasicClientCookie("CookiesSupported", "True");
        cookie1.setDomain("www.springerlink.com");
        cookie2.setDomain("www.springerlink.com");
        COOKIES = Arrays.<Cookie>asList(cookie1, cookie2);
    }

    @Override
    public UriRequest createFetchTask(final String taskId, final HttpCrawlTaskData data) {
        return new UriRequest(data.getUrl(), format(FILE_ID, taskId, data.getFileId()), data.getMaxAge(), data.getReferrer(), COOKIES);
    }
}
