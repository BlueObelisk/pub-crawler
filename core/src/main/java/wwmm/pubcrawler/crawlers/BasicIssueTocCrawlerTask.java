package wwmm.pubcrawler.crawlers;

import nu.xom.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import wwmm.pubcrawler.archivers.ArticleArchiver;
import wwmm.pubcrawler.archivers.IssueArchiver;
import wwmm.pubcrawler.controller.Fetcher;
import wwmm.pubcrawler.controller.URITask;
import wwmm.pubcrawler.crawler.TaskData;
import wwmm.pubcrawler.http.HtmlDocument;
import wwmm.pubcrawler.model.Article;
import wwmm.pubcrawler.model.Issue;
import wwmm.pubcrawler.model.id.JournalId;
import wwmm.pubcrawler.model.id.PublisherId;
import wwmm.pubcrawler.parsers.IssueTocParser;
import wwmm.pubcrawler.parsers.IssueTocParserFactory;
import wwmm.pubcrawler.processors.IssueTocProcessor;
import wwmm.pubcrawler.utils.HtmlUtils;

import java.net.URI;
import java.util.List;

/**
 * @author Sam Adams
 */
public abstract class BasicIssueTocCrawlerTask extends BasicHttpCrawlTask {

    private static final Logger LOG = LoggerFactory.getLogger(BasicIssueTocCrawlerTask.class);

    private final IssueTocProcessor<HtmlDocument> processor;

    public BasicIssueTocCrawlerTask(final Fetcher<URITask, CrawlerResponse> fetcher, final IssueTocProcessor<HtmlDocument> processor) {
        super(fetcher);
        this.processor = processor;
    }

    @Override
    protected void handleResponse(final String id, final TaskData data, final CrawlerResponse response) throws Exception {
        final Document html = HtmlUtils.readHtmlDocument(response);
        final URI url = URI.create(html.getBaseURI());
        
        final PublisherId publisherId = new PublisherId(data.getString("publisher"));
        final JournalId journalId = new JournalId(publisherId, data.getString("journal"));

        processor.process(id, journalId, new HtmlDocument(url, html));
    }

}
