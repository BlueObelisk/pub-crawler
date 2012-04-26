package wwmm.pubcrawler.http;

import nu.xom.Document;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import uk.ac.cam.ch.wwmm.httpcrawler.HttpFetcher;
import wwmm.pubcrawler.utils.HtmlUtils;

import javax.inject.Inject;
import java.io.IOException;

/**
 * @author Sam Adams
 */
public class HtmlDocumentResourceHttpFetcher extends DocumentResourceHttpFetcher {

    @Inject
    public HtmlDocumentResourceHttpFetcher(final HttpFetcher httpFetcher) {
        super(httpFetcher);
    }

    @Override
    protected Document readDocument(final CrawlerResponse response) throws IOException {
        return HtmlUtils.readHtmlDocument(response);
    }
}
