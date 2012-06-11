package wwmm.pubcrawler.crawlers.acs.parsers;

import nu.xom.Builder;
import nu.xom.Document;
import org.apache.commons.io.IOUtils;
import org.ccil.cowan.tagsoup.Parser;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;
import wwmm.pubcrawler.model.Author;
import wwmm.pubcrawler.model.SupplementaryResource;
import wwmm.pubcrawler.model.id.ArticleId;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.List;

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


    @Test
    public void testGetAuthorDetail() throws Exception {
        Author author1 = new Author("Jason C. Kwan");
        author1.setAffiliation("Department of Medicinal Chemistry, University of Florida, 1600 SW Archer Road, Gainesville, Florida 32610, United States");

        Author author2 = new Author("Ranjala Ratnayake");
        author2.setAffiliation("Department of Medicinal Chemistry, University of Florida, 1600 SW Archer Road, Gainesville, Florida 32610, United States");

        Author author3 = new Author("Khalil A. Abboud");
        author3.setAffiliation("Department of Chemistry, 214 Leigh Hall, University of Florida, Gainesville, Florida 32611, United States");

        Author author4 = new Author("Valerie J. Paul");
        author4.setAffiliation("Smithsonian Marine Station, 701 Seaway Drive, Fort Pierce, Florida 34949, United States");

        Author author5 = new Author("Hendrik Luesch");
        author5.setAffiliation("Department of Medicinal Chemistry, University of Florida, 1600 SW Archer Road, Gainesville, Florida 32610, United States");
        author5.setEmailAddress("luesch@cop.ufl.edu");

        AcsArticleSuppInfoPageParser parser = getArticleJo1013564();
        List<Author> authors = parser.getAuthorDetails();
        assertEquals(asList(author1, author2, author3, author4, author5), authors);
    }

    @Test
    public void testGetAbstract() throws Exception {
        String expectedAbstract = "<p>Grassypeptolides A&#x2212;C (<b>1</b>&#x2212;<b>3</b>), a group of closely related " +
            "bis-thiazoline containing cyclic depsipeptides, have been isolated from extracts of the marine cyanobacterium " +
            "<i>Lyngbya confervoides</i>. Although structural differences between the analogues are minimal, comparison of " +
            "the <i>in vitro</i> cytotoxicity of the series revealed a structure&#x2212;activity relationship. When the ethyl " +
            "substituent of <b>1</b> is changed to a methyl substituent in <b>2</b>, activity is only slightly reduced " +
            "(3&#x2212;4-fold), whereas inversion of the Phe unit flanking the bis-thiazoline moiety results in " +
            "16&#x2212;23-fold greater potency. We show that both <b>1</b> and <b>3</b> cause G1 phase cell cycle arrest " +
            "at lower concentrations, followed at higher concentrations by G2/M phase arrest, and that these compounds bind " +
            "Cu<sup>2+</sup> and Zn<sup>2+</sup>. The three-dimensional structure of <b>2</b> was determined by MS, NMR, " +
            "and X-ray crystallography, and the structure of <b>3</b> was established by MS, NMR, and chemical degradation. " +
            "The structure of <b>3</b> was explored by <i>in silico</i> molecular modeling, revealing subtle differences in " +
            "overall conformation between <b>1</b> and <b>3</b>. Attempts to interconvert <b>1</b> and <b>3</b> with base " +
            "were unsuccessful, but enzymatic conversion may be possible and could be a novel form of activation for " +
            "chemical defense.</p>";

        AcsArticleSuppInfoPageParser parser = getArticleJo1013564();
        assertEquals(expectedAbstract, parser.getAbstractAsHtml());
    }

    @Test
    public void testGetSupplementaryResources() throws Exception {
        AcsArticleSuppInfoPageParser parser = getArticleJo1013564();
        List<SupplementaryResource> resources = parser.getSupplementaryResources();

        assertEquals(2, resources.size());
        assertEquals(URI.create("http://pubs.acs.org/doi/suppl/10.1021/jo1013564/suppl_file/jo1013564_si_001.pdf"), resources.get(0).getUrl());
        assertEquals("jo1013564_si_001.pdf", resources.get(0).getFilePath());
        assertEquals("jo1013564_si_001.pdf (2.33 MB)", resources.get(0).getLinkText());
        assertEquals("application/pdf", resources.get(0).getContentType());

        assertEquals(URI.create("http://pubs.acs.org/doi/suppl/10.1021/jo1013564/suppl_file/jo1013564_si_002.cif"), resources.get(1).getUrl());
        assertEquals("jo1013564_si_002.cif", resources.get(1).getFilePath());
        assertEquals("jo1013564_si_002.cif (33 KB)", resources.get(1).getLinkText());
        assertEquals("chemical/x-cif", resources.get(1).getContentType());
    }


    protected AcsArticleSuppInfoPageParser getArticleJo1013564() throws Exception {
        final ArticleId articleRef = new ArticleId("acs/joceah/75/23/jo1013564");

        Document doc = jo1013564;
        if (doc == null) {
            doc = loadDocument("jo1013564_supp.html");
            jo1013564 = doc;
        }
        return new AcsArticleSuppInfoPageParser(articleRef, doc, URI.create("http://pubs.acs.org/doi/suppl/10.1021/jo1013564"));
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
