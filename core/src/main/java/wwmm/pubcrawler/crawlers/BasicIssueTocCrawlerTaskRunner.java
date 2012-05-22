package wwmm.pubcrawler.crawlers;

import nu.xom.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import wwmm.pubcrawler.http.DocumentResource;
import wwmm.pubcrawler.http.Fetcher;
import wwmm.pubcrawler.http.UriRequest;
import wwmm.pubcrawler.model.id.JournalId;
import wwmm.pubcrawler.model.id.PublisherId;
import wwmm.pubcrawler.processors.IssueTocProcessor;
import wwmm.pubcrawler.tasks.IssueTocCrawlTaskData;
import wwmm.pubcrawler.utils.HtmlUtils;

import java.net.URI;

/**
 * @author Sam Adams
 */
public abstract class BasicIssueTocCrawlerTaskRunner extends BasicHttpCrawlTaskRunner<IssueTocCrawlTaskData> {

    private static final Logger LOG = LoggerFactory.getLogger(BasicIssueTocCrawlerTaskRunner.class);

    private final IssueTocProcessor<DocumentResource> processor;

    public BasicIssueTocCrawlerTaskRunner(final Fetcher<UriRequest, CrawlerResponse> fetcher, final IssueTocProcessor<DocumentResource> processor) {
        super(fetcher);
        this.processor = processor;
    }

    @Override
    protected void handleResponse(final String id, final IssueTocCrawlTaskData data, final CrawlerResponse response) throws Exception {
        final Document html = HtmlUtils.readHtmlDocument(response);
        final URI url = URI.create(html.getBaseURI());
        
        final PublisherId publisherId = new PublisherId(data.getPublisher());
        final JournalId journalId = new JournalId(publisherId, data.getJournal());

        processor.process(id, journalId, new DocumentResource(url, html));
    }
}
