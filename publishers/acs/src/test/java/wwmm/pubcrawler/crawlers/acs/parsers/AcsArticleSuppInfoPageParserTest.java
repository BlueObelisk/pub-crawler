package wwmm.pubcrawler.crawlers.acs.parsers;

import nu.xom.Builder;
import nu.xom.Document;
import org.apache.commons.io.IOUtils;
import org.ccil.cowan.tagsoup.Parser;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;
import wwmm.pubcrawler.model.Article;
import wwmm.pubcrawler.model.id.ArticleId;
import wwmm.pubcrawler.types.Doi;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

/**
 * @author Sam Adams
 */
public class AcsArticleSuppInfoPageParserTest {

    private static Document jo1013564;

    @AfterClass
    public static void afterAllTests() {
        jo1013564 = null;
    }

    @Test
    public void testGetJournalTitle() throws Exception {
        AcsArticleSuppInfoPageParser parser = getArticleJo1013564();
        assertEquals("The Journal of Organic Chemistry", parser.getReference().getJournalTitle());
    }

    @Test
    public void testGetJournalTitleAbbreviated() throws Exception {
        AcsArticleSuppInfoPageParser parser = getArticleJo1013564();
        assertEquals("J. Org. Chem.", parser.getReference().getAbbreviatedJournalTitle());
    }

    @Test
    public void testGetIssueVolume() throws Exception {
        AcsArticleSuppInfoPageParser parser = getArticleJo1013564();
        assertEquals("75", parser.getReference().getVolume());
    }

    @Test
    public void testGetIssueNumber() throws Exception {
        AcsArticleSuppInfoPageParser parser = getArticleJo1013564();
        assertEquals("23", parser.getReference().getNumber());
    }

    @Test
    public void testGetIssueYear() throws Exception {
        AcsArticleSuppInfoPageParser parser = getArticleJo1013564();
        assertEquals("2010", parser.getReference().getYear());
    }

    @Test
    public void testGetPages() throws Exception {
        AcsArticleSuppInfoPageParser parser = getArticleJo1013564();
        assertEquals("8012–8023", parser.getReference().getPages());
    }

    @Test
    public void testGetArticleTitle() throws Exception {
        AcsArticleSuppInfoPageParser parser = getArticleJo1013564();
        assertEquals("Grassypeptolides A−C, Cytotoxic Bis-thiazoline Containing Marine Cyclodepsipeptides", parser.getTitle());
    }

    @Test
    public void testGetAuthors() throws Exception {
        AcsArticleSuppInfoPageParser parser = getArticleJo1013564();
        assertEquals(asList("Jason C. Kwan", "Ranjala Ratnayake", "Khalil A. Abboud", "Valerie J. Paul", "Hendrik Luesch"), parser.getAuthors());
    }

    protected AcsArticleSuppInfoPageParser getArticleJo1013564() throws Exception {
        Article article = new Article();
        article.setId(new ArticleId("acs/joceah/75/23/jo1013564"));
        article.setDoi(new Doi("10.1021/jo1013564"));

        Document doc = jo1013564;
        if (doc == null) {
            doc = loadDocument("jo1013564_supp.html");
            jo1013564 = doc;
        }
        return new AcsArticleSuppInfoPageParser(article, doc, URI.create("http://pubs.acs.org/doi/abs/10.1021/jo1013564"));
    }

    private Document loadDocument(String filename) throws Exception {
        Builder builder = new Builder(new Parser());
        InputStream in = getClass().getResourceAsStream(filename);
        try {
            return builder.build(new InputStreamReader(in, "UTF-8"));
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

}
