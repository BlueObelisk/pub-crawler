package wwmm.pubcrawler.crawlers.acta.parsers;

import nu.xom.Builder;
import nu.xom.Document;
import org.apache.commons.io.IOUtils;
import org.ccil.cowan.tagsoup.Parser;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import wwmm.pubcrawler.crawlers.acta.Iucr;
import wwmm.pubcrawler.model.Journal;
import wwmm.pubcrawler.model.id.JournalId;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author Sam Adams
 */
public class IucrPublicationListParserTest {

    private static final URI URL = URI.create("http://journals.iucr.org/");

    private static Document publications;

    @BeforeClass
    public static void beforeAnyTests() throws Exception {
        publications = loadHtml("publications.html");
    }

    @AfterClass
    public static void afterAllTests() {
        publications = null;
    }
    
    @Test
    public void testFindPublications() throws Exception {
        IucrPublicationListParser parser = new IucrPublicationListParser(publications, URL);
        List<Journal> list = parser.findJournals();
        assertEquals(8, list.size());
    }

    @Test
    public void testGetPublicationId() throws Exception {
        IucrPublicationListParser parser = new IucrPublicationListParser(publications, URL);
        List<Journal> list = parser.findJournals();
        assertEquals(new JournalId(Iucr.PUBLISHER_ID, "b"), list.get(1).getId());
    }

    @Test
    public void testGetPublicationTitle() throws Exception {
        IucrPublicationListParser parser = new IucrPublicationListParser(publications, URL);
        List<Journal> list = parser.findJournals();
        assertEquals("Acta Crystallographica Section B: Structural Science", list.get(1).getTitle());
    }

    @Test
    public void testGetPublicationUrl() throws Exception {
        IucrPublicationListParser parser = new IucrPublicationListParser(publications, URL);
        List<Journal> list = parser.findJournals();
        assertEquals(URI.create("http://journals.iucr.org/b/journalhomepage.html"), list.get(1).getUrl());
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
