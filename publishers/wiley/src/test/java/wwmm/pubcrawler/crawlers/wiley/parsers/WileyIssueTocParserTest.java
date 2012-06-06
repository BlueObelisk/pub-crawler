package wwmm.pubcrawler.crawlers.wiley.parsers;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Node;
import org.apache.commons.io.IOUtils;
import org.ccil.cowan.tagsoup.Parser;
import org.junit.AfterClass;
import org.junit.Test;
import wwmm.pubcrawler.crawlers.wiley.Wiley;
import wwmm.pubcrawler.model.IssueLink;
import wwmm.pubcrawler.model.id.ArticleId;
import wwmm.pubcrawler.model.id.IssueId;
import wwmm.pubcrawler.model.id.JournalId;
import wwmm.pubcrawler.types.Doi;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

/**
 * @author Sam Adams
 */
public class WileyIssueTocParserTest {

    private static final JournalId JOURNAL_15360687 = new JournalId(Wiley.PUBLISHER_ID, "1536-0687");

    private static Document journal15360687;

    @AfterClass
    public static void afterAllTests() {
        journal15360687 = null;
    }

    @Test
    public void testGetJournalTitle() throws Exception {
        WileyIssueTocParser parser = getJournalParser15360687_16_4();
        assertEquals("About Campus", parser.getJournalTitle());
    }

    @Test
    public void testGetVolume() throws Exception {
        WileyIssueTocParser parser = getJournalParser15360687_16_4();
        assertEquals("16", parser.getVolume());
    }

    @Test
    public void testGetNumber() throws Exception {
        WileyIssueTocParser parser = getJournalParser15360687_16_4();
        assertEquals("4", parser.getNumber());
    }

    @Test
    public void testGetYear() throws Exception {
        WileyIssueTocParser parser = getJournalParser15360687_16_4();
        assertEquals("2011", parser.getYear());
    }

    @Test
    public void testGetArticleNodes() throws Exception {
        WileyIssueTocParser parser = getJournalParser15360687_16_4();
        assertEquals(6, parser.getArticleNodes().size());
    }

    @Test
    public void testGetArticleTitle() throws Exception {
        WileyIssueTocParser parser = getJournalParser15360687_16_4();
        List<Node> nodes = parser.getArticleNodes();
        assertEquals("Multiracial in a monoracial world: Student stories of racial dissolution on the colorblind campus", parser.getArticleTitle(nodes.get(3)));
    }

    @Test
    public void testGetArticleAuthors() throws Exception {
        WileyIssueTocParser parser = getJournalParser15360687_16_4();
        List<Node> nodes = parser.getArticleNodes();
        assertEquals(asList("Samuel D. Museus", "April L. Yee", "Susan A. Lambe"), parser.getArticleAuthors(nodes.get(3)));
    }

    @Test
    public void testGetArticlePages() throws Exception {
        WileyIssueTocParser parser = getJournalParser15360687_16_4();
        List<Node> nodes = parser.getArticleNodes();
        assertEquals("20-25", parser.findArticlePages(nodes.get(3)));
    }

    @Test
    public void testGetArticleDoi() throws Exception {
        WileyIssueTocParser parser = getJournalParser15360687_16_4();
        List<Node> nodes = parser.getArticleNodes();
        assertEquals(new Doi("10.1002/abc.20070"), parser.getArticleDoi(nodes.get(3)));
    }

    @Test
    public void testGetArticleUrl() throws Exception {
        WileyIssueTocParser parser = getJournalParser15360687_16_4();
        List<Node> nodes = parser.getArticleNodes();
        assertEquals(URI.create("http://onlinelibrary.wiley.com/doi/10.1002/abc.20070/abstract"), parser.getArticleUrl(nodes.get(3)));
    }

    @Test
    public void testGetArticleId() throws Exception {
        WileyIssueTocParser parser = getJournalParser15360687_16_4();
        List<Node> nodes = parser.getArticleNodes();
        assertEquals(new ArticleId(JOURNAL_15360687, "abc.20070"), parser.getArticleId(nodes.get(3)));
    }

    @Test
    public void testGetPreviousIssueLink() throws Exception {
        WileyIssueTocParser parser = getJournalParser15360687_16_4();
        IssueLink issueRef = parser.getPreviousIssue();
        assertEquals("16", issueRef.getVolume());
        assertEquals("3", issueRef.getNumber());
        assertEquals(new IssueId(JOURNAL_15360687, "16", "3"), issueRef.getIssueId());
        assertEquals(URI.create("http://onlinelibrary.wiley.com/doi/10.1002/abc.v16.3/issuetoc"), issueRef.getUrl());
    }

    private WileyIssueTocParser getJournalParser15360687_16_4() throws Exception {
        Document journal = journal15360687;
        if (journal == null) {
            journal = loadHtml("1536-0687_16_4.html");
            journal15360687 = journal;
        }
        return new WileyIssueTocParser(journal, URI.create("http://onlinelibrary.wiley.com/doi/10.1002/abc.v16.4/issuetoc"), JOURNAL_15360687);
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
