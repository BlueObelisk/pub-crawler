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

import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertSame;

import org.junit.Test;

import wwmm.pubcrawler.core.model.ArticleReference;

public class ArticleReferenceTest {
	
	@Test
	public void testConstructor() {
		ArticleReference ar = new ArticleReference();
		assertNull(ar.getJournalTitle());
		assertNull(ar.getYear());
		assertNull(ar.getVolume());
		assertNull(ar.getNumber());
		assertNull(ar.getPages());
	}
	
	@Test 
	public void testSetAndGetJournal() {
		ArticleReference ar = new ArticleReference();
		String journal = "Acta. Cryst. E";
		ar.setJournalTitle(journal);
		assertSame(journal, ar.getJournalTitle());
	}
	
	@Test 
	public void testSetAndGetYear() {
		ArticleReference ar = new ArticleReference();
		String year = "2009";
		ar.setYear(year);
		assertSame(year, ar.getYear());
	}
	
	@Test 
	public void testSetAndGetVolume() {
		ArticleReference ar = new ArticleReference();
		String volume = "8";
		ar.setVolume(volume);
		assertSame(volume, ar.getVolume());
	}
	
	@Test 
	public void testSetAndGetNumber() {
		ArticleReference ar = new ArticleReference();
		String number = "28";
		ar.setNumber(number);
		assertSame(number, ar.getNumber());
	}
	
	@Test 
	public void testSetAndGetPage() {
		ArticleReference ar = new ArticleReference();
		String pages = "499-508";
		ar.setPages(pages);
		assertSame(pages, ar.getPages());
	}

}
