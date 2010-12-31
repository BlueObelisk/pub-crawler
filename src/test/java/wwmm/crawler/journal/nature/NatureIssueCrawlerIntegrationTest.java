/*******************************************************************************
 * Copyright 2010 Nick Day
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package wwmm.crawler.journal.nature;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import java.util.List;

import nu.xom.Document;

import org.junit.Test;

import wwmm.pubcrawler.core.crawler.IssueCrawler;
import wwmm.pubcrawler.core.model.DOI;
import wwmm.pubcrawler.core.model.IssueDescription;
import wwmm.pubcrawler.journal.nature.NatureIssueCrawler;
import wwmm.pubcrawler.journal.nature.NatureJournalIndex;

public class NatureIssueCrawlerIntegrationTest {
	
	/**
	 * Goes out to the Nature site to check that the correct number
	 * of DOIs are scraped from a particular issue.  Basically a 
	 * check that the table of contents HTML structure hasn't 
	 * been changed.
	 */
	@Test
	public void testGetIssueDois() {
		IssueDescription details = new IssueDescription("2009", "3");
		IssueCrawler crawler = new NatureIssueCrawler(NatureJournalIndex.NATURE_CHEMISTRY);
		List<DOI> doiList = crawler.getDois(details);
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
		IssueCrawler crawler = new NatureIssueCrawler(NatureJournalIndex.NATURE_CHEMISTRY);
		Document doc = crawler.getCurrentIssueHtml();
		assertNotNull(doc);
	}

}
