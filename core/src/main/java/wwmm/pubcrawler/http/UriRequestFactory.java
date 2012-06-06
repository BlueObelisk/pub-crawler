package wwmm.pubcrawler.http;

import wwmm.pubcrawler.tasks.HttpCrawlTaskData;

import static java.lang.String.format;

/**
 * @author Sam Adams
 */
public class UriRequestFactory implements RequestFactory<HttpCrawlTaskData, UriRequest> {

    private static final String FILE_ID = "%s/%s";

    @Override
    public UriRequest createFetchTask(final String taskId, final HttpCrawlTaskData data) {
        return new UriRequest(data.getUrl(), format(FILE_ID, taskId, data.getFileId()), data.getMaxAge(), data.getReferrer());
    }
}
