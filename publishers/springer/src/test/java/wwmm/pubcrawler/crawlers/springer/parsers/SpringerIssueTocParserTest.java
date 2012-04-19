package wwmm.pubcrawler.crawlers.springer.parsers;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Node;
import org.apache.commons.io.IOUtils;
import org.ccil.cowan.tagsoup.Parser;
import org.junit.AfterClass;
import org.junit.Test;
import wwmm.pubcrawler.crawlers.springer.Springer;
import wwmm.pubcrawler.model.Issue;
import wwmm.pubcrawler.model.id.ArticleId;
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
public class SpringerIssueTocParserTest {

    private static final JournalId JOURNAL_10009116 = new JournalId(Springer.PUBLISHER_ID, "1000-9116");

    private static Document journal10009116;

    @AfterClass
    public static void afterAllTests() {
        journal10009116 = null;
    }

    @Test
    public void testGetJournalTitle() throws Exception {
        SpringerIssueTocParser parser = getJournalParser10009116_21_6();
        assertEquals("Acta Seismologica Sinica", parser.getJournalTitle());
    }

    @Test
    public void testGetVolume() throws Exception {
        SpringerIssueTocParser parser = getJournalParser10009116_21_6();
        assertEquals("21", parser.getVolume());
    }

    @Test
    public void testGetNumber() throws Exception {
        SpringerIssueTocParser parser = getJournalParser10009116_21_6();
        assertEquals("6", parser.getNumber());
    }

    @Test
    public void testGetArticleNodes() throws Exception {
        SpringerIssueTocParser parser = getJournalParser10009116_21_6();
        assertEquals(13, parser.getArticleNodes().size());
    }

    @Test
    public void testGetArticleTitle() throws Exception {
        SpringerIssueTocParser parser = getJournalParser10009116_21_6();
        List<Node> nodes = parser.getArticleNodes();
        assertEquals("Characteristics of present-day active strain field of Chinese mainland", parser.getArticleTitle(nodes.get(1)));
    }

    @Test
    public void testGetArticleAuthors() throws Exception {
        SpringerIssueTocParser parser = getJournalParser10009116_21_6();
        List<Node> nodes = parser.getArticleNodes();
        assertEquals(asList("Liang-qian Guo", "Yan-xing Li", "Guo-hua Yang", "Xin-kang Hu"), parser.getArticleAuthors(nodes.get(1)));
    }

    @Test
    public void testGetArticlePages() throws Exception {
        SpringerIssueTocParser parser = getJournalParser10009116_21_6();
        List<Node> nodes = parser.getArticleNodes();
        assertEquals("562-572", parser.findArticlePages(nodes.get(1)));
    }
    
    @Test
    public void testGetArticleUrl() throws Exception {
        SpringerIssueTocParser parser = getJournalParser10009116_21_6();
        List<Node> nodes = parser.getArticleNodes();
        assertEquals(URI.create("http://www.springerlink.com/content/j6837536j36p461t/"), parser.getArticleUrl(nodes.get(1)));
    }

    @Test
    public void testGetArticleId() throws Exception {
        SpringerIssueTocParser parser = getJournalParser10009116_21_6();
        List<Node> nodes = parser.getArticleNodes();
        assertEquals(new ArticleId(JOURNAL_10009116, "j6837536j36p461t"), parser.getArticleId(nodes.get(1)));
    }

    @Test
    public void testGetIssueLinks() throws Exception {
        SpringerIssueTocParser parser = getJournalParser10009116_21_6();
        List<Issue> links = parser.getIssueLinks();
        assertEquals(72, links.size());
        assertEquals("21", links.get(1).getVolume());
        assertEquals("4", links.get(1).getNumber());
        assertEquals(URI.create("http://www.springerlink.com/content/1000-9116/21/4/"), links.get(1).getUrl());
    }

    private SpringerIssueTocParser getJournalParser10009116_21_6() throws Exception {
        Document journal = journal10009116;
        if (journal == null) {
            journal = loadHtml("1000-9116_21_6.html");
            journal10009116 = journal;
        }
        return new SpringerIssueTocParser(journal, URI.create("http://www.springerlink.com/content/1000-9116/"), JOURNAL_10009116);
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
