package wwmm.pubcrawler.crawlers;

import nu.xom.Document;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import wwmm.pubcrawler.http.Fetcher;
import wwmm.pubcrawler.http.UriRequest;
import wwmm.pubcrawler.crawler.TaskData;
import wwmm.pubcrawler.http.DocumentResource;
import wwmm.pubcrawler.model.id.JournalId;
import wwmm.pubcrawler.model.id.PublisherId;
import wwmm.pubcrawler.processors.IssueListProcessor;
import wwmm.pubcrawler.utils.HtmlUtils;

import java.net.URI;

/**
 * @author Sam Adams
 */
public class BasicIssueListHttpCrawlTask extends BasicHttpCrawlTask {

    private final IssueListProcessor<DocumentResource> processor;

    public BasicIssueListHttpCrawlTask(final Fetcher<UriRequest, CrawlerResponse> fetcher, final IssueListProcessor<DocumentResource> processor) {
        super(fetcher);
        this.processor = processor;
    }

    @Override
    protected void handleResponse(final String id, final TaskData data, final CrawlerResponse response) throws Exception {
        final Document html = HtmlUtils.readHtmlDocument(response);
        final URI url = URI.create(html.getBaseURI());

        final PublisherId publisherId = new PublisherId(data.getString("publisher"));
        final JournalId journalId = new JournalId(publisherId, data.getString("journal"));

        processor.processIssueList(journalId, new DocumentResource(url, html));
        // TODO find archive volumes/issues
    }
}
