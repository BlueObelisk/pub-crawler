package wwmm.pubcrawler.crawlers.acs.tasks;

import nu.xom.Document;
import org.joda.time.Duration;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerGetRequest;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import uk.ac.cam.ch.wwmm.httpcrawler.HttpFetcher;
import wwmm.pubcrawler.crawlers.acs.parsers.AcsIssueTocParser;
import wwmm.pubcrawler.model.Article;
import wwmm.pubcrawler.model.Issue;
import wwmm.pubcrawler.model.id.JournalId;
import wwmm.pubcrawler.v2.crawler.CrawlRunner;
import wwmm.pubcrawler.v2.crawler.CrawlTask;
import wwmm.pubcrawler.v2.crawler.TaskData;
import wwmm.pubcrawler.v2.crawler.TaskQueue;
import wwmm.pubcrawler.v2.crawler.common.HtmlCrawler;
import wwmm.pubcrawler.v2.repositories.ArticleRepository;
import wwmm.pubcrawler.v2.repositories.IssueRepository;

import javax.inject.Inject;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;
import static wwmm.pubcrawler.v2.crawler.CrawlTaskBuilder.newJob;

/**
 * @author Sam Adams
 */
public class AcsIssueTocCrawler extends HtmlCrawler implements CrawlRunner {

    private static final String CURRENT_ISSUE_URL = "http://pubs.acs.org/toc/%s/current";

    private final HttpFetcher httpCrawler;
    private final TaskQueue crawlQueue;
    private final IssueRepository issueRepository;
    private final ArticleRepository articleRepository;

    @Inject
    public AcsIssueTocCrawler(final HttpFetcher httpCrawler, final TaskQueue crawlQueue, final IssueRepository issueRepository, final ArticleRepository articleRepository) {
        this.httpCrawler = httpCrawler;
        this.crawlQueue = crawlQueue;
        this.issueRepository = issueRepository;
        this.articleRepository = articleRepository;
    }

    @Override
    public void run(final String id, final TaskData data) throws Exception {
        final String key = String.format("%s:toc.html", id);
        final URI url = URI.create(data.getString("url"));
        final JournalId journalId = new JournalId(data.getString("journalId"));
        
        final CrawlerGetRequest request = new CrawlerGetRequest(url, key, Duration.standardDays(1));
        final CrawlerResponse response = httpCrawler.execute(request);

        final Document html = readDocument(response);
        final AcsIssueTocParser parser = new AcsIssueTocParser(html, URI.create(html.getBaseURI()), journalId);
        
        final Issue issue = parser.toIssue();
        issueRepository.updateIssue(issue);

        System.out.println("issue: " + issue.getId());

        for (final Article article : parser.getArticles()) {
            System.out.println("article: " + article.getId());

            articleRepository.updateArticle(article);
        }
        
        final Issue prevIssue = parser.getPreviousIssue();
        if (prevIssue != null) {
            queueTask(journalId, prevIssue);
        }
    }

    private void queueTask(final JournalId journalId, final Issue issue) {
        final Map<String,String> data = new HashMap<String, String>();
        data.put("url", issue.getUrl().toString());
        data.put("journalId", journalId.getValue());

        final CrawlTask task = newJob()
            .ofType(AcsIssueTocCrawler.class)
            .withId(issue.getId().getValue())
            .withData(data)
            .build();

        crawlQueue.queueTask(task);
    }
}
