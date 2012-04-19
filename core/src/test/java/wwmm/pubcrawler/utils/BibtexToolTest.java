/*
 * Copyright 2010-2011 Nick Day, Sam Adams
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
 */
package wwmm.pubcrawler.utils;

import org.junit.Test;
import wwmm.pubcrawler.model.Reference;

import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author Nick Day
 * @author Sam Adams
 */
public class BibtexToolTest {

	@Test
	public void testFailsOnEmptyFile() throws IOException {
		String bibStr = ResourceUtil.readStringUtf8(getClass(), "./empty-file.bib");
		try {
			new BibtexTool(bibStr);
			fail("Parsing the empty file should have caused an exception.");
		} catch(IllegalStateException e) {
			assertTrue("Should throw like this if the Bibtex file is empty", true);
		}
	}
	
	@Test
	public void testReadsValidFilesWithoutFailing() throws IOException {
		String bibStr1 = ResourceUtil.readStringUtf8(getClass(), "./10.1021_cg801336t.bib");
		new BibtexTool(bibStr1);
		
		String bibStr2 = ResourceUtil.readStringUtf8(getClass(), "./10.1107_S010876730804333X.bib");
		new BibtexTool(bibStr2);
		
		String bibStr3 = ResourceUtil.readStringUtf8(getClass(), "./10.1107_S1600536809002645.bib");
		new BibtexTool(bibStr3);
	}
	
	@Test
	public void testGetsCorrectNumberOfDataItems() throws IOException {
		String bibStr1 = ResourceUtil.readStringUtf8(getClass(), "./10.1107_S0021889809003987.bib");
		BibtexTool bt1 = new BibtexTool(bibStr1);
		Map<String, String> nvps1 = bt1.getNameValuePairs();
		assertEquals(10, nvps1.size());
		
		String bibStr2 = ResourceUtil.readStringUtf8(getClass(), "./10.1021_cm802828z.bib");
		BibtexTool bt2 = new BibtexTool(bibStr2);
		Map<String, String> nvps2 = bt2.getNameValuePairs();
		assertEquals(10, nvps2.size());
	}
	
	@Test
	public void testGetsCorrectValues() throws IOException {
		String bibStr1 = ResourceUtil.readStringUtf8(getClass(), "./10.1107_S0108270109003370.bib");
		BibtexTool bt1 = new BibtexTool(bibStr1);
		String journal = bt1.getValue("journal");
		assertEquals("Acta Crystallographica Section C", journal);
		String year = bt1.getValue("year");
		assertEquals("2009", year);
		String volume = bt1.getValue("volume");
		assertEquals("65", volume);
		String number = bt1.getValue("number");
		assertEquals("3", number);
		String pages = bt1.getValue("pages");
		assertEquals("o92--o96", pages);
		String doi = bt1.getValue("doi");
		assertEquals("10.1107/S0108270109003370", doi);
	}
	
	@Test
	public void testGetReference() throws IOException {
		String bibStr1 = ResourceUtil.readStringUtf8(getClass(), "./10.1021_la8028484.bib");
		BibtexTool bt1 = new BibtexTool(bibStr1);
		Reference ar = bt1.getReference();
		assertNotNull(ar);
		String journal = ar.getJournalTitle();
		assertEquals("Langmuir", journal);
		String year = ar.getYear();
		assertEquals("2009", year);
		String volume = ar.getVolume();
		assertEquals("25", volume);
		String number = ar.getNumber();
		assertEquals("4", number);
		String pages = ar.getPages();
		assertEquals("1893-1896", pages);
	}

}
