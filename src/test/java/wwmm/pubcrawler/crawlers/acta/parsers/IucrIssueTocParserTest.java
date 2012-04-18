package wwmm.pubcrawler.crawlers.acta.parsers;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Node;
import org.apache.commons.io.IOUtils;
import org.ccil.cowan.tagsoup.Parser;
import org.junit.AfterClass;
import org.junit.Ignore;
import org.junit.Test;
import wwmm.pubcrawler.crawlers.acta.Iucr;
import wwmm.pubcrawler.model.Article;
import wwmm.pubcrawler.model.Issue;
import wwmm.pubcrawler.model.SupplementaryResource;
import wwmm.pubcrawler.model.id.ArticleId;
import wwmm.pubcrawler.model.id.IssueId;
import wwmm.pubcrawler.model.id.JournalId;
import wwmm.pubcrawler.types.Doi;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.junit.Assert.*;

/**
 * @author Sam Adams
 */
public class IucrIssueTocParserTest {

    private static final JournalId ACTA_A = new JournalId(Iucr.PUBLISHER_ID, "a");
    private static final JournalId ACTA_B = new JournalId(Iucr.PUBLISHER_ID, "b");
    private static final JournalId ACTA_C = new JournalId(Iucr.PUBLISHER_ID, "c");
    private static final JournalId ACTA_E = new JournalId(Iucr.PUBLISHER_ID, "e");

    private static final URI URL_C_2005_10 = URI.create("http://journals.iucr.org/c/issues/2005/10/00/");
    private static final URI URL_B_1997_01 = URI.create("http://journals.iucr.org/b/issues/1997/01/00/");
    private static final URI URL_E_2004_11 = URI.create("http://journals.iucr.org/e/issues/2004/11/00/");
    private static final URI URL_E_2001_03 = URI.create("http://journals.iucr.org/e/issues/2011/03/00/");
    private static final URI URL_A_2010_06 = URI.create("http://journals.iucr.org/a/issues/2010/06/00/");
    private static final URI URL_B_2010_01 = URI.create("http://journals.iucr.org/b/issues/2010/01/00/");
    private static final URI URL_C_1997_03 = URI.create("http://journals.iucr.org/c/issues/1997/03/00/");
    private static final URI URL_A_2009_06 = URI.create("http://journals.iucr.org/a/issues/2009/06/00/");
    private static final URI URL_C_1991_10 = URI.create("http://journals.iucr.org/c/issues/1991/10/00/");

    private static ConcurrentMap<String,Document> cache = new ConcurrentHashMap<String, Document>();
    
    @AfterClass
    public static void afterAllTests() {
        cache = null;
    }

    @Test
    public void testPageWithVolumeInTitle() throws Exception {
        IucrIssueTocParser parser = getActaA2010_06();
        assertEquals("66", parser.getVolume());
    }

    @Test
    public void testGetIssueId() throws Exception {
        IucrIssueTocParser parser = getActaB2010_01();
        assertEquals(new IssueId(ACTA_B, "66", "1"), parser.getIssueId());
    }

    @Test
    public void testGetJournalTitle() throws Exception {
        IucrIssueTocParser parser = getActaB2010_01();
        assertEquals("Acta Crystallographica Section B: Structural Science", parser.findJournalTitle());
    }

    @Test
    public void testGetVolume() throws Exception {
        IucrIssueTocParser parser = getActaA2010_06();
        assertEquals("66", parser.getVolume());
    }

    @Test
    public void testGetNumber() throws Exception {
        IucrIssueTocParser parser = getActaB2010_01();
        assertEquals("1", parser.getNumber());
    }

    @Test
    public void testGetYear() throws Exception {
        IucrIssueTocParser parser = getActaB2010_01();
        assertEquals("2010", parser.getYear());
    }

    @Test
    public void testGetArticles() throws Exception {
        IucrIssueTocParser parser = getActaB2010_01();
        List<Article> articles = parser.getArticles();
        assertEquals(13, articles.size());
    }

    @Test
    public void testGetArticleIds() throws Exception {
        IucrIssueTocParser parser = getActaB2010_01();
        List<Article> articles = parser.getArticles();

        Article a0 = articles.get(0);
        assertEquals(new ArticleId(ACTA_B, "bk5091"), a0.getId());

        Article a7 = articles.get(7);
        assertEquals(new ArticleId(ACTA_B, "zb5008"), a7.getId());

        Article a12 = articles.get(12);
        assertEquals(new ArticleId(ACTA_B, "me0395"), a12.getId());
    }

    @Test
    public void testGetArticleDois() throws Exception {
        IucrIssueTocParser parser = getActaB2010_01();
        List<Article> articles = parser.getArticles();

        Article a0 = articles.get(0);
        assertEquals(new Doi("10.1107/S0108768109053981"), a0.getDoi());

        Article a7 = articles.get(7);
        assertEquals(new Doi("10.1107/S0108768109048769"), a7.getDoi());

        Article a12 = articles.get(12);
        assertEquals(new Doi("10.1107/S0108768109047855"), a12.getDoi());
    }

    @Test
    public void testGetArticleReferences() throws Exception {
        IucrIssueTocParser parser = getActaB2010_01();
        List<Article> articles = parser.getArticles();

        Article a0 = articles.get(0);
        assertEquals("1-16", a0.getReference().getPages());

        Article a7 = articles.get(7);
        assertEquals("69-75", a7.getReference().getPages());

        Article a12 = articles.get(12);
        assertEquals("104-108", a12.getReference().getPages());
    }


    @Test
    public void testArticleTitles() throws Exception {
        IucrIssueTocParser parser = getActaB2010_01();
        List<Article> articles = parser.getArticles();

        assertEquals("<h1>Polysomatic apatites</h1>", articles.get(0).getTitleHtml());
        assertEquals("<h1>A complicated quasicrystal approximant &#x3B5;<sub>16</sub> predicted by the strong-reflections approach</h1>", articles.get(1).getTitleHtml());
        assertEquals("<h1>Structures of incommensurate and commensurate composite crystals Rb<sub><i>x</i></sub>MnO<sub>2</sub> (<i>x</i> = 1.3711, 1.3636)</h1>", articles.get(2).getTitleHtml());
        assertEquals("<h1>Orientational disorder and phase transitions in crystals of dioxofluoromolybdate, (NH<sub>4</sub>)<sub>2</sub>MoO<sub>2</sub>F<sub>4</sub></h1>", articles.get(3).getTitleHtml());
        assertEquals("<h1>Octahedral tilting in cation-ordered Jahn-Teller distorted perovskites - a group-theoretical analysis</h1>", articles.get(4).getTitleHtml());
        assertEquals("<h1>Revision of the structure of Cs<sub>2</sub>CuSi<sub>5</sub>O<sub>12</sub> leucite as orthorhombic <i>Pbca</i></h1>", articles.get(5).getTitleHtml());
        assertEquals("<h1>Structure solution of the new titanate Li<sub>4</sub>Ti<sub>8</sub>Ni<sub>3</sub>O<sub>21</sub> using precession electron diffraction</h1>", articles.get(6).getTitleHtml());
        assertEquals("<h1>Concomitant polymorphic behavior of di-&#x3BC;-thiocyanato-&#x3BA;<sup>2</sup><i>N</i>:<i>S</i>;&#x3BA;<sup>2</sup><i>S</i>:<i>N</i>-bis[bis(tri-<i>p</i>-fluorophenylphosphine-&#x3BA;<i>P</i>)silver(I)]</h1>", articles.get(7).getTitleHtml());
        assertEquals("<h1><i>Ab initio</i> structure determination of phase II of racemic ibuprofen by X-ray powder diffraction</h1>", articles.get(8).getTitleHtml());
        assertEquals("<h1>Zur Kristallchemie von Graphen und Graphit</h1>", articles.get(9).getTitleHtml());
        assertEquals("<h1>Structures of dipeptides: the head-to-tail story</h1>", articles.get(10).getTitleHtml());
        assertEquals("<h1>A list of organic kryptoracemates</h1>", articles.get(11).getTitleHtml());
        assertEquals("<h1>Notes for authors 2010</h1>", articles.get(12).getTitleHtml());
    }

    @Test
    public void testArticleAuthors() throws Exception {
        IucrIssueTocParser parser = getActaB2010_01();
        List<Article> articles = parser.getArticles();

        assertEquals(Arrays.asList("T. Baikie", "S. S. Pramana", "C. Ferraris", "Y. Huang", "E. Kendrick", "K. Knight", "Z. Ahmad", "T. J. White"), articles.get(0).getAuthors());
        assertEquals(Arrays.asList("M. Li", "J. Sun", "P. Oleynikov", "S. Hovm\u00f6ller", "X. Zou", "B. Grushko"), articles.get(1).getAuthors());
        assertEquals(Arrays.asList("J. Nuss", "S. Pfeiffer", "S. van Smaalen", "M. Jansen"), articles.get(2).getAuthors());
        assertEquals(Arrays.asList("A. A. Udovenko", "A. D. Vasiliev", "N. M. Laptash"), articles.get(3).getAuthors());
        assertEquals(Arrays.asList("C. J. Howard", "M. A. Carpenter"), articles.get(4).getAuthors());
        assertEquals(Arrays.asList("A. M. T. Bell", "K. S. Knight", "C. M. B. Henderson", "A. N. Fitch"), articles.get(5).getAuthors());
        assertEquals(Arrays.asList("M. Gemmi", "H. Klein", "A. Rageau", "P. Strobel", "F. Le Cras"), articles.get(6).getAuthors());
        assertEquals(Arrays.asList("B. Omondi", "R. Meijboom"), articles.get(7).getAuthors());
        assertEquals(Arrays.asList("P. Derollez", "E. Dudognon", "F. Affouard", "F. Dan\u00e8de", "N. T. Correia", "M. Descamps"), articles.get(8).getAuthors());
        assertEquals(Arrays.asList("M. Tr\u00f6mel"), articles.get(9).getAuthors());
        assertEquals(Arrays.asList("C. H. G\u00f6rbitz"), articles.get(10).getAuthors());
        assertEquals(Arrays.asList("L. F\u00e1bi\u00e1n", "C. P. Brock"), articles.get(11).getAuthors());
        assertEquals(Arrays.asList(), articles.get(12).getAuthors());
    }

    @Test
    public void testArticleSuppInfo() throws Exception {
        IucrIssueTocParser parser = getActaB2010_01();
        List<Node> articleNodes = parser.getArticleNodes();
        List<SupplementaryResource> supplementaryResources =
            parser.getArticleSupplementaryResources(new ArticleId(ACTA_B, "bp5027"), articleNodes.get(3));
        assertEquals(2, supplementaryResources.size());
    }

    @Test
    @Ignore
    public void testArticleSuppInfoLinks() throws Exception {
        fail();
    }

    @Test
    public void testGetPreviousIssue() throws Exception {
        IucrIssueTocParser parser = getActaB2010_01();
        Issue prev = parser.getPreviousIssue();
        assertNotNull(prev);
        assertEquals(new IssueId(ACTA_B, "2009", "06-00"), prev.getId());
        assertEquals(URI.create("http://journals.iucr.org/b/issues/2009/06/00/isscontsbdy.html"), prev.getUrl());
    }

    @Test
    public void testToIssue() throws Exception {
        IucrIssueTocParser parser = getActaB2010_01();
        Issue issue = parser.getIssueDetails();
        assertEquals(new IssueId(ACTA_B, "66", "1"),issue.getId());
        assertEquals(URI.create("http://journals.iucr.org/b/issues/2010/01/00/isscontsbdy.html"), issue.getUrl());
        assertEquals("2010", issue.getYear());
        assertEquals("66", issue.getVolume());
        assertEquals("1", issue.getNumber());
        assertNotNull(issue.getArticles());
        assertEquals(13, issue.getArticles().size());
        assertNotNull(issue.getPreviousIssue());
        assertEquals(new IssueId(ACTA_B, "2009", "06-00"), issue.getPreviousIssue().getId());
    }

    @Test
    public void testGetOldFormatIssueId() throws Exception {
        IucrIssueTocParser parser = getActaE2004_11();
        assertEquals(new IssueId(ACTA_E, "60", "11"), parser.getIssueDetails().getId());
    }

    @Test
    public void testGetOldFormatVolume() throws Exception {
        IucrIssueTocParser parser = getActaE2004_11();
        assertEquals("60", parser.getVolume());
    }

    @Test
    public void testGetOldFormatNumber() throws Exception {
        IucrIssueTocParser parser = getActaE2004_11();
        assertEquals("11", parser.getNumber());
    }

    @Test
    public void testGetOldFormatArticles() throws Exception {
        IucrIssueTocParser parser = getActaE2004_11();
        List<Article> articles = parser.getArticles();
        assertNotNull(articles);
        assertEquals(178, articles.size());
    }

    @Test
    public void testGetArticlesWithoutLink() throws Exception {
        IucrIssueTocParser parser = getActaC2005_10();
        List<Article> articles = parser.getArticles();
        assertNotNull(articles);
        assertEquals(21, articles.size());
    }

    @Test
    public void testReadActaB1997_01GetArticles() throws Exception {
        IucrIssueTocParser parser = getActaB1997_01();
        List<Article> articles = parser.getArticles();
        assertEquals(21, articles.size());
    }

    @Test
    @Ignore
    public void testReadActaB1997_01GetArticleSuppInfo() throws Exception {
        IucrIssueTocParser parser = getActaB1997_01();
        List<Article> articles = parser.getArticles();
        assertEquals(0, articles.get(0).getSupplementaryResources().size());
        assertEquals(2, articles.get(1).getSupplementaryResources().size());
        assertEquals(1, articles.get(2).getSupplementaryResources().size());
        assertEquals(1, articles.get(3).getSupplementaryResources().size());
        assertEquals(0, articles.get(4).getSupplementaryResources().size());
        assertEquals(0, articles.get(5).getSupplementaryResources().size());
        assertEquals(1, articles.get(6).getSupplementaryResources().size());
        assertEquals(1, articles.get(7).getSupplementaryResources().size());
        assertEquals(1, articles.get(8).getSupplementaryResources().size());
        assertEquals(0, articles.get(9).getSupplementaryResources().size());
        assertEquals(1, articles.get(10).getSupplementaryResources().size());
        assertEquals(1, articles.get(11).getSupplementaryResources().size());
        assertEquals(0, articles.get(12).getSupplementaryResources().size());
        assertEquals(1, articles.get(13).getSupplementaryResources().size());
        assertEquals(1, articles.get(14).getSupplementaryResources().size());
        assertEquals(0, articles.get(15).getSupplementaryResources().size());
        assertEquals(2, articles.get(16).getSupplementaryResources().size());
        assertEquals(0, articles.get(17).getSupplementaryResources().size());
        assertEquals(1, articles.get(18).getSupplementaryResources().size());
        assertEquals(1, articles.get(19).getSupplementaryResources().size());
        assertEquals(0, articles.get(20).getSupplementaryResources().size());
    }

    @Test
    public void testGetE201103Articles() throws Exception {
        IucrIssueTocParser parser = getActaE2011_03();
        List<Article> articles = parser.getArticles();
        assertNotNull(articles);
        assertEquals(257, articles.size());
    }

    @Test
    public void testGetE201103ArticleTitles() throws Exception {
        IucrIssueTocParser parser = getActaE2011_03();
        List<Article> articles = parser.getArticles();
        assertEquals("Vanadium(V) oxide arsenate(V), VOAsO4", articles.get(0).getTitle());
        assertEquals("Redetermination of AgPO3", articles.get(1).getTitle());
    }

    @Test
    public void testGetE201103ArticleAuthors() throws Exception {
        IucrIssueTocParser parser = getActaE2011_03();
        List<Article> articles = parser.getArticles();
        assertEquals(Arrays.asList("S. Ezzine Yahmed", "M. F. Zid", "A. Driss"), articles.get(0).getAuthors());
        assertEquals(Arrays.asList("K. V. Terebilenko", "I. V. Zatovsky", "I. V. Ogorodnyk", "V. N. Baumer", "N. S. Slobodyanik"), articles.get(1).getAuthors());
    }

    @Test
    public void testGetE201103ArticleDois() throws Exception {
        IucrIssueTocParser parser = getActaE2011_03();
        List<Article> articles = parser.getArticles();
        assertEquals(new Doi("10.1107/S1600536811004053"), articles.get(0).getDoi());
        assertEquals(new Doi("10.1107/S1600536811003977"), articles.get(1).getDoi());
    }


    @Test
    public void testGetC199703Volume() throws Exception {
        IucrIssueTocParser parser = getActaC1997_03();
        assertEquals("53", parser.getVolume());
    }

    @Test
    public void testGetC199703Number() throws Exception {
        IucrIssueTocParser parser = getActaC1997_03();
        assertEquals("3", parser.getNumber());
    }

    @Test
    public void testGetC199703Year() throws Exception {
        IucrIssueTocParser parser = getActaC1997_03();
        assertEquals("1997", parser.getYear());
    }

    @Test
    public void testGetC199703Articles() throws Exception {
        IucrIssueTocParser parser = getActaC1997_03();
        List<Article> articles = parser.getArticles();
        assertNotNull(articles);
        assertEquals(58, articles.size());
    }

    @Test
    public void testGetA200906Volume() throws Exception {
        IucrIssueTocParser parser = getActaA2009_06();
        assertEquals("65", parser.getVolume());
    }

    @Test
    public void testGetA200906Number() throws Exception {
        IucrIssueTocParser parser = getActaA2009_06();
        assertEquals("6", parser.getNumber());
    }

    @Test
    public void testGetA200906Year() throws Exception {
        IucrIssueTocParser parser = getActaA2009_06();
        assertEquals("2009", parser.getYear());
    }

    @Test
    public void testGetA200906Articles() throws Exception {
        IucrIssueTocParser parser = getActaA2009_06();
        List<Article> articles = parser.getArticles();
        assertNotNull(articles);
        assertEquals(11, articles.size());

        assertEquals("Foundations of Crystallography with Computer Applications", articles.get(8).getTitle());
    }

    @Test
    public void testGetC199110Articles() throws Exception {
        IucrIssueTocParser parser = getActaC1991_10();
        List<Article> articles = parser.getArticles();
        assertNotNull(articles);
        assertEquals(93, articles.size());
    }

    @Test
    public void testGetC199101ArticleIds() throws Exception {
        IucrIssueTocParser parser = getActaC1991_10();
        List<Article> articles = parser.getArticles();
        assertEquals(new ArticleId(ACTA_C, "du0272"), articles.get(0).getId());
    }

    protected IucrIssueTocParser getActaC1997_03() throws Exception {
        return new IucrIssueTocParser(loadBody("c-1997-03-body.html", URL_C_1997_03), loadHead("c-1997-03-head.html", URL_C_1997_03), ACTA_C);
    }

    protected IucrIssueTocParser getActaA2009_06() throws Exception {
        return new IucrIssueTocParser(loadBody("a-2009-06-body.html", URL_A_2009_06), loadHead("a-2009-06-head.html", URL_A_2009_06), ACTA_A);
    }

    protected IucrIssueTocParser getActaE2004_11() throws Exception {
        return new IucrIssueTocParser(loadBody("e-2004-11-body.html", URL_E_2004_11), loadHead("e-2004-11-head.html", URL_E_2004_11), ACTA_E);
    }

    protected IucrIssueTocParser getActaE2011_03() throws Exception {
        return new IucrIssueTocParser(loadBody("e-2011-03-body.html", URL_E_2001_03), loadHead("e-2011-03-head.html", URL_E_2001_03), ACTA_E);
    }

    protected IucrIssueTocParser getActaB1997_01() throws Exception {
        return new IucrIssueTocParser(loadBody("b-1997-01-body.html", URL_B_1997_01), loadHead("b-1997-01-head.html", URL_B_1997_01), ACTA_B);
    }

    protected IucrIssueTocParser getActaB2010_01() throws Exception {
        return new IucrIssueTocParser(loadBody("b-2010-01-body.html", URL_B_2010_01), loadHead("b-2010-01-head.html", URL_B_2010_01), ACTA_B);
    }

    protected IucrIssueTocParser getActaA2010_06() throws Exception {
        return new IucrIssueTocParser(loadBody("a-2010-06-body.html", URL_A_2010_06), loadHead("a-2010-06-head.html", URL_A_2010_06), ACTA_A);
    }

    protected IucrIssueTocParser getActaC2005_10() throws Exception {
        return new IucrIssueTocParser(loadBody("c-2005-10-body.html", URL_C_2005_10), loadHead("c-2005-10-head.html", URL_C_2005_10), ACTA_C);
    }

    protected IucrIssueTocParser getActaC1991_10() throws Exception {
        return new IucrIssueTocParser(loadBody("c-1991-10-body.html", URL_C_1991_10), loadHead("c-1991-10-head.html", URL_C_1991_10), ACTA_C);
    }

    private Document loadHead(final String filename, final URI baseUrl) throws Exception {
        return loadHtml(filename, baseUrl.resolve("isscontshdr.html"));
    }

    private Document loadBody(final String filename, final URI baseUrl) throws Exception {
        return loadHtml(filename, baseUrl.resolve("isscontsbdy.html"));
    }

    private Document loadHtml(final String filename, final URI url) throws Exception {
        Document document = cache.get(filename);
        if (document == null) {
            Builder builder = new Builder(new Parser());
            InputStream in = IucrPublicationListParserTest.class.getResourceAsStream(filename);
            try {
                document = builder.build(new InputStreamReader(in, "UTF-8"));
                document.setBaseURI(url.toString());
                cache.put(filename, document);
            } finally {
                IOUtils.closeQuietly(in);
            }
        }
        return document;
    }
}
