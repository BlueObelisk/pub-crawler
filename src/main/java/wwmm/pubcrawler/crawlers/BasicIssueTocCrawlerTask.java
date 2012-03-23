package wwmm.pubcrawler.crawlers;

import nu.xom.Document;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import wwmm.pubcrawler.controller.ArticleArchiver;
import wwmm.pubcrawler.controller.BasicHttpFetcher;
import wwmm.pubcrawler.model.Article;
import wwmm.pubcrawler.model.Issue;
import wwmm.pubcrawler.utils.HtmlUtils;
import wwmm.pubcrawler.v2.crawler.CrawlTask;
import wwmm.pubcrawler.v2.crawler.TaskData;
import wwmm.pubcrawler.v2.crawler.TaskQueue;

import java.net.URI;

/**
 * @author Sam Adams
 */
public abstract class BasicIssueTocCrawlerTask extends BasicHttpCrawlTask {

    private final TaskQueue taskQueue;
    private final IssueTocParserFactory parserFactory;
    private final ArticleArchiver archiver;

    public BasicIssueTocCrawlerTask(final BasicHttpFetcher fetcher, final IssueTocParserFactory parserFactory, final TaskQueue taskQueue, final ArticleArchiver archiver) {
        super(fetcher);
        this.taskQueue = taskQueue;
        this.parserFactory = parserFactory;
        this.archiver = archiver;
    }

    @Override
    protected void handleResponse(final String id, final TaskData data, final CrawlerResponse response) throws Exception {
        final Document html = HtmlUtils.readDocument(response);
        final URI url = URI.create(data.getString("url"));
        final String journal = data.getString("journal");
        final IssueTocParser parser = parserFactory.createIssueTocParser(html, url, journal);
        
        for (Article article : parser.getArticles()) {
            archiver.archive(article);
        }
        
        Issue prev = parser.getPreviousIssue();
        CrawlTask task = createIssueTocTask(journal, prev);
        taskQueue.queueTask(task);
    }

    protected abstract CrawlTask createIssueTocTask(final String journal, final Issue prev);

}
