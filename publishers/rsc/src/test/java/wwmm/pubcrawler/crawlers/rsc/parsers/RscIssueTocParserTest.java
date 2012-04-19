package wwmm.pubcrawler.crawlers.rsc.parsers;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Node;
import org.apache.commons.io.IOUtils;
import org.ccil.cowan.tagsoup.Parser;
import org.junit.AfterClass;
import org.junit.Test;
import wwmm.pubcrawler.crawlers.rsc.Rsc;
import wwmm.pubcrawler.model.Issue;
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
public class RscIssueTocParserTest {

    private static final JournalId CHEMICAL_SCIENCE = new JournalId(Rsc.PUBLISHER_ID, "sc");

    private static Document scLatest;

    @AfterClass
    public static void afterAllTests() {
        scLatest = null;
    }

    @Test
    public void testGetJournalTitle() throws Exception {
        RscIssueTocParser parser = getJournalParserScLatest();
        assertEquals("Chemical Science", parser.getJournalTitle());
    }

    @Test
    public void testGetVolume() throws Exception {
        RscIssueTocParser parser = getJournalParserScLatest();
        assertEquals("3", parser.getVolume());
    }

    @Test
    public void testGetNumber() throws Exception {
        RscIssueTocParser parser = getJournalParserScLatest();
        assertEquals("5", parser.getNumber());
    }

    @Test
    public void testGetYear() throws Exception {
        RscIssueTocParser parser = getJournalParserScLatest();
        assertEquals("2012", parser.getYear());
    }

    @Test
    public void testGetArticleNodes() throws Exception {
        RscIssueTocParser parser = getJournalParserScLatest();
        assertEquals(52, parser.getArticleNodes().size());
    }

    @Test
    public void testGetArticleTitle() throws Exception {
        RscIssueTocParser parser = getJournalParserScLatest();
        List<Node> nodes = parser.getArticleNodes();
        assertEquals("Biological stimuli and biomolecules in the assembly and manipulation of nanoscale polymeric particles", parser.getArticleTitle(nodes.get(3)));
    }

    @Test
    public void testGetArticleAuthors() throws Exception {
        RscIssueTocParser parser = getJournalParserScLatest();
        List<Node> nodes = parser.getArticleNodes();
        assertEquals(asList("Lyndsay M. Randolph", "Miao-Ping Chien", "Nathan C. Gianneschi"), parser.getArticleAuthors(nodes.get(3)));
    }

    @Test
    public void testGetArticlePages() throws Exception {
        RscIssueTocParser parser = getJournalParserScLatest();
        List<Node> nodes = parser.getArticleNodes();
        assertEquals("1363-1380", parser.findArticlePages(nodes.get(3)));
    }

    @Test
    public void testGetArticleUrl() throws Exception {
        RscIssueTocParser parser = getJournalParserScLatest();
        List<Node> nodes = parser.getArticleNodes();
        assertEquals(URI.create("http://pubs.rsc.org/en/content/articlelanding/2012/sc/c2sc00857b"), parser.getArticleUrl(nodes.get(3)));
    }

    @Test
    public void testGetArticleId() throws Exception {
        RscIssueTocParser parser = getJournalParserScLatest();
        List<Node> nodes = parser.getArticleNodes();
        assertEquals(new ArticleId(CHEMICAL_SCIENCE, "C2SC00857B"), parser.getArticleId(nodes.get(3)));
    }

    @Test
    public void testGetArticleDoi() throws Exception {
        RscIssueTocParser parser = getJournalParserScLatest();
        List<Node> nodes = parser.getArticleNodes();
        assertEquals(new Doi("10.1039/C2SC00857B"), parser.getArticleDoi(nodes.get(3)));
    }

    @Test
    public void testGetPreviousIssueLink() throws Exception {
        RscIssueTocParser parser = getJournalParserScLatest();
        Issue issueRef = parser.getPreviousIssue();
        assertEquals(new IssueId(CHEMICAL_SCIENCE, "3", "4"), issueRef.getId());
        assertEquals("Chemical Science", issueRef.getJournalTitle());
        assertEquals(URI.create("http://pubs.rsc.org/en/journals/journal/sc?issueid=sc003004&issnprint=2041-6520"), issueRef.getUrl());
        assertEquals("3", issueRef.getVolume());
        assertEquals("4", issueRef.getNumber());
    }

    private RscIssueTocParser getJournalParserScLatest() throws Exception {
        Document journal = scLatest;
        if (journal == null) {
            journal = loadHtml("sc-latest.html");
            scLatest = journal;
        }
        return new RscIssueTocParser(journal, URI.create("http://pubs.rsc.org/en/journals/journal/sc?issueid=latest&jname=Chemical%20Science&isarchive=False&issnprint=2041-6520&issnonline=2041-6539&iscontentavailable=True"), CHEMICAL_SCIENCE);
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
