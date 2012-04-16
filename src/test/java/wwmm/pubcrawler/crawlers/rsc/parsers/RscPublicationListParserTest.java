package wwmm.pubcrawler.crawlers.rsc.parsers;

import nu.xom.Builder;
import nu.xom.Document;
import org.apache.commons.io.IOUtils;
import org.ccil.cowan.tagsoup.Parser;
import org.junit.Test;
import wwmm.pubcrawler.model.Journal;

import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author Sam Adams
 */
public class RscPublicationListParserTest {

    private Document loadDocument() throws Exception {
        final InputStream in = getClass().getResourceAsStream("journals.html");
        try {
            return new Builder(new Parser()).build(in);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    @Test
    public void testFindJournals() throws Exception {
        Document html = loadDocument();
        RscPublicationListParser parser = new RscPublicationListParser(html);

        List<Journal> journals = parser.findJournals();

        assertEquals(35,
            journals.size());

        assertEquals("Analyst",
            journals.get(0).getTitle());
        assertEquals("Analytical Methods",
            journals.get(1).getTitle());
        assertEquals("Annual Reports Section \"A\" (Inorganic Chemistry)",
            journals.get(2).getTitle());
        assertEquals("Annual Reports Section \"B\" (Organic Chemistry)",
            journals.get(3).getTitle());
        assertEquals("Annual Reports Section \"C\" (Physical Chemistry)",
            journals.get(4).getTitle());
        assertEquals("Biomaterials Science",
            journals.get(5).getTitle());
        assertEquals("Catalysis Science & Technology",
            journals.get(6).getTitle());
        assertEquals("Chemical Communications",
            journals.get(7).getTitle());
        assertEquals("Chemical Science",
            journals.get(8).getTitle());
        assertEquals("Chemical Society Reviews",
            journals.get(9).getTitle());
        assertEquals("Chemistry Education Research and Practice",
            journals.get(10).getTitle());
        assertEquals("CrystEngComm",
            journals.get(11).getTitle());
        assertEquals("Dalton Transactions",
            journals.get(12).getTitle());
        assertEquals("Energy & Environmental Science",
            journals.get(13).getTitle());
        assertEquals("Faraday Discussions",
            journals.get(14).getTitle());
        assertEquals("Food & Function",
            journals.get(15).getTitle());
        assertEquals("Green Chemistry",
            journals.get(16).getTitle());
        assertEquals("Integrative Biology",
            journals.get(17).getTitle());
        assertEquals("Journal of Analytical Atomic Spectrometry",
            journals.get(18).getTitle());
        assertEquals("Journal of Environmental Monitoring",
            journals.get(19).getTitle());
        assertEquals("Journal of Materials Chemistry",
            journals.get(20).getTitle());
        assertEquals("Lab on a Chip",
            journals.get(21).getTitle());
        assertEquals("MedChemComm",
            journals.get(22).getTitle());
        assertEquals("Metallomics",
            journals.get(23).getTitle());
        assertEquals("Molecular BioSystems",
            journals.get(24).getTitle());
        assertEquals("Nanoscale",
            journals.get(25).getTitle());
        assertEquals("Natural Product Reports",
            journals.get(26).getTitle());
        assertEquals("New Journal of Chemistry",
            journals.get(27).getTitle());
        assertEquals("Organic & Biomolecular Chemistry",
            journals.get(28).getTitle());
        assertEquals("Photochemical & Photobiological Sciences",
            journals.get(29).getTitle());
        assertEquals("Physical Chemistry Chemical Physics",
            journals.get(30).getTitle());
        assertEquals("Polymer Chemistry",
            journals.get(31).getTitle());
        assertEquals("RSC Advances",
            journals.get(32).getTitle());
        assertEquals("Soft Matter",
            journals.get(33).getTitle());
        assertEquals("Toxicology Research",
            journals.get(34).getTitle());
    }
}
