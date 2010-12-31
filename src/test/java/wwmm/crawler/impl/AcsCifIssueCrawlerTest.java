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
package wwmm.crawler.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import junitx.util.PrivateAccessor;

import org.junit.Test;

import wwmm.pubcrawler.core.model.SupplementaryResourceDescription;
import wwmm.pubcrawler.impl.AcsCifIssueCrawler;
import wwmm.pubcrawler.journal.acs.AcsIssueCrawler;

public class AcsCifIssueCrawlerTest {
	
	/**
	 * Test makes sure that files that are CIFs are recognised as such
	 * in the isCifFile() method.
	 */
	@Test
	public void testIsCifFile() throws Throwable {
		AcsCifIssueCrawler crawler = new AcsCifIssueCrawler(mock(AcsIssueCrawler.class));
		SupplementaryResourceDescription sfd1 = mock(SupplementaryResourceDescription.class);
		String cifContentType = "chemical/x-cif";
		when(sfd1.getContentType()).thenReturn(cifContentType);
		// use reflection to access private isCifFile method for testing
		boolean isCif1 = (Boolean) PrivateAccessor.invoke(crawler, "isCifFile", 
				new Class[]{SupplementaryResourceDescription.class}, new Object[]{sfd1});
		assertTrue(isCif1);
		
		String notCifContentType = "text/plain";
		when(sfd1.getContentType()).thenReturn(notCifContentType);
		// use reflection to access private isCifFile method for testing
		boolean isCif2 = (Boolean) PrivateAccessor.invoke(crawler, "isCifFile", 
				new Class[]{SupplementaryResourceDescription.class}, new Object[]{sfd1});
		assertFalse(isCif2);
	}

}
