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
package wwmm.crawler.journal.acs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

import wwmm.pubcrawler.core.crawler.ArticleCrawler;
import wwmm.pubcrawler.core.model.ArticleDescription;
import wwmm.pubcrawler.core.model.ArticleReference;
import wwmm.pubcrawler.core.model.DOI;
import wwmm.pubcrawler.core.model.SupplementaryResourceDescription;
import wwmm.pubcrawler.core.model.FullTextResourceDescription;
import wwmm.pubcrawler.journal.acs.AcsArticleCrawler;

public class AcsArticleCrawlerIntegrationTest {
	
	/**
	 * Goes out to the abstract page for an article at the
	 * ACS site and scrapes the article details.  Basically
	 * a test that the abstract page HTML has not changed.
	 */
	@Test
	public void testGetArticleDetails() throws NullPointerException {
		DOI doi = new DOI(DOI.DOI_SITE_URL+"/10.1021/cg801336t");
		ArticleCrawler crawler = new AcsArticleCrawler(doi);
		ArticleDescription details = crawler.getDetails();
		assertNotNull(details);
		String authors = details.getAuthors();
		assertEquals("Ichiro Hisaki, Norie Shizuki, Kazuaki Aburaya, Masanori Katsuta, Norimitsu Tohnai, Mikiji Miyata", authors);
		DOI detailsDoi = details.getDoi();
		assertEquals(doi, detailsDoi);

		List<FullTextResourceDescription> ftrds = details.getFullTextResources();
		assertEquals(3, ftrds.size());
		FullTextResourceDescription ftrd1 = ftrds.get(0);
		assertEquals("http://pubs.acs.org/doi/full/10.1021/cg801336t", ftrd1.getURL());
		assertEquals("Full Text HTML", ftrd1.getLinkText());
		assertEquals("text/html", ftrd1.getContentType());
		FullTextResourceDescription ftrd2 = ftrds.get(1);
		assertEquals("http://pubs.acs.org/doi/pdf/10.1021/cg801336t", ftrd2.getURL());
		assertEquals("Hi-Res PDF[1954 KB]", ftrd2.getLinkText());
		assertEquals("application/pdf", ftrd2.getContentType());
		FullTextResourceDescription ftrd3 = ftrds.get(2);
		assertEquals("http://pubs.acs.org/doi/pdfplus/10.1021/cg801336t", ftrd3.getURL());
		assertEquals("PDF w/ Links[370 KB]", ftrd3.getLinkText());
		assertEquals("application/pdf", ftrd3.getContentType());
		
		ArticleReference ref = details.getReference();
		String journalTitle = ref.getJournalTitle();
		assertEquals("Cryst. Growth Des.", journalTitle);
		String number = ref.getNumber();
		assertEquals("3", number);
		String pages = ref.getPages();
		assertEquals("1280-1283", pages);
		String volume = ref.getVolume();
		assertEquals("9", volume);
		String year = ref.getYear();
		assertEquals("2009", year);
		String title = details.getTitle();
		assertEquals("Structures of Brucinium Cholate: Bile Acid and Strychnine Derivatives Meet in the Crystals", title);
		
		List<SupplementaryResourceDescription> suppList = details.getSupplementaryResources();
		assertEquals(4, suppList.size());
		SupplementaryResourceDescription sfd0 = suppList.get(0);
		String contentType0 = sfd0.getContentType();
		assertEquals("application/pdf", contentType0);
		String fileId0 = sfd0.getFileId();
		assertEquals("cg801336t_si_001", fileId0);
		String linkText0 = sfd0.getLinkText();
		assertEquals("cg801336t_si_001.pdf (841 KB)", linkText0);
		String url0 = sfd0.getURL();
		assertEquals("http://pubs.acs.org/doi/suppl/10.1021/cg801336t/suppl_file/cg801336t_si_001.pdf", url0);
		SupplementaryResourceDescription sfd2 = suppList.get(2);
		String contentType2 = sfd2.getContentType();
		assertEquals("chemical/x-cif", contentType2);
		String fileId2 = sfd2.getFileId();
		assertEquals("cg801336t_si_003", fileId2);
		String linkText2 = sfd2.getLinkText();
		assertEquals("cg801336t_si_003.cif (69 KB)", linkText2);
		String url2 = sfd2.getURL();
		assertEquals("http://pubs.acs.org/doi/suppl/10.1021/cg801336t/suppl_file/cg801336t_si_003.cif", url2);
	}

}
