package wwmm.pubcrawler.crawlers.acta;

import nu.xom.Document;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerRequest;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import uk.ac.cam.ch.wwmm.httpcrawler.GetRequestBuilder;
import uk.ac.cam.ch.wwmm.httpcrawler.HttpFetcher;
import wwmm.pubcrawler.http.Fetcher;
import wwmm.pubcrawler.utils.HtmlUtils;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URI;

/**
 * @author Sam Adams
 */
public class IucrFrameResourceFetcher implements Fetcher<IucrFrameRequest, IucrFrameResource> {

    private final HttpFetcher httpFetcher;

    @Inject
    public IucrFrameResourceFetcher(final HttpFetcher httpFetcher) {
        this.httpFetcher = httpFetcher;
    }

    @Override
    public IucrFrameResource fetch(final IucrFrameRequest task) throws Exception {
        final CrawlerResponse body = httpFetcher.execute(createRequest(task, task.getBodyUrl()));
        final CrawlerResponse head = httpFetcher.execute(createRequest(task, task.getHeadUrl()));
        return new IucrFrameResource(readHtml(body), readHtml(head));
    }

    private CrawlerRequest createRequest(final IucrFrameRequest task, final URI url) {
        return new GetRequestBuilder()
         .withKey(createId(task, url))
         .withUrl(url)
         .withMaxAge(task.getMaxAge())
         .withReferrer(task.getReferrer())
         .build();
    }

    private String createId(final IucrFrameRequest task, final URI url) {
        final String path = url.getPath();
        return task.getId() + path.substring(path.lastIndexOf('/'));
    }

    private Document readHtml(final CrawlerResponse response) throws IOException {
        return HtmlUtils.readHtmlDocument(response);
    }

}
