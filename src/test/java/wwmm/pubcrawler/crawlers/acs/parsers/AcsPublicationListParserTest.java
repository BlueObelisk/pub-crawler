package wwmm.pubcrawler.crawlers.acs.parsers;

import nu.xom.Builder;
import nu.xom.Document;
import org.apache.commons.io.IOUtils;
import org.ccil.cowan.tagsoup.Parser;
import org.junit.Test;
import wwmm.pubcrawler.crawlers.acs.parsers.AcsPublicationListParser;
import wwmm.pubcrawler.model.Journal;
import wwmm.pubcrawler.model.id.PublisherId;
import wwmm.pubcrawler.utils.ResourceUtil;

import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author Sam Adams
 */
public class AcsPublicationListParserTest {

    private static final PublisherId ACS = new PublisherId("acs");

    private Document loadDocument(String path) throws Exception {
        final InputStream in  = ResourceUtil.open(getClass(), "/wwmm/pubcrawler/crawlers/acs/" + path);
        try {
            return new Builder(new Parser()).build(in);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }
    
    @Test
    public void testFindJournals() throws Exception {
        Document html = loadDocument("acs-pubs.html");
        AcsPublicationListParser parser = new AcsPublicationListParser(html);
        
        List<Journal> journals = parser.findJournals();
        
        assertEquals(46, journals.size());

        assertEquals("Accounts of Chemical Research",
            journals.get(0).getTitle());
        assertEquals("ACS Applied Materials & Interfaces",
            journals.get(1).getTitle());
        assertEquals("ACS Catalysis",
            journals.get(2).getTitle());
        assertEquals("ACS Chemical Biology",
            journals.get(3).getTitle());
        assertEquals("ACS Chemical Neuroscience",
            journals.get(4).getTitle());
        assertEquals("ACS Combinatorial Science",
            journals.get(5).getTitle());
        assertEquals("ACS Macro Letters",
            journals.get(6).getTitle());
        assertEquals("ACS Medicinal Chemistry Letters",
            journals.get(7).getTitle());
        assertEquals("ACS Nano",
            journals.get(8).getTitle());
        assertEquals("ACS Synthetic Biology",
            journals.get(9).getTitle());
        assertEquals("Analytical Chemistry",
            journals.get(10).getTitle());
        assertEquals("Biochemistry",
            journals.get(11).getTitle());
        assertEquals("Bioconjugate Chemistry",
            journals.get(12).getTitle());
        assertEquals("Biomacromolecules",
            journals.get(13).getTitle());
        assertEquals("Biotechnology Progress",
            journals.get(14).getTitle());
        assertEquals("Chemical & Engineering News Archive Archives",
            journals.get(15).getTitle());
        assertEquals("Chemical Research in Toxicology",
            journals.get(16).getTitle());
        assertEquals("Chemical Reviews",
            journals.get(17).getTitle());
        assertEquals("Chemistry of Materials",
            journals.get(18).getTitle());
        assertEquals("Crystal Growth & Design",
            journals.get(19).getTitle());
        assertEquals("Energy & Fuels",
            journals.get(20).getTitle());
        assertEquals("Environmental Science & Technology",
            journals.get(21).getTitle());
        assertEquals("Industrial & Engineering Chemistry",
            journals.get(22).getTitle());
        assertEquals("Industrial & Engineering Chemistry Research",
            journals.get(23).getTitle());
        assertEquals("Inorganic Chemistry",
            journals.get(24).getTitle());
        assertEquals("Journal of the American Chemical Society",
            journals.get(25).getTitle());
        assertEquals("Journal of Agricultural and Food Chemistry",
            journals.get(26).getTitle());
        assertEquals("Journal of Chemical & Engineering Data",
            journals.get(27).getTitle());
        assertEquals("Journal of Chemical Education",
            journals.get(28).getTitle());
        assertEquals("Journal of Chemical Information and Modeling",
            journals.get(29).getTitle());
        assertEquals("Journal of Chemical Theory and Computation",
            journals.get(30).getTitle());
        assertEquals("Journal of Medicinal Chemistry",
            journals.get(31).getTitle());
        assertEquals("Journal of Natural Products",
            journals.get(32).getTitle());
        assertEquals("The Journal of Organic Chemistry",
            journals.get(33).getTitle());
        assertEquals("The Journal of Physical Chemistry A",
            journals.get(34).getTitle());
        assertEquals("The Journal of Physical Chemistry B",
            journals.get(35).getTitle());
        assertEquals("The Journal of Physical Chemistry C",
            journals.get(36).getTitle());
        assertEquals("The Journal of Physical Chemistry Letters",
            journals.get(37).getTitle());
        assertEquals("Journal of Proteome Research",
            journals.get(38).getTitle());
        assertEquals("Langmuir",
            journals.get(39).getTitle());
        assertEquals("Macromolecules",
            journals.get(40).getTitle());
        assertEquals("Molecular Pharmaceutics",
            journals.get(41).getTitle());
        assertEquals("Nano Letters",
            journals.get(42).getTitle());
        assertEquals("Organic Letters",
            journals.get(43).getTitle());
        assertEquals("Organic Process Research & Development",
            journals.get(44).getTitle());
        assertEquals("Organometallics",
            journals.get(45).getTitle());
    }
    
}
