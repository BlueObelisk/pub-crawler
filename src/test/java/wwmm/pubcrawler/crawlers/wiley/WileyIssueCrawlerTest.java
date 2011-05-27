package wwmm.pubcrawler.crawlers.wiley;

import org.junit.Test;
import org.mockito.Mockito;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerRequest;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import uk.ac.cam.ch.wwmm.httpcrawler.HttpCrawler;
import wwmm.pubcrawler.CrawlerContext;
import wwmm.pubcrawler.crawlers.AbstractCrawlerTest;
import wwmm.pubcrawler.crawlers.springer.SpringerIssueCrawler;
import wwmm.pubcrawler.model.Article;
import wwmm.pubcrawler.model.Issue;
import wwmm.pubcrawler.model.Journal;
import wwmm.pubcrawler.types.Doi;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author Sam Adams
 */
public class WileyIssueCrawlerTest extends AbstractCrawlerTest {

    private CrawlerResponse prepareAbacus_46_4Response() throws IOException {
        return prepareResponse("./abac2010_46_4.html",
                URI.create("http://onlinelibrary.wiley.com/doi/10.1111/abac.2010.46.issue-4/issuetoc"));
    }

    protected WileyIssueCrawler getAbacusIssue46_4() throws IOException {
        Issue issue = new Issue();
        issue.setUrl(URI.create("http://onlinelibrary.wiley.com/doi/10.1111/abac.2010.46.issue-4/issuetoc"));

        CrawlerResponse response = prepareAbacus_46_4Response();

        HttpCrawler crawler = Mockito.mock(HttpCrawler.class);
        Mockito.when(crawler.execute(Mockito.any(CrawlerRequest.class)))
                .thenReturn(response);

        CrawlerContext context = new CrawlerContext(null, crawler, null);
        return new WileyIssueCrawler(issue, new Journal("abac", "Abacus"), context);
    }

    @Test
    public void testGetVolume() throws IOException {
        WileyIssueCrawler crawler = getAbacusIssue46_4();
        assertEquals("46", crawler.getVolume());
    }

    @Test
    public void testGetNumber() throws IOException {
        WileyIssueCrawler crawler = getAbacusIssue46_4();
        assertEquals("4", crawler.getNumber());
    }

    @Test
    public void testGetYear() throws IOException {
        WileyIssueCrawler crawler = getAbacusIssue46_4();
        assertEquals("2010", crawler.getYear());
    }

    @Test
    public void testGetArticles() throws IOException {
        WileyIssueCrawler crawler = getAbacusIssue46_4();
        assertEquals(6, crawler.getArticleNodes().size());
    }

    @Test
    public void testGetArticleDois() throws IOException {
        WileyIssueCrawler crawler = getAbacusIssue46_4();
        List<Article> articles = crawler.getArticles();
        assertEquals(new Doi("10.1111/j.1467-6281.2010.00324.x"), articles.get(0).getDoi());
        assertEquals(new Doi("10.1111/j.1467-6281.2010.00325.x"), articles.get(1).getDoi());
        assertEquals(new Doi("10.1111/j.1467-6281.2010.00326.x"), articles.get(2).getDoi());
        assertEquals(new Doi("10.1111/j.1467-6281.2010.00327.x"), articles.get(3).getDoi());
        assertEquals(new Doi("10.1111/j.1467-6281.2010.00328.x"), articles.get(4).getDoi());
        assertEquals(new Doi("10.1111/j.1467-6281.2010.00313.x"), articles.get(5).getDoi());
    }

}
