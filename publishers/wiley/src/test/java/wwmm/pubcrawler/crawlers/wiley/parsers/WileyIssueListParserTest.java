package wwmm.pubcrawler.crawlers.wiley.parsers;

import nu.xom.Builder;
import nu.xom.Document;
import org.apache.commons.io.IOUtils;
import org.ccil.cowan.tagsoup.Parser;
import org.junit.AfterClass;
import org.junit.Test;
import wwmm.pubcrawler.crawlers.wiley.Wiley;
import wwmm.pubcrawler.model.IssueLink;
import wwmm.pubcrawler.model.id.IssueId;
import wwmm.pubcrawler.model.id.JournalId;
import wwmm.pubcrawler.parsers.IssueListParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author Sam Adams
 */
public class WileyIssueListParserTest {

    private static final JournalId JOURNAL_ID = new JournalId(Wiley.PUBLISHER_ID, "1611-0218");

    private static Document issueList;

    @AfterClass
    public static void afterAllTests() {
        issueList = null;
    }

    @Test
    public void testGetIssueLinks() throws Exception {
        IssueListParser parser = getIssueListParser();
        assertEquals(11, parser.findIssues().size());
    }

    @Test
    public void testGetIssueIds() throws Exception {
        IssueListParser parser = getIssueListParser();
        final List<IssueLink> issueLinks = parser.findIssues();
        assertEquals(new IssueId(JOURNAL_ID, "27", "11-12"), issueLinks.get(0).getIssueId());
        assertEquals(new IssueId(JOURNAL_ID, "27", "10"), issueLinks.get(1).getIssueId());
        assertEquals(new IssueId(JOURNAL_ID, "27", "9"), issueLinks.get(2).getIssueId());
        assertEquals(new IssueId(JOURNAL_ID, "27", "8"), issueLinks.get(3).getIssueId());
        assertEquals(new IssueId(JOURNAL_ID, "27", "7"), issueLinks.get(4).getIssueId());
        assertEquals(new IssueId(JOURNAL_ID, "27", "6"), issueLinks.get(5).getIssueId());
        assertEquals(new IssueId(JOURNAL_ID, "27", "5"), issueLinks.get(6).getIssueId());
        assertEquals(new IssueId(JOURNAL_ID, "27", "4"), issueLinks.get(7).getIssueId());
        assertEquals(new IssueId(JOURNAL_ID, "27", "3"), issueLinks.get(8).getIssueId());
        assertEquals(new IssueId(JOURNAL_ID, "27", "2"), issueLinks.get(9).getIssueId());
        assertEquals(new IssueId(JOURNAL_ID, "27", "1"), issueLinks.get(10).getIssueId());
    }

    private WileyIssueListParser getIssueListParser() throws Exception {
        Document html = issueList;
        if (html == null) {
            html = loadHtml("issue-list.html");
            issueList = html;
        }
        return new WileyIssueListParser(html, URI.create("http://onlinelibrary.wiley.com/journal/10.1002/(ISSN)1611-0218/issues/fragment?activeYear=2008&SKIP_DECORATION=true"), JOURNAL_ID);
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
