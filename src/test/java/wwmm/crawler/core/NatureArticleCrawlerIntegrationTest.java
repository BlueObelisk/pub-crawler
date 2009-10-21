package wwmm.crawler.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.junit.Test;

import wwmm.pubcrawler.core.ArticleDetails;
import wwmm.pubcrawler.core.ArticleReference;
import wwmm.pubcrawler.core.DOI;
import wwmm.pubcrawler.core.FullTextResourceDetails;
import wwmm.pubcrawler.core.NatureArticleCrawler;
import wwmm.pubcrawler.core.SupplementaryResourceDetails;

public class NatureArticleCrawlerIntegrationTest {

	/**
	 * Goes out to the abstract page for an article at the
	 * Nature site and scrapes the article details.  Basically
	 * a test that the abstract page HTML has not changed.
	 */
	@Test
	public void testGetArticleDetails() throws URISyntaxException, NullPointerException {
		DOI doi = new DOI(DOI.DOI_SITE_URL+"/10.1038/nchem.213");
		NatureArticleCrawler crawler = new NatureArticleCrawler(doi);
		ArticleDetails details = crawler.getDetails();
		assertNotNull(details);
		String authors = details.getAuthors();
		assertEquals("Tanya K. Ronson, Julie Fisher, Lindsay P. Harding, Pierre J. Rizkallah, John E. Warren, Michaele J. Hardie", authors);
		DOI detailsDoi = details.getDoi();
		assertEquals(doi, detailsDoi);

		List<FullTextResourceDetails> ftrds = details.getFullTextResources();
		assertEquals(2, ftrds.size());
		FullTextResourceDetails ftrd1 = ftrds.get(0);
		assertEquals(new URI("http://www.nature.com/nchem/journal/v1/n3/full/nchem.213.html"), ftrd1.getURI());
		assertEquals("Full text", ftrd1.getLinkText());
		assertEquals("text/html", ftrd1.getContentType());
		FullTextResourceDetails ftrd2 = ftrds.get(1);
		assertEquals(new URI("http://www.nature.com/nchem/journal/v1/n3/pdf/nchem.213.pdf"), ftrd2.getURI());
		assertEquals("Download PDF", ftrd2.getLinkText());
		assertEquals("application/pdf", ftrd2.getContentType());
		
		ArticleReference ref = details.getReference();
		String journalTitle = ref.getJournalTitle();
		assertEquals("Nature Chemistry", journalTitle);
		String number = ref.getNumber();
		assertEquals("3", number);
		String pages = ref.getPages();
		assertEquals("212 - 216", pages);
		String volume = ref.getVolume();
		assertEquals("1", volume);
		String title = details.getTitle();
		assertEquals("Stellated polyhedral assembly of a topologically complicated Pd4L4 |[lsquo]|Solomon cube|[rsquo]|", title);
		
		List<SupplementaryResourceDetails> suppList = details.getSupplementaryResources();
		assertEquals(2, suppList.size());
		SupplementaryResourceDetails sfd0 = suppList.get(0);
		String contentType0 = sfd0.getContentType();
		assertEquals("application/pdf", contentType0);
		String fileId0 = sfd0.getFileId();
		assertEquals("nchem.213-s1.pdf", fileId0);
		String linkText0 = sfd0.getLinkText();
		assertEquals("Supplementary information - Download PDF file (1,177 KB)", linkText0);
		URI uri0 = sfd0.getURI();
		assertEquals(new URI("http://www.nature.com/nchem/journal/v1/n3/extref/nchem.213-s1.pdf"), uri0);
		SupplementaryResourceDetails sfd2 = suppList.get(1);
		String contentType2 = sfd2.getContentType();
		assertEquals("text/plain; charset=UTF-8", contentType2);
		String fileId2 = sfd2.getFileId();
		assertEquals("nchem.213-s2.cif", fileId2);
		String linkText2 = sfd2.getLinkText();
		assertEquals("Supplementary information - Download cif (35 KB)", linkText2);
		URI uri2 = sfd2.getURI();
		assertEquals(new URI("http://www.nature.com/nchem/journal/v1/n3/extref/nchem.213-s2.cif"), uri2);
	}
	
}
