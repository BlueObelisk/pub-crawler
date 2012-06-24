package wwmm.pubcrawler.crawlers.wiley.parsers;

import nu.xom.Builder;
import nu.xom.Document;
import org.apache.commons.io.IOUtils;
import org.ccil.cowan.tagsoup.Parser;
import org.junit.AfterClass;
import org.junit.Test;
import wwmm.pubcrawler.crawlers.wiley.Wiley;
import wwmm.pubcrawler.model.id.JournalId;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

/**
 * @author Sam Adams
 */
public class WileyIssueListIndexParserTest {

    private static final JournalId JOURNAL_ID = new JournalId(Wiley.PUBLISHER_ID, "1611-0218");

    private static Document issueList;

    @AfterClass
    public static void afterAllTests() {
        issueList = null;
    }

    @Test
    public void testGetIssueIds() throws Exception {
        WileyIssueListIndexParser parser = getIssueListParser();
        final List<String> years = parser.getIssueYears();
        assertEquals(asList("2012", "2011", "2010", "2009", "2008", "2007", "2006", "2005", "2004", "2003",
                            "2002", "2001", "2000", "1999", "1998", "1997", "1996", "1995", "1994", "1993",
                            "1992", "1991", "1990", "1989", "1988", "1987", "1986", "1985", "1984", "1983", "1982"), years);
    }

    private WileyIssueListIndexParser getIssueListParser() throws Exception {
        Document html = issueList;
        if (html == null) {
            html = loadHtml("issues.html");
            issueList = html;
        }
        return new WileyIssueListIndexParser(html, URI.create("http://onlinelibrary.wiley.com/journal/10.1002/(ISSN)1611-0218/issues/fragment?activeYear=2008&SKIP_DECORATION=true"));
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
