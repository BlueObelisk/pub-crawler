package wwmm.pubcrawler.crawlers;

import nu.xom.Document;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import wwmm.pubcrawler.controller.Fetcher;
import wwmm.pubcrawler.controller.URITask;
import wwmm.pubcrawler.crawler.TaskData;
import wwmm.pubcrawler.http.HtmlDocument;
import wwmm.pubcrawler.processors.PublicationListProcessor;
import wwmm.pubcrawler.utils.HtmlUtils;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URI;

/**
 * @author Sam Adams
 */
public abstract class BasicPublicationListCrawlTask extends BasicHttpCrawlTask {

    private final PublicationListProcessor<HtmlDocument> processor;
    
    @Inject
    public BasicPublicationListCrawlTask(final Fetcher<URITask, CrawlerResponse> fetcher, final PublicationListProcessor<HtmlDocument> processor) {
        super(fetcher);
        this.processor = processor;
    }

    @Override
    protected void handleResponse(final String id, final TaskData data, final CrawlerResponse response) throws Exception {
        final Document html = readResponse(response);
        processor.processPublicationList(new HtmlDocument(URI.create(html.getBaseURI()), html));
    }

    protected Document readResponse(final CrawlerResponse response) throws IOException {
        return HtmlUtils.readHtmlDocument(response);
    }

}
