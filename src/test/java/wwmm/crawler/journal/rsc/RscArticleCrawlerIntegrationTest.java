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
package wwmm.crawler.journal.rsc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static wwmm.pubcrawler.core.CrawlerConstants.RSC_HOMEPAGE_URL;

import java.util.List;

import org.junit.Test;

import wwmm.pubcrawler.core.ArticleCrawler;
import wwmm.pubcrawler.core.ArticleDescription;
import wwmm.pubcrawler.core.ArticleReference;
import wwmm.pubcrawler.core.DOI;
import wwmm.pubcrawler.core.FullTextResourceDescription;
import wwmm.pubcrawler.core.SupplementaryResourceDescription;
import wwmm.pubcrawler.journal.rsc.RscArticleCrawler;

public class RscArticleCrawlerIntegrationTest {

	/**
	 * Goes out to the abstract page for an article at the
	 * CSJ site and scrapes the article details.  Basically
	 * a test that the abstract page HTML has not changed.
	 */
	@Test
	public void testGetArticleDetails() throws NullPointerException {
		DOI doi = new DOI("http://dx.doi.org/10.1039/C0CC01684E");
		ArticleCrawler crawler = new RscArticleCrawler(doi);
		ArticleDescription details = crawler.getDetails();
		assertNotNull("NULL article details", details);
		String authors = details.getAuthors();
		assertEquals("Article authors", "Nathalie Busschaert, Philip A. Gale, Cally J. E. Haynes, Mark E. Light, Stephen J. Moore, Christine C. Tong, Jeffery T. Davis, William A. Harrell, Jr.", authors);
		DOI detailsDoi = details.getDoi();
		assertEquals("Article DOI", doi, detailsDoi);

		List<FullTextResourceDescription> ftrds = details.getFullTextResources();
		assertEquals("Number of fulltext resources found", 2, ftrds.size());
		FullTextResourceDescription ftrd1 = ftrds.get(0);
		assertEquals("Fulltext HTML URI", "http://pubs.rsc.org/en/Content/ArticleHTML/2010/CC/C0CC01684E", ftrd1.getURL());
		assertEquals("Fulltext HTML link text", "HTML", ftrd1.getLinkText());
		assertEquals("Fulltext HTML MIME", "text/html", ftrd1.getContentType());
		FullTextResourceDescription ftrd2 = ftrds.get(1);
		assertEquals("Fulltext PDF URI", "http://pubs.rsc.org/en/Content/ArticlePDF/2010/CC/C0CC01684E", ftrd2.getURL());
		assertEquals("Fulltext PDF link", "PDF", ftrd2.getLinkText());
		assertEquals("Fulltext PDF MIME", "application/pdf", ftrd2.getContentType());
		ArticleReference ref = details.getReference();
		String journalTitle = ref.getJournalTitle();
		assertEquals("Journal in reference", "Chem. Commun.", journalTitle);
		String pages = ref.getPages();
		assertEquals("Pages in reference", "6252 - 6254", pages);
		String volume = ref.getVolume();
		assertEquals("Volume in reference", "46", volume);
		String year = ref.getYear();
		assertEquals("Year in reference", "2010", year);
		String issue = ref.getNumber();
		assertEquals("Issue in reference", "34", issue);
		String title = details.getTitle();
		assertEquals("Title", "Tripodal transmembrane transporters for bicarbonate", title);
		
		List<SupplementaryResourceDescription> suppList = details.getSupplementaryResources();
		assertEquals("Number of supplementary resources found", 2, suppList.size());
		SupplementaryResourceDescription sfd0 = suppList.get(0);
		String contentType0 = sfd0.getContentType();
		assertEquals("First supplementary resource MIME", "application/pdf", contentType0);
		String fileId0 = sfd0.getFileId();
		assertEquals("First supplementary resource file ID", "C0CC01684E", fileId0);
		String linkText0 = sfd0.getLinkText();
		assertEquals("First supplementary resource link text", "Details of synthesis, stability constant determination, crystallography and supplementary membrane transport studies", linkText0);
		String uri0 = sfd0.getURL();
		assertEquals("First supplementary resource URI", "http://www.rsc.org/suppdata/CC/C0/C0CC01684E/C0CC01684E.PDF", uri0);
		
		SupplementaryResourceDescription sfd1 = suppList.get(1);
		String contentType2 = sfd1.getContentType();
		assertEquals("Second supplementary resource MIME", "text/plain", contentType2);
		String fileId2 = sfd1.getFileId();
		assertEquals("Second supplementary resource file ID", "C0CC01684E", fileId2);
		String linkText2 = sfd1.getLinkText();
		assertEquals("Second supplementary resource link text", "Crystal structure data", linkText2);
		String uri2 = sfd1.getURL();
		assertEquals("Second supplementary resource URI", "http://www.rsc.org/suppdata/CC/C0/C0CC01684E/C0CC01684E.TXT", uri2);		
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
		DOI doi1 = new DOI("http://dx.doi.org/10.1039/C0CC01684E");
		ArticleCrawler crawler1 = new RscArticleCrawler(doi1);
		ArticleDescription details1 = crawler1.getDetails();
		ArticleReference ref1 = details1.getReference();
		String title1 = ref1.getJournalTitle();
		assertEquals("Journal abbreviation in first reference", "Chem. Commun.", title1);
		String pages1 = ref1.getPages();
		assertEquals("Pages in first reference", "6252 - 6254", pages1);
		String vol1 = ref1.getVolume();
		assertNotNull("Not NULL volume", vol1);
		String year1 = ref1.getYear();
		assertEquals("Year in first reference", "2010", year1);
		DOI doi2 = new DOI(DOI.DOI_SITE_URL+"/10.1039/b900026g");
		ArticleCrawler crawler2 = new RscArticleCrawler(doi2);
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
