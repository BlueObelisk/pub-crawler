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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import wwmm.pubcrawler.core.ArticleDescription;
import wwmm.pubcrawler.core.ArticleReference;
import wwmm.pubcrawler.core.DOI;
import wwmm.pubcrawler.core.SupplementaryResourceDescription;

public class ArticleDetailsTest {
	
	@Test
	public void testConstructor() {
		ArticleDescription ad = new ArticleDescription();
		assertNull("DOI should initially be null when using the default constructor.", ad.getDoi());
		assertNull("Title should initially be null when using the default constructor.", ad.getTitle());
		assertNull("Authors should initially be null when using the default constructor.", ad.getAuthors());
		assertNull("Reference should initially be null when using the default constructor.", ad.getReference());
		assertEquals(0, ad.getSupplementaryResources().size());
		assertEquals(0, ad.getFullTextResources().size());
		assertTrue(ad.hasBeenPublished());
	}
	
	@Test
	public void testSetAndGetDOI() {
		ArticleDescription ad = new ArticleDescription();
		DOI doi = mock(DOI.class);
		ad.setDoi(doi);
		assertSame(doi, ad.getDoi());
	}
	
	@Test 
	public void testSetAndGetTitle() {
		ArticleDescription ad = new ArticleDescription();
		String title = "Any old title will do";
		ad.setTitle(title);
		assertSame(title, ad.getTitle());
	}
	
	@Test
	public void testSetAndGetAuthors() {
		ArticleDescription ad = new ArticleDescription();
		String authors = "N. E. Day, O. J. Downing, J. A. Townsend, P. Murray-Rust";
		ad.setAuthors(authors);
		assertSame(authors, ad.getAuthors());
	}
	
	@Test
	public void testSetAndGetReference() {
		ArticleDescription ad = new ArticleDescription();
		ArticleReference reference = mock(ArticleReference.class);
		ad.setReference(reference);
		assertSame(reference, ad.getReference());
	}
	
	@Test
	public void testSetAndGetSupplementaryFileList() {
		ArticleDescription ad = new ArticleDescription();
		List<SupplementaryResourceDescription> sfd = mock(ArrayList.class);
		ad.setSupplementaryResources(sfd);
		assertEquals(sfd, ad.getSupplementaryResources());
	}

}
