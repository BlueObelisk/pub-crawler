package wwmm.crawler.core;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import java.util.List;

import nu.xom.Document;

import org.junit.Test;

import wwmm.pubcrawler.core.AcsIssueCrawler;
import wwmm.pubcrawler.core.AcsJournal;
import wwmm.pubcrawler.core.DOI;
import wwmm.pubcrawler.core.IssueDetails;
import wwmm.pubcrawler.core.NatureIssueCrawler;
import wwmm.pubcrawler.core.NatureJournal;

public class NatureIssueCrawlerIntegrationTest {
	
	/**
	 * Goes out to the Nature site to check that the correct number
	 * of DOIs are scraped from a particular issue.  Basically a 
	 * check that the table of contents HTML structure hasn't 
	 * been changed.
	 */
	@Test
	public void testGetIssueDois() {
		IssueDetails details = new IssueDetails("2009", "3");
		NatureIssueCrawler crawler = new NatureIssueCrawler(NatureJournal.CHEMISTRY);
		List<DOI> doiList = crawler.getDOIs(details);
		assertEquals(23, doiList.size());
		assertEquals(new DOI(DOI.DOI_SITE_URL+"/10.1038/nchem.223"), doiList.get(9));
	}

	/**
	 * Test that the current issue is returned successfully
	 * i.e. an exception will be thrown if the returned HTTP 
	 * status is not 200.  Is a test that the current issues 
	 * are still at the same URL template.
	 */
	@Test
	public void testGetCurrentIssueHtml() {
		NatureIssueCrawler crawler = new NatureIssueCrawler(NatureJournal.CHEMISTRY);
		Document doc = crawler.getCurrentIssueHtml();
		assertNotNull(doc);
	}

}
