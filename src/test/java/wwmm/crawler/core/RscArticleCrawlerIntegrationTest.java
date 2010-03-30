package wwmm.crawler.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static wwmm.pubcrawler.core.CrawlerConstants.RSC_HOMEPAGE_URL;

import java.util.List;

import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;
import org.junit.Test;

import wwmm.pubcrawler.core.ArticleDescription;
import wwmm.pubcrawler.core.ArticleReference;
import wwmm.pubcrawler.core.DOI;
import wwmm.pubcrawler.core.FullTextResourceDescription;
import wwmm.pubcrawler.core.RscArticleCrawler;
import wwmm.pubcrawler.core.SupplementaryResourceDescription;

public class RscArticleCrawlerIntegrationTest {

	/**
	 * Goes out to the abstract page for an article at the
	 * CSJ site and scrapes the article details.  Basically
	 * a test that the abstract page HTML has not changed.
	 */
	@Test
	public void testGetArticleDetails() throws URIException, NullPointerException {
		DOI doi = new DOI(DOI.DOI_SITE_URL+"/10.1039/b821431j");
		RscArticleCrawler crawler = new RscArticleCrawler(doi);
		ArticleDescription details = crawler.getDetails();
		assertNotNull("NULL article details", details);
		String authors = details.getAuthors();
		assertEquals("Article authors", "Celia Ribes, Eva Falomir, Juan Murga, Miguel Carda and J. Alberto Marco", authors);
		DOI detailsDoi = details.getDoi();
		assertEquals("Article DOI", doi, detailsDoi);

		List<FullTextResourceDescription> ftrds = details.getFullTextResources();
		assertEquals("Number of fulltext resources found", 2, ftrds.size());
		FullTextResourceDescription ftrd1 = ftrds.get(0);
		assertEquals("Fulltext HTML URI", new URI(RSC_HOMEPAGE_URL+"/delivery/_ArticleLinking/ArticleLinking.cfm?JournalCode=OB&Year=2009&ManuscriptID=b821431j&Iss=7", false), ftrd1.getURI());
		assertEquals("Fulltext HTML link text", "HTML article", ftrd1.getLinkText());
		assertEquals("Fulltext HTML MIME", "text/html", ftrd1.getContentType());
		FullTextResourceDescription ftrd2 = ftrds.get(1);
		assertEquals("Fulltext PDF URI", new URI(RSC_HOMEPAGE_URL+"/ej/OB/2009/b821431j.pdf", false), ftrd2.getURI());
		assertEquals("Fulltext PDF link", "PDF", ftrd2.getLinkText());
		assertEquals("Fulltext PDF MIME", "application/pdf", ftrd2.getContentType());
		ArticleReference ref = details.getReference();
		String journalTitle = ref.getJournalTitle();
		assertEquals("Journal in reference", "Org. Biomol. Chem.", journalTitle);
		String pages = ref.getPages();
		assertEquals("Pages in reference", "1355 - 1360", pages);
		String volume = ref.getVolume();
		assertEquals("Volume in reference", "7", volume);
		String year = ref.getYear();
		assertEquals("Year in reference", "2009", year);
		String title = details.getTitle();
		assertEquals("Title", "Convergent, stereoselective syntheses of the glycosidase inhibitors broussonetines D and M", title);
		
		List<SupplementaryResourceDescription> suppList = details.getSupplementaryResources();
		assertEquals("Number of supplementary resources found", 3, suppList.size());
		SupplementaryResourceDescription sfd0 = suppList.get(0);
		String contentType0 = sfd0.getContentType();
		assertEquals("First supplementary resource MIME", "application/pdf", contentType0);
		String fileId0 = sfd0.getFileId();
		assertEquals("First supplementary resource file ID", "b821431j_1", fileId0);
		String linkText0 = sfd0.getLinkText();
		assertEquals("First supplementary resource link text", "Additional experimental procedures and tabulated spectral data of compounds 7, 8, 9a, 10, 11, 13, 14, 16�19 and 21", linkText0);
		URI uri0 = sfd0.getURI();
		assertEquals("First supplementary resource URI", new URI(RSC_HOMEPAGE_URL+"/suppdata/OB/b8/b821431j/b821431j_1.pdf", false), uri0);
		SupplementaryResourceDescription sfd2 = suppList.get(2);
		String contentType2 = sfd2.getContentType();
		assertEquals("Second supplementary resource MIME", "text/plain", contentType2);
		String fileId2 = sfd2.getFileId();
		assertEquals("Second supplementary resource file ID", "b821431j", fileId2);
		String linkText2 = sfd2.getLinkText();
		assertEquals("Second supplementary resource link text", "Crystal structure data", linkText2);
		URI uri2 = sfd2.getURI();
		assertEquals("Second supplementary resource URI", new URI(RSC_HOMEPAGE_URL+"/suppdata/OB/b8/b821431j/b821431j.txt", false), uri2);		
	}
	
	/**
	 * Two different types of reference appear on the article abstract 
	 * pages, each with differing amount of detail, e.g.:
	 * 
	 * 1. Chem. Commun., 2009, 1658 - 1660, DOI: 10.1039/b815825h
	 * 2. Org. Biomol. Chem., 2009, 7, 1280 - 1283, DOI: 10.1039/b900026g
	 * 
	 * So sometimes the volume is included, sometimes not.  This is taken
	 * into account in the crawler.  This test checks that it is working
	 * correctly.
	 */
	@Test
	public void testGetReference() {
		DOI doi1 = new DOI(DOI.DOI_SITE_URL+"/10.1039/b815825h");
		RscArticleCrawler crawler1 = new RscArticleCrawler(doi1);
		ArticleDescription details1 = crawler1.getDetails();
		ArticleReference ref1 = details1.getReference();
		String title1 = ref1.getJournalTitle();
		assertEquals("Journal abbreviation in first reference", "Chem. Commun.", title1);
		String pages1 = ref1.getPages();
		assertEquals("Pages in first reference", "1658-1660", pages1);
		String vol1 = ref1.getVolume();
		assertNull("Not NULL volume", vol1);
		String year1 = ref1.getYear();
		assertEquals("Year in first reference", "2009", year1);
		DOI doi2 = new DOI(DOI.DOI_SITE_URL+"/10.1039/b900026g");
		RscArticleCrawler crawler2 = new RscArticleCrawler(doi2);
		ArticleDescription details2 = crawler2.getDetails();
		ArticleReference ref2 = details2.getReference();
		String title2 = ref2.getJournalTitle();
		assertEquals("Journal abbreviation in second reference", "Org. Biomol. Chem.", title2);
		String pages2 = ref2.getPages();
		assertEquals("Pages in second reference", "1280 - 1283", pages2);
		String vol2 = ref2.getVolume();
		assertEquals("Volume in second reference", "7", vol2);
		String year2 = ref2.getYear();
		assertEquals("Year in second reference", "2009", year2);
	}
	
}
