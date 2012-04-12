package wwmm.pubcrawler.crawlers.acs.parsers;

import nu.xom.Builder;
import nu.xom.Document;
import org.apache.commons.io.IOUtils;
import org.ccil.cowan.tagsoup.Parser;
import org.joda.time.LocalDate;
import org.junit.AfterClass;
import org.junit.Test;
import wwmm.pubcrawler.model.Article;
import wwmm.pubcrawler.model.Issue;
import wwmm.pubcrawler.model.id.JournalId;
import wwmm.pubcrawler.model.id.PublisherId;
import wwmm.pubcrawler.types.Doi;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Sam Adams
 */
public class AcsIssueTocParserTest {

    private static final PublisherId ACS = new PublisherId("acs");
    private static final JournalId JACSAT = new JournalId(ACS, "jacsat");
    private static final JournalId INOCAJ = new JournalId(ACS, "inocaj");
    private static final JournalId JCEAXX = new JournalId(ACS, "jceaax");

    private static Document journalJacs132_51;
    private static Document journalInocajLegacy;
    private static Document journalJceaax55_9;
    private static Document journalJceaax53_9;
    private static Document journalInocaj39_19;

    @AfterClass
    public static void afterAllTests() {
        journalJacs132_51 = null;
        journalInocajLegacy = null;
        journalJceaax55_9 = null;
        journalJceaax53_9 = null;
        journalInocaj39_19 = null;
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
        assertEquals("acs/jacsat/132/50", prev.getId().getUid());
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
        Issue issue = crawler.getIssueDetails();
        assertNotNull(issue);
        assertEquals("acs/jacsat/132/51", issue.getId().getUid());
        assertEquals("2010", issue.getYear());
        assertEquals("132", issue.getVolume());
        assertEquals("51", issue.getNumber());
        assertNotNull(issue.getArticles());
        assertEquals(64, issue.getArticles().size());
        assertNotNull(issue.getPreviousIssue());
        assertEquals("acs/jacsat/132/50", issue.getPreviousIssue().getId().getUid());
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

    protected AcsIssueTocParser getJacsIssue132_51() throws Exception {
        Document journal = journalJacs132_51;
        if (journal == null) {
            journal = loadHtml("jacs-132-51.html");
            journalJacs132_51 = journal;
        }
        return new AcsIssueTocParser(journal, URI.create("http://pubs.acs.org/toc/jacsat/132/51"), JACSAT);
    }

    protected AcsIssueTocParser getInocajLegacyIssue() throws Exception {
        Document journal = journalInocajLegacy;
        if (journal == null) {
            journal = loadHtml("inocaj-34-25.html");
            journalInocajLegacy = journal;
        }
        return new AcsIssueTocParser(journal, URI.create("http://pubs.acs.org/toc/inocaj/34/25"), INOCAJ);
    }

    protected AcsIssueTocParser getJceaaxIssue55_9() throws Exception {
        Document journal = journalJceaax55_9;
        if (journal == null) {
            journal = loadHtml("jceaax_55_9.html");
            journalJceaax55_9 = journal;
        }
        return new AcsIssueTocParser(journal, URI.create("http://pubs.acs.org/toc/jceaax/55/9"), JCEAXX);
    }

    protected AcsIssueTocParser getJceaaxIssue53_9() throws Exception {
        Document journal = journalJceaax53_9;
        if (journal == null) {
            journal = loadHtml("jceaax_53_9.html");
            journalJceaax53_9 = journal;
        }
        return new AcsIssueTocParser(journal, URI.create("http://pubs.acs.org/toc/jceaax/53/9"), JCEAXX);
    }

    protected AcsIssueTocParser getInocajIssue39_19() throws Exception {
        Document journal = journalInocaj39_19;
        if (journal == null) {
            journal = loadHtml("inocaj-39-19.html");
            journalInocaj39_19 = journal;
        }
        return new AcsIssueTocParser(journal, URI.create("http://pubs.acs.org/toc/inocaj/39/19"), INOCAJ);
    }

    private Document loadHtml(final String filename) throws Exception {
        Builder builder = new Builder(new Parser());
        InputStream in = getClass().getResourceAsStream(filename);
        try {
            return builder.build(new InputStreamReader(in, "UTF-8"));
        } finally {
            IOUtils.closeQuietly(in);
        }
    }
}
