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
package wwmm.crawler.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.apache.commons.httpclient.URIException;
import org.junit.Test;

import wwmm.pubcrawler.core.ActaArticleCrawler;
import wwmm.pubcrawler.core.ArticleCrawler;
import wwmm.pubcrawler.core.ArticleDescription;
import wwmm.pubcrawler.core.ArticleReference;
import wwmm.pubcrawler.core.DOI;
import wwmm.pubcrawler.core.FullTextResourceDescription;
import wwmm.pubcrawler.core.SupplementaryResourceDescription;

public class ActaArticleCrawlerIntegrationTest {
	
	/**
	 * Goes out to the abstract page for an article at the
	 * Acta site and scrapes the article details.  Basically
	 * a test that the abstract page HTML has not changed.
	 */
	@Test
	public void testGetArticleDetails() throws URIException, NullPointerException {
		DOI doi = new DOI(DOI.DOI_SITE_URL+"/10.1107/S0108270109006118");
		ArticleCrawler crawler = new ActaArticleCrawler(doi);
		ArticleDescription details = crawler.getDetails();
		assertNotNull(details);
		String authors = details.getAuthors();
		assertEquals("Kim, Jinyoung and Ahn, Docheon and Kulshreshtha, Chandramouli and Sohn, Kee-Sun and Shin, Namsoo", authors);
		DOI detailsDoi = details.getDoi();
		assertEquals(doi, detailsDoi);

		List<FullTextResourceDescription> ftrds = details.getFullTextResources();
		assertEquals(2, ftrds.size());
		FullTextResourceDescription ftrd1 = ftrds.get(0);
		assertEquals("http://journals.iucr.org/c/issues/2009/04/00/sq3185/index.html", ftrd1.getURL());
		assertEquals("HTML", ftrd1.getLinkText());
		assertEquals("text/html", ftrd1.getContentType());
		FullTextResourceDescription ftrd2 = ftrds.get(1);
		assertEquals("http://journals.iucr.org/c/issues/2009/04/00/sq3185/sq3185.pdf", ftrd2.getURL());
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
		
		List<SupplementaryResourceDescription> suppList = details.getSupplementaryResources();
		assertEquals(1, suppList.size());
		SupplementaryResourceDescription sfd0 = suppList.get(0);
		String contentType0 = sfd0.getContentType();
		assertEquals("text/plain; charset=utf-8", contentType0);
		String fileId0 = sfd0.getFileId();
		assertEquals("sq3185sup1", fileId0);
		String linkText0 = sfd0.getLinkText();
		assertEquals("CIF", linkText0);
		String url0 = sfd0.getURL();
		assertEquals("http://scripts.iucr.org/cgi-bin/sendcif?sq3185sup1", url0);
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
		ArticleCrawler crawler = new ActaArticleCrawler(doi);
		ArticleDescription details = crawler.getDetails();
		assertEquals(2, details.getSupplementaryResources().size());
	}

}
