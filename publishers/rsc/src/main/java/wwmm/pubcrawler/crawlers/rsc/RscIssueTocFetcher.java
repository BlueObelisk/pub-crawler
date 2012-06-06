package wwmm.pubcrawler.crawlers.rsc;

import nu.xom.Document;
import org.apache.http.message.BasicNameValuePair;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerPostRequest;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerRequest;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import uk.ac.cam.ch.wwmm.httpcrawler.HttpFetcher;
import wwmm.pubcrawler.http.DocumentResource;
import wwmm.pubcrawler.http.Fetcher;
import wwmm.pubcrawler.utils.HtmlUtils;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URI;

/**
 * @author Sam Adams
 */
public class RscIssueTocFetcher implements Fetcher<RscIssueTocRequest, DocumentResource> {

    private static final URI ISSUES_URL = URI.create("http://pubs.rsc.org/en/journals/issues");
    private final HttpFetcher httpFetcher;

    @Inject
    public RscIssueTocFetcher(final HttpFetcher httpFetcher) {
        this.httpFetcher = httpFetcher;
    }

    @Override
    public DocumentResource fetch(final RscIssueTocRequest task) throws Exception {
        final CrawlerRequest request = createRequest(task);
        final CrawlerResponse response = httpFetcher.execute(request);
        final Document document = readDocument(response);
        return new DocumentResource(URI.create(document.getBaseURI()), document);
    }

    private Document readDocument(final CrawlerResponse response) throws IOException {
        return HtmlUtils.readHtmlDocument(response);
    }

    private CrawlerRequest createRequest(final RscIssueTocRequest task) {
        return new CrawlerPostRequest(ISSUES_URL, task.getId(), task.getMaxAge(),
                                      new BasicNameValuePair("name", task.getJournal()),
                                      new BasicNameValuePair("issue", task.getIssue()),
                                      new BasicNameValuePair("jname", task.getJournalName()),
                                      new BasicNameValuePair("iscontentavailable", "true"));
    }
}
