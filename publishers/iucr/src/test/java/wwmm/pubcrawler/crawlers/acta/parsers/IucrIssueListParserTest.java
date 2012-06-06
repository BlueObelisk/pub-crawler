package wwmm.pubcrawler.crawlers.acta.parsers;

import nu.xom.Builder;
import nu.xom.Document;
import org.apache.commons.io.IOUtils;
import org.ccil.cowan.tagsoup.Parser;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import wwmm.pubcrawler.crawlers.acta.Iucr;
import wwmm.pubcrawler.model.IssueLink;
import wwmm.pubcrawler.model.id.IssueId;
import wwmm.pubcrawler.model.id.JournalId;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author Sam Adams
 */
public class IucrIssueListParserTest {

    private static final URI URL = URI.create("http://journals.iucr.org/d/contents/backissuesbdy.html");
    private static final JournalId ACTA_D = new JournalId(Iucr.PUBLISHER_ID, "d");

    private static Document backIssues;

    @BeforeClass
    public static void beforeAnyTests() throws Exception {
        backIssues = loadHtml("d-backissues.html");
    }

    @AfterClass
    public static void afterAllTests() {
        backIssues = null;
    }

    @Test
    public void testFindIssues() throws Exception {
        IucrIssueListParser parser = new IucrIssueListParser(backIssues, URL, ACTA_D);
        List<IssueLink> list = parser.findIssues();
        assertEquals(200, list.size());
    }
    
    @Test
    public void testFindIssueId() throws Exception {
        IucrIssueListParser parser = new IucrIssueListParser(backIssues, URL, ACTA_D);
        List<IssueLink> list = parser.findIssues();
        assertEquals(new IssueId(ACTA_D, "2012", "02-00"), list.get(2).getIssueId());
    }

    @Test
    public void testFindIssueVolume() throws Exception {
        IucrIssueListParser parser = new IucrIssueListParser(backIssues, URL, ACTA_D);
        List<IssueLink> list = parser.findIssues();
        assertEquals("68", list.get(2).getVolume());
    }

    @Test
    public void testFindIssueVolumeWithLineBreak() throws Exception {
        IucrIssueListParser parser = new IucrIssueListParser(backIssues, URL, ACTA_D);
        List<IssueLink> list = parser.findIssues();
        assertEquals("61", list.get(82).getVolume());
    }

    @Test
    public void testFindIssueNumber() throws Exception {
        IucrIssueListParser parser = new IucrIssueListParser(backIssues, URL, ACTA_D);
        List<IssueLink> list = parser.findIssues();
        assertEquals("2", list.get(2).getNumber());
    }

    @Test
    public void testFindIssueUrl() throws Exception {
        IucrIssueListParser parser = new IucrIssueListParser(backIssues, URL, ACTA_D);
        List<IssueLink> list = parser.findIssues();
        assertEquals(URI.create("http://journals.iucr.org/d/issues/2012/02/00/issconts.html"), list.get(2).getUrl());
    }

    private static Document loadHtml(final String filename) throws Exception {
        Builder builder = new Builder(new Parser());
        InputStream in = IucrPublicationListParserTest.class.getResourceAsStream(filename);
        try {
            return builder.build(new InputStreamReader(in, "UTF-8"));
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

}
