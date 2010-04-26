package wwmm.crawler.core;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import java.util.List;

import nu.xom.Document;

import org.junit.Test;

import wwmm.pubcrawler.core.ActaIssueCrawler;
import wwmm.pubcrawler.core.ActaJournal;
import wwmm.pubcrawler.core.DOI;
import wwmm.pubcrawler.core.IssueDescription;

public class ActaIssueCrawlerIntegrationTest {
	
	/**
	 * Goes out to the Acta site to check that the correct number
	 * of DOIs are scraped from a particular issue.  Basically a 
	 * check that the table of contents HTML structure hasn't 
	 * been changed.
	 */
	@Test
	public void testGetIssueDois() {
		IssueDescription details = new IssueDescription("2009", "01-00");
		ActaIssueCrawler crawler = new ActaIssueCrawler(ActaJournal.SECTION_C);
		List<DOI> doiList = crawler.getDois(details);
		assertEquals(23, doiList.size());
		assertEquals(new DOI(DOI.DOI_SITE_URL+"/10.1107/S0108270108041504"), doiList.get(9));
	}

	/**
	 * Test that the current issue is returned successfully
	 * i.e. an exception will be thrown if the returned HTTP 
	 * status is not 200.  Is a test that the current issues 
	 * are still at the same URL template.
	 */
	@Test
	public void testGetCurrentIssueHtml() {
		ActaIssueCrawler crawler = new ActaIssueCrawler(ActaJournal.SECTION_C);
		Document doc = crawler.getCurrentIssueHtml();
		assertNotNull(doc);
	}

}
