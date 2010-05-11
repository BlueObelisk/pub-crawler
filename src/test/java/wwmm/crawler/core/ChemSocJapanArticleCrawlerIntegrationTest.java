package wwmm.crawler.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.apache.commons.httpclient.URIException;
import org.junit.Test;

import wwmm.pubcrawler.core.ArticleDescription;
import wwmm.pubcrawler.core.ArticleReference;
import wwmm.pubcrawler.core.ChemSocJapanArticleCrawler;
import wwmm.pubcrawler.core.DOI;
import wwmm.pubcrawler.core.FullTextResourceDescription;
import wwmm.pubcrawler.core.SupplementaryResourceDescription;

public class ChemSocJapanArticleCrawlerIntegrationTest {

	/**
	 * Goes out to the abstract page for an article at the
	 * CSJ site and scrapes the article details.  Basically
	 * a test that the abstract page HTML has not changed.
	 */
	@Test
	public void testGetArticleDetails() throws URIException, NullPointerException {
		DOI doi = new DOI(DOI.DOI_SITE_URL+"/10.1246/cl.2008.682");
		ChemSocJapanArticleCrawler crawler = new ChemSocJapanArticleCrawler(doi);
		ArticleDescription details = crawler.getDetails();
		assertNotNull(details);
		String authors = details.getAuthors();
		assertEquals("Koichiro Takao and Yasuhisa Ikeda", authors);
		DOI detailsDoi = details.getDoi();
		assertEquals(doi, detailsDoi);

		List<FullTextResourceDescription> ftrds = details.getFullTextResources();
		assertEquals(1, ftrds.size());
		FullTextResourceDescription ftrd = ftrds.get(0);
		assertEquals("http://www.jstage.jst.go.jp/article/cl/37/7/682/_pdf", ftrd.getURL());
		assertEquals("PDF (75K)", ftrd.getLinkText());
		assertEquals("application/pdf", ftrd.getContentType());
		
		ArticleReference ref = details.getReference();
		String journalTitle = ref.getJournalTitle();
		assertEquals("Chemistry Letters", journalTitle);
		String number = ref.getNumber();
		assertEquals("7", number);
		String pages = ref.getPages();
		assertEquals("682-683", pages);
		String volume = ref.getVolume();
		assertEquals("37", volume);
		String year = ref.getYear();
		assertEquals("2008", year);
		String title = details.getTitle();
		assertEquals("Alternative Route to Metal Halide Free Ionic Liquids", title);
		
		List<SupplementaryResourceDescription> suppList = details.getSupplementaryResources();
		assertEquals(2, suppList.size());
		SupplementaryResourceDescription sfd0 = suppList.get(0);
		String contentType0 = sfd0.getContentType();
		assertEquals("application/pdf", contentType0);
		String fileId0 = sfd0.getFileId();
		assertEquals("1", fileId0);
		String linkText0 = sfd0.getLinkText();
		assertEquals("Supporting Information", linkText0);
		String url0 = sfd0.getURL();
		assertEquals("http://www.jstage.jst.go.jp/article/cl/37/7/37_682/_appendix/1", url0);
		SupplementaryResourceDescription sfd1 = suppList.get(1);
		String contentType1 = sfd1.getContentType();
		assertEquals("application/octet-stream", contentType1);
		String fileId1 = sfd1.getFileId();
		assertEquals("2", fileId1);
		String linkText1 = sfd1.getLinkText();
		assertEquals("Crystallographic Information File (CIF)", linkText1);
		String url1 = sfd1.getURL();
		assertEquals("http://www.jstage.jst.go.jp/article/cl/37/7/37_682/_appendix/2", url1);
	}
	
}
