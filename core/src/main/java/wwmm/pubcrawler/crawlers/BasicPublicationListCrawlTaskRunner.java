package wwmm.pubcrawler.crawlers;

import nu.xom.Document;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import wwmm.pubcrawler.http.DocumentResource;
import wwmm.pubcrawler.http.Fetcher;
import wwmm.pubcrawler.http.UriRequest;
import wwmm.pubcrawler.processors.PublicationListProcessor;
import wwmm.pubcrawler.tasks.HttpCrawlTaskData;
import wwmm.pubcrawler.utils.HtmlUtils;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URI;

/**
 * @author Sam Adams
 */
public abstract class BasicPublicationListCrawlTaskRunner extends BasicHttpCrawlTaskRunner<HttpCrawlTaskData> {

    private final PublicationListProcessor<DocumentResource> processor;

    @Inject
    public BasicPublicationListCrawlTaskRunner(final Fetcher<UriRequest, CrawlerResponse> fetcher, final PublicationListProcessor<DocumentResource> processor) {
        super(fetcher);
        this.processor = processor;
    }

    @Override
    protected void handleResponse(final String id, final HttpCrawlTaskData data, final CrawlerResponse response) throws Exception {
        final Document html = readResponse(response);
        processor.processPublicationList(new DocumentResource(URI.create(html.getBaseURI()), html));
    }

    protected Document readResponse(final CrawlerResponse response) throws IOException {
        return HtmlUtils.readHtmlDocument(response);
    }
}
