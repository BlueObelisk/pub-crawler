package wwmm.crawler.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.junit.Test;

import wwmm.pubcrawler.core.ActaArticleCrawler;
import wwmm.pubcrawler.core.ArticleDetails;
import wwmm.pubcrawler.core.ArticleReference;
import wwmm.pubcrawler.core.DOI;
import wwmm.pubcrawler.core.FullTextResourceDetails;
import wwmm.pubcrawler.core.SupplementaryResourceDetails;

public class ActaArticleCrawlerIntegrationTest {
	
	/**
	 * Goes out to the abstract page for an article at the
	 * Acta site and scrapes the article details.  Basically
	 * a test that the abstract page HTML has not changed.
	 */
	@Test
	public void testGetArticleDetails() throws URISyntaxException, NullPointerException {
		DOI doi = new DOI(DOI.DOI_SITE_URL+"/10.1107/S0108270109006118");
		ActaArticleCrawler crawler = new ActaArticleCrawler(doi);
		ArticleDetails details = crawler.getDetails();
		assertNotNull(details);
		String authors = details.getAuthors();
		assertEquals("Kim, Jinyoung and Ahn, Docheon and Kulshreshtha, Chandramouli and Sohn, Kee-Sun and Shin, Namsoo", authors);
		DOI detailsDoi = details.getDoi();
		assertEquals(doi, detailsDoi);

		List<FullTextResourceDetails> ftrds = details.getFullTextResources();
		assertEquals(2, ftrds.size());
		FullTextResourceDetails ftrd1 = ftrds.get(0);
		assertEquals(new URI("http://journals.iucr.org/c/issues/2009/04/00/sq3185/index.html"), ftrd1.getURI());
		assertEquals("HTML", ftrd1.getLinkText());
		assertEquals("text/html", ftrd1.getContentType());
		FullTextResourceDetails ftrd2 = ftrds.get(1);
		assertEquals(new URI("http://journals.iucr.org/c/issues/2009/04/00/sq3185/sq3185.pdf"), ftrd2.getURI());
		assertEquals("PDF", ftrd2.getLinkText());
		assertEquals("application/pdf", ftrd2.getContentType());
		ArticleReference ref = details.getReference();
		String journalTitle = ref.getJournalTitle();
		assertEquals("Acta Crystallographica Section C", journalTitle);
		String number = ref.getNumber();
		assertEquals("4", number);
		String pages = ref.getPages();
		assertEquals("i14--i16", pages);
		String volume = ref.getVolume();
		assertEquals("65", volume);
		String year = ref.getYear();
		assertEquals("2009", year);
		String title = details.getTitle();
		assertEquals("Lithium barium silicate, Li${\\sb 2}$BaSiO${\\sb 4}$, from synchrotron powder data", title);
		
		List<SupplementaryResourceDetails> suppList = details.getSupplementaryResources();
		assertEquals(1, suppList.size());
		SupplementaryResourceDetails sfd0 = suppList.get(0);
		String contentType0 = sfd0.getContentType();
		assertEquals("text/plain; charset=utf-8", contentType0);
		String fileId0 = sfd0.getFileId();
		assertEquals("sq3185sup1", fileId0);
		String linkText0 = sfd0.getLinkText();
		assertEquals("CIF", linkText0);
		URI uri0 = sfd0.getURI();
		assertEquals(new URI("http://scripts.iucr.org/cgi-bin/sendcif?sq3185sup1"), uri0);
	}
	
	/**
	 * Most article in Acta journals only have 1 CIF as supp info.  These
	 * are linked directly from the article abstract page.  If there is more
	 * than 1 CIF for an article, another page has to be scraped, so there
	 * is another chance for the scraper to fail.  This test checks that the
	 * scraping of the extra page is working.
	 */
	@Test
	public void testGetMultipleCifsFromArticle() {
		DOI doi = new DOI(DOI.DOI_SITE_URL+"/10.1107/S0108768109004066");
		ActaArticleCrawler crawler = new ActaArticleCrawler(doi);
		ArticleDetails details = crawler.getDetails();
		assertEquals(2, details.getSupplementaryResources().size());
	}

}
