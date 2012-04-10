package wwmm.pubcrawler.crawlers.elsevier.parsers;

import nu.xom.Builder;
import nu.xom.Document;
import org.apache.commons.io.IOUtils;
import org.ccil.cowan.tagsoup.Parser;
import org.junit.AfterClass;
import org.junit.Test;
import wwmm.pubcrawler.crawlers.elsevier.Elsevier;
import wwmm.pubcrawler.model.Issue;
import wwmm.pubcrawler.model.id.JournalId;

import java.io.InputStream;
import java.net.URI;

import static org.junit.Assert.assertEquals;

/**
 * @author Sam Adams
 */
public class ElsevierIssueTocParserTest {

    private static final JournalId JOURNAL_09254005 = new JournalId(Elsevier.PUBLISHER_ID, "09254005");
    
    private static Document journal09254005;
    private static Document journal01677322;

    @AfterClass
    public static void afterAllTests() {
        journal09254005 = null;
        journal01677322 = null;
    }

    @Test
    public void testGetJournalTitle() throws Exception {
        ElsevierIssueTocParser parser = getJournalParser09254005_165_1();
        assertEquals("Sensors and Actuators B: Chemical", parser.getJournalTitle());
    }

    @Test
    public void testGetVolume() throws Exception {
        ElsevierIssueTocParser parser = getJournalParser09254005_165_1();
        assertEquals("165", parser.getVolume());
    }

    @Test
    public void testGetNumber() throws Exception {
        ElsevierIssueTocParser parser = getJournalParser09254005_165_1();
        assertEquals("1", parser.getNumber());
    }

    @Test
    public void testGetNumberWhenMissing() throws Exception {
        ElsevierIssueTocParser parser = getJournalParser01677322_168();
        assertEquals(Issue.NULL_NUMBER, parser.getNumber());
    }

    @Test
    public void testGetArticleNodes() throws Exception {
        ElsevierIssueTocParser parser = getJournalParser09254005_165_1();
        assertEquals(26, parser.getArticleNodes().size());
    }

    @Test
    public void testGetPreviousIssueLink() throws Exception {
        ElsevierIssueTocParser parser = getJournalParser09254005_165_1();
        Issue issueRef = parser.getPreviousIssue();
        assertEquals("164", issueRef.getVolume());
        assertEquals("1", issueRef.getNumber());
        assertEquals(URI.create("http://www.sciencedirect.com/science/journal/09254005/164/1"), issueRef.getUrl());
    }

    @Test
    public void testGetPreviousIssueLinkWithoutNumber() throws Exception {
        ElsevierIssueTocParser parser = getJournalParser01677322_168();
        Issue issueRef = parser.getPreviousIssue();
        assertEquals("167", issueRef.getVolume());
        assertEquals(Issue.NULL_NUMBER, issueRef.getNumber());
        assertEquals(URI.create("http://www.sciencedirect.com/science/journal/01677322/167"), issueRef.getUrl());
    }

    private ElsevierIssueTocParser getJournalParser09254005_165_1() throws Exception {
        Document journal = journal09254005;
        if (journal == null) {
            journal = loadHtml("09254005-165-1.html");
            journal09254005 = journal;
        }
        return new ElsevierIssueTocParser(journal, URI.create("http://www.sciencedirect.com/science/journal/09254005/165/1"), JOURNAL_09254005);
    }

    private ElsevierIssueTocParser getJournalParser01677322_168() throws Exception {
        Document journal = journal01677322;
        if (journal == null) {
            journal = loadHtml("01677322-168.html");
            journal01677322 = journal;
        }
        return new ElsevierIssueTocParser(journal, URI.create("http://www.sciencedirect.com/science/journal/01677322/168"), JOURNAL_09254005);
    }

    private Document loadHtml(final String filename) throws Exception {
        Builder builder = new Builder(new Parser());
        InputStream in = getClass().getResourceAsStream(filename);
        try {
            return builder.build(in);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }


}
