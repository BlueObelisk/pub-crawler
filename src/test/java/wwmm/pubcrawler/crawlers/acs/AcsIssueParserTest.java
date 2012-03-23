package wwmm.pubcrawler.crawlers.acs;

import nu.xom.Builder;
import nu.xom.Document;
import org.apache.commons.io.IOUtils;
import org.ccil.cowan.tagsoup.Parser;
import org.joda.time.LocalDate;
import org.junit.Test;
import wwmm.pubcrawler.crawlers.acs.parsers.AcsIssueTocParser;
import wwmm.pubcrawler.model.Article;
import wwmm.pubcrawler.model.Issue;
import wwmm.pubcrawler.model.Journal;
import wwmm.pubcrawler.model.id.PublisherId;
import wwmm.pubcrawler.types.Doi;
import wwmm.pubcrawler.utils.ResourceUtil;

import java.io.InputStream;
import java.net.URI;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Sam Adams
 */
public class AcsIssueParserTest {

    private static final PublisherId ACS = new PublisherId("acs");
    private static final Journal JACSAT = new Journal(ACS, "jacsat", "Journal of the American Chemical Society");
    private static final Journal INOCAJ = new Journal(ACS, "inocaj", "Inorganic Chemistry");
    private static final Journal JCEAXX = new Journal(ACS, "jceaax", "Journal of Chemical & Engineering Data");

    private Document loadDocument(String path) throws Exception {
        final InputStream in  = ResourceUtil.open(getClass(), "/wwmm/pubcrawler/crawlers/acs/" + path);
        try {
            return new Builder(new Parser()).build(in);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    protected AcsIssueTocParser getJacsIssue132_51() throws Exception {
        return new AcsIssueTocParser(loadDocument("jacs-132-51.html"), URI.create("http://pubs.acs.org/toc/jacsat/132/51"), JACSAT);
    }

    protected AcsIssueTocParser getInocajLegacyIssue() throws Exception {
        return new AcsIssueTocParser(loadDocument("inocaj-34-25.html"), URI.create("http://pubs.acs.org/toc/inocaj/34/25"), INOCAJ);
    }

    protected AcsIssueTocParser getJceaaxIssue55_9() throws Exception {
        return new AcsIssueTocParser(loadDocument("jceaax_55_9.html"), URI.create("http://pubs.acs.org/toc/jceaax/55/9"), JCEAXX);
    }

    protected AcsIssueTocParser getJceaaxIssue53_9() throws Exception {
        return new AcsIssueTocParser(loadDocument("jceaax_53_9.html"), URI.create("http://pubs.acs.org/toc/jceaax/53/9"), JCEAXX);
    }

    protected AcsIssueTocParser getInocajIssue39_19() throws Exception {
        return new AcsIssueTocParser(loadDocument("inocaj-39-19.html"), URI.create("http://pubs.acs.org/toc/inocaj/39/19"), INOCAJ);
    }

    @Test
    public void testGetArticleDois() throws Exception {
        AcsIssueTocParser crawler = getJacsIssue132_51();
        List<Article> articles = crawler.getArticles();
        assertEquals(64, articles.size());
        assertEquals(new Doi("10.1021/ja104809x"), articles.get(0).getDoi());
        assertEquals(new Doi("10.1021/ja110154r"), articles.get(63).getDoi());
    }

    @Test
    public void testGetBasicArticleHtmlTitles() throws Exception {
        AcsIssueTocParser crawler = getJacsIssue132_51();
        List<Article> articles = crawler.getArticles();
        assertEquals(64, articles.size());
        assertEquals("<h1 xmlns=\"http://www.w3.org/1999/xhtml\">Accumulative Charge Separation Inspired by Photosynthesis</h1>",
                articles.get(0).getTitleHtml());
    }

    @Test
    public void testGetArticleHtmlTitlesWithEntities() throws Exception {
        AcsIssueTocParser crawler = getJacsIssue132_51();
        List<Article> articles = crawler.getArticles();
        assertEquals(64, articles.size());
        assertEquals("<h1 xmlns=\"http://www.w3.org/1999/xhtml\">Direct Assembly of Polyarenes via C\u2212C Coupling Using PIFA/BF<sub>3</sub>\u00B7Et<sub>2</sub>O</h1>",
                articles.get(1).getTitleHtml());
    }

    @Test
    public void testGetArticleAuthorsWithEntities() throws Exception {
        AcsIssueTocParser crawler = getJacsIssue132_51();
        List<Article> articles = crawler.getArticles();
        assertEquals(64, articles.size());
        List<String> authors = articles.get(0).getAuthors();
        assertEquals(7, authors.size());
        assertEquals("Susanne Karlsson", authors.get(0));
        assertEquals("Julien Boixel", authors.get(1));
        assertEquals("Yann Pellegrin", authors.get(2));
        assertEquals("Errol Blart", authors.get(3));
        assertEquals("Hans-Christian Becker", authors.get(4));
        assertEquals("Fabrice Odobel", authors.get(5));
        assertEquals("Leif Hammarstr\u00f6m", authors.get(6));
    }

    @Test
    public void testGetPreviousIssue() throws Exception {
        AcsIssueTocParser crawler = getJacsIssue132_51();
        Issue prev = crawler.getPreviousIssue();
        assertNotNull(prev);
        assertEquals("acs/jacsat/132/50", prev.getId().getValue());
        assertEquals(URI.create("http://pubs.acs.org/toc/jacsat/132/50"), prev.getUrl());
    }

    @Test
    public void testGetVolume() throws Exception {
        AcsIssueTocParser crawler = getJacsIssue132_51();
        assertEquals("132", crawler.getVolume());
    }

    @Test
    public void testGetNumber() throws Exception {
        AcsIssueTocParser crawler = getJacsIssue132_51();
        assertEquals("51", crawler.getNumber());
    }

    @Test
    public void testGetYear() throws Exception {
        AcsIssueTocParser crawler = getJacsIssue132_51();
        assertEquals("2010", crawler.getYear());
    }

    @Test
    public void testGetJournalAbbreviation() throws Exception {
        AcsIssueTocParser crawler = getJacsIssue132_51();
        assertEquals("jacsat", crawler.getJournalAbbreviation());
    }

    @Test
    public void testGetDate() throws Exception {
        AcsIssueTocParser issue = getJacsIssue132_51();
        assertEquals(new LocalDate(2010, 12, 29), issue.getDate());
    }

    @Test
    public void testToIssue() throws Exception {
        AcsIssueTocParser crawler = getJacsIssue132_51();
        Issue issue = crawler.toIssue();
        assertNotNull(issue);
        assertEquals("acs/jacsat/132/51", issue.getId().getValue());
        assertEquals("2010", issue.getYear());
        assertEquals("132", issue.getVolume());
        assertEquals("51", issue.getNumber());
        assertNotNull(issue.getArticles());
        assertEquals(64, issue.getArticles().size());
        assertNotNull(issue.getPreviousIssue());
        assertEquals("acs/jacsat/132/50", issue.getPreviousIssue().getId().getValue());
    }

    @Test
    public void testGetYearFromLegacyIssue() throws Exception {
        AcsIssueTocParser crawler = getInocajLegacyIssue();
        assertEquals("1995", crawler.getYear());
    }

    @Test
    public void testGetInocaj39_19ArticleDois() throws Exception {
        AcsIssueTocParser crawler = getInocajIssue39_19();
        List<Article> articles = crawler.getArticles();
        assertEquals(30, articles.size());
    }

    @Test
    public void testGetJceaax55_9_Year() throws Exception {
        AcsIssueTocParser crawler = getJceaaxIssue55_9();
        assertEquals("2010", crawler.getYear());
    }

    @Test
    public void testGetJceaax53_9_Year() throws Exception {
        AcsIssueTocParser crawler = getJceaaxIssue53_9();
        assertEquals(null, crawler.getYear());
    }

}
