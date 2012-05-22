package wwmm.pubcrawler.crawlers;

import nu.xom.Document;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import wwmm.pubcrawler.http.DocumentResource;
import wwmm.pubcrawler.http.Fetcher;
import wwmm.pubcrawler.http.UriRequest;
import wwmm.pubcrawler.model.id.JournalId;
import wwmm.pubcrawler.model.id.PublisherId;
import wwmm.pubcrawler.processors.IssueListProcessor;
import wwmm.pubcrawler.tasks.IssueListCrawlTaskData;
import wwmm.pubcrawler.utils.HtmlUtils;

import java.net.URI;

/**
 * @author Sam Adams
 */
public class BasicIssueListHttpCrawlTaskRunner extends BasicHttpCrawlTaskRunner<IssueListCrawlTaskData> {

    private final IssueListProcessor<DocumentResource> processor;

    public BasicIssueListHttpCrawlTaskRunner(final Fetcher<UriRequest, CrawlerResponse> fetcher, final IssueListProcessor<DocumentResource> processor) {
        super(fetcher);
        this.processor = processor;
    }

    @Override
    protected void handleResponse(final String id, final IssueListCrawlTaskData data, final CrawlerResponse response) throws Exception {
        final Document html = HtmlUtils.readHtmlDocument(response);
        final URI url = URI.create(html.getBaseURI());

        final PublisherId publisherId = new PublisherId(data.getPublisher());
        final JournalId journalId = new JournalId(publisherId, data.getJournal());

        processor.processIssueList(journalId, new DocumentResource(url, html));
        // TODO find archive volumes/issues
    }
}
