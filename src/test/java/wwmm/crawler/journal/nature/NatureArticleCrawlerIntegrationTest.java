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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static wwmm.pubcrawler.core.CrawlerConstants.CIF_CONTENT_TYPE;

import java.util.List;

import org.junit.Test;

import wwmm.pubcrawler.core.model.ArticleDescription;
import wwmm.pubcrawler.core.model.ArticleReference;
import wwmm.pubcrawler.core.model.FullTextResourceDescription;
import wwmm.pubcrawler.core.model.SupplementaryResourceDescription;
import wwmm.pubcrawler.core.model.DOI;
import wwmm.pubcrawler.journal.nature.NatureArticleCrawler;

public class NatureArticleCrawlerIntegrationTest {

	/**
	 * Goes out to the abstract page for an article at the
	 * Nature site and scrapes the article details.  Basically
	 * a test that the abstract page HTML has not changed.
	 */
	@Test
	public void testGetArticleDetails() throws NullPointerException {
		DOI doi = new DOI(DOI.DOI_SITE_URL+"/10.1038/nchem.213");
		NatureArticleCrawler crawler = new NatureArticleCrawler(doi);
		ArticleDescription details = crawler.getDetails();
		assertNotNull(details);
		String authors = details.getAuthors();
		assertEquals("Tanya K. Ronson, Julie Fisher, Lindsay P. Harding, Pierre J. Rizkallah, John E. Warren, Michaele J. Hardie", authors);
		DOI detailsDoi = details.getDoi();
		assertEquals(doi, detailsDoi);

		List<FullTextResourceDescription> ftrds = details.getFullTextResources();
		assertEquals(1, ftrds.size());
		FullTextResourceDescription ftrd2 = ftrds.get(0);
		assertEquals("http://www.nature.com/nchem/journal/v1/n3/pdf/nchem.213.pdf", ftrd2.getURL());
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
//		String title = details.getTitle();
//		assertEquals("Stellated polyhedral assembly of a topologically complicated Pd4L4 |[lsquo]|Solomon cube|[rsquo]|", title);
		
		List<SupplementaryResourceDescription> suppList = details.getSupplementaryResources();
		assertEquals(2, suppList.size());
		SupplementaryResourceDescription sfd0 = suppList.get(0);
		String contentType0 = sfd0.getContentType();
		assertEquals("application/pdf", contentType0);
		String fileId0 = sfd0.getFileId();
		assertEquals("nchem.213-s1.pdf", fileId0);
		String linkText0 = sfd0.getLinkText();
		assertEquals("Supplementary information - Download PDF file (1,177 KB)", linkText0);
		String uri0 = sfd0.getURL();
		assertEquals("http://www.nature.com/nchem/journal/v1/n3/extref/nchem.213-s1.pdf", uri0);
		SupplementaryResourceDescription sfd2 = suppList.get(1);
		String contentType2 = sfd2.getContentType();
		assertEquals(CIF_CONTENT_TYPE, contentType2);
		String fileId2 = sfd2.getFileId();
		assertEquals("nchem.213-s2.cif", fileId2);
		String linkText2 = sfd2.getLinkText();
		assertEquals("Supplementary information - Download cif (35 KB)", linkText2);
		String uri2 = sfd2.getURL();
		assertEquals("http://www.nature.com/nchem/journal/v1/n3/extref/nchem.213-s2.cif", uri2);
	}
	
}
