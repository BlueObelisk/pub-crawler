package wwmm.pubcrawler.crawlers.acta.parsers;

import nu.xom.Builder;
import nu.xom.Document;
import org.apache.commons.io.IOUtils;
import org.ccil.cowan.tagsoup.Parser;
import org.junit.AfterClass;
import org.junit.Test;
import wwmm.pubcrawler.crawlers.acta.Iucr;
import wwmm.pubcrawler.model.SupplementaryResource;
import wwmm.pubcrawler.model.id.ArticleId;
import wwmm.pubcrawler.model.id.JournalId;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.junit.Assert.assertEquals;

/**
 * @author Sam Adams
 */
public class IucrSuppInfoResourcePageParserTest {

    private static final JournalId ACTA_B = new JournalId(Iucr.PUBLISHER_ID, "b");

    private static ConcurrentMap<String,Document> cache = new ConcurrentHashMap<String, Document>();

    @AfterClass
    public static void afterAllTests() {
        cache = null;
    }

    @Test
    public void testFindResources() throws Exception {
        IucrSuppInfoResourcePageParser parser = getParserBk5060();
        final List<SupplementaryResource> resources = parser.getResources();
        assertEquals(3, resources.size());
    }

    @Test
    public void testFindResourcesLinks() throws Exception {
        IucrSuppInfoResourcePageParser parser = getParserBk5060();
        final List<SupplementaryResource> resources = parser.getResources();
        assertEquals(3, resources.size());

        assertEquals(URI.create("http://scripts.iucr.org/cgi-bin/sendsupfiles?bk5060&file=bk5060sup1.cif&mime=application/x-unknown"), resources.get(0).getUrl());
        assertEquals(URI.create("http://scripts.iucr.org/cgi-bin/sendsupfiles?bk5060&file=bk5060sup2.cif&mime=application/x-unknown"), resources.get(1).getUrl());
        assertEquals(URI.create("http://scripts.iucr.org/cgi-bin/sendsupfiles?bk5060&file=bk5060sup3.pdf&mime=application/pdf"), resources.get(2).getUrl());
    }

    @Test
    public void testFindResourcesLinkText() throws Exception {
        IucrSuppInfoResourcePageParser parser = getParserBk5060();
        final List<SupplementaryResource> resources = parser.getResources();
        assertEquals(3, resources.size());

        assertEquals("Crystallographic Information File (CIF)", resources.get(0).getLinkText());
        assertEquals("Crystallographic Information File (CIF)", resources.get(1).getLinkText());
        assertEquals("Portable Document Format (PDF) file", resources.get(2).getLinkText());
    }

    @Test
    public void testFindResourcesDescription() throws Exception {
        IucrSuppInfoResourcePageParser parser = getParserBk5060();
        final List<SupplementaryResource> resources = parser.getResources();
        assertEquals(3, resources.size());

        assertEquals("Contains datablocks hgi2_n_red, hgi2_n, hgi2_n30k, hgi2_n60k, hgi2_n100k, hgi2_n200k, hgi2_n293k", resources.get(0).getDescription());
        assertEquals("Contains datablock raidur9", resources.get(1).getDescription());
        assertEquals("Supplementary material", resources.get(2).getDescription());
    }

    @Test
    public void testFindFilePath() throws Exception {
        IucrSuppInfoResourcePageParser parser = getParserBk5060();
        final List<SupplementaryResource> resources = parser.getResources();
        assertEquals(3, resources.size());

        assertEquals("bk5060sup1.cif", resources.get(0).getFilePath());
        assertEquals("bk5060sup2.cif", resources.get(1).getFilePath());
        assertEquals("bk5060sup3.pdf", resources.get(2).getFilePath());
    }

    private IucrSuppInfoResourcePageParser getParserBk5060() throws Exception {
        ArticleId articleRef = new ArticleId(ACTA_B, "bk5060");
        final URI url = URI.create("http://scripts.iucr.org/cgi-bin/sendsup?bk5060");
        Document html = loadHtml("bk5060_cifs.html", url);
        return new IucrSuppInfoResourcePageParser(articleRef, html, url);
    }


    private Document loadHtml(final String filename, final URI url) throws Exception {
        Document document = cache.get(filename);
        if (document == null) {
            Builder builder = new Builder(new Parser());
            InputStream in = IucrPublicationListParserTest.class.getResourceAsStream(filename);
            if (in == null) {
                throw new FileNotFoundException("File not found: " + filename);
            }
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
