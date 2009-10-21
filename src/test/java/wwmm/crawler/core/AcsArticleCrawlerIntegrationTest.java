package wwmm.crawler.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.junit.Test;

import wwmm.pubcrawler.core.AcsArticleCrawler;
import wwmm.pubcrawler.core.ArticleDetails;
import wwmm.pubcrawler.core.ArticleReference;
import wwmm.pubcrawler.core.DOI;
import wwmm.pubcrawler.core.FullTextResourceDetails;
import wwmm.pubcrawler.core.SupplementaryResourceDetails;

public class AcsArticleCrawlerIntegrationTest {
	
	/**
	 * Goes out to the abstract page for an article at the
	 * ACS site and scrapes the article details.  Basically
	 * a test that the abstract page HTML has not changed.
	 */
	@Test
	public void testGetArticleDetails() throws URISyntaxException, NullPointerException {
		DOI doi = new DOI(DOI.DOI_SITE_URL+"/10.1021/cg801336t");
		AcsArticleCrawler crawler = new AcsArticleCrawler(doi);
		ArticleDetails details = crawler.getDetails();
		assertNotNull(details);
		String authors = details.getAuthors();
		assertEquals("Ichiro Hisaki, Norie Shizuki, Kazuaki Aburaya, Masanori Katsuta, Norimitsu Tohnai, Mikiji Miyata", authors);
		DOI detailsDoi = details.getDoi();
		assertEquals(doi, detailsDoi);

		List<FullTextResourceDetails> ftrds = details.getFullTextResources();
		assertEquals(3, ftrds.size());
		FullTextResourceDetails ftrd1 = ftrds.get(0);
		assertEquals(new URI("http://pubs.acs.org/doi/full/10.1021/cg801336t"), ftrd1.getURI());
		assertEquals("Full Text HTML", ftrd1.getLinkText());
		assertEquals("text/html", ftrd1.getContentType());
		FullTextResourceDetails ftrd2 = ftrds.get(1);
		assertEquals(new URI("http://pubs.acs.org/doi/pdf/10.1021/cg801336t"), ftrd2.getURI());
		assertEquals("Hi-Res PDF[1954 KB]", ftrd2.getLinkText());
		assertEquals("application/pdf", ftrd2.getContentType());
		FullTextResourceDetails ftrd3 = ftrds.get(2);
		assertEquals(new URI("http://pubs.acs.org/doi/pdfplus/10.1021/cg801336t"), ftrd3.getURI());
		assertEquals("PDF w/ Links[370 KB]", ftrd3.getLinkText());
		assertEquals("application/pdf", ftrd3.getContentType());
		
		ArticleReference ref = details.getReference();
		String journalTitle = ref.getJournalTitle();
		assertEquals("Cryst. Growth Des.", journalTitle);
		String number = ref.getNumber();
		assertEquals("3", number);
		String pages = ref.getPages();
		assertEquals("1280-1283", pages);
		String volume = ref.getVolume();
		assertEquals("9", volume);
		String year = ref.getYear();
		assertEquals("2009", year);
		String title = details.getTitle();
		assertEquals("Structures of Brucinium Cholate: Bile Acid and Strychnine Derivatives Meet in the Crystals", title);
		
		List<SupplementaryResourceDetails> suppList = details.getSupplementaryResources();
		assertEquals(4, suppList.size());
		SupplementaryResourceDetails sfd0 = suppList.get(0);
		String contentType0 = sfd0.getContentType();
		assertEquals("application/pdf", contentType0);
		String fileId0 = sfd0.getFileId();
		assertEquals("cg801336t_si_001", fileId0);
		String linkText0 = sfd0.getLinkText();
		assertEquals("cg801336t_si_001.pdf (841 KB)", linkText0);
		URI uri0 = sfd0.getURI();
		assertEquals(new URI("http://pubs.acs.org/doi/suppl/10.1021/cg801336t/suppl_file/cg801336t_si_001.pdf"), uri0);
		SupplementaryResourceDetails sfd2 = suppList.get(2);
		String contentType2 = sfd2.getContentType();
		assertEquals("chemical/x-cif", contentType2);
		String fileId2 = sfd2.getFileId();
		assertEquals("cg801336t_si_003", fileId2);
		String linkText2 = sfd2.getLinkText();
		assertEquals("cg801336t_si_003.cif (69 KB)", linkText2);
		URI uri2 = sfd2.getURI();
		assertEquals(new URI("http://pubs.acs.org/doi/suppl/10.1021/cg801336t/suppl_file/cg801336t_si_003.cif"), uri2);
	}

}
