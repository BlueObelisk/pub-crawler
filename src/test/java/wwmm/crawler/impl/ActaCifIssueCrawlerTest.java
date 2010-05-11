package wwmm.crawler.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import junitx.util.PrivateAccessor;

import org.junit.Test;

import wwmm.pubcrawler.core.ActaIssueCrawler;
import wwmm.pubcrawler.core.SupplementaryResourceDescription;
import wwmm.pubcrawler.impl.ActaCifIssueCrawler;

public class ActaCifIssueCrawlerTest {
	
	/**
	 * Test makes sure that files that are CIFs are recognised as such
	 * in the isCifFile() method.
	 */
	@Test
	public void testIsCifFile() throws Throwable {
		ActaCifIssueCrawler crawler = new ActaCifIssueCrawler(mock(ActaIssueCrawler.class));
		SupplementaryResourceDescription sfd1 = mock(SupplementaryResourceDescription.class);
		String cifUri = "http://scripts.iucr.org/cgi-bin/sendcif?lg3009sup1";
		when(sfd1.getURL()).thenReturn(cifUri);
		// use reflection to access private isCifFile method for testing
		boolean isCif1 = (Boolean) PrivateAccessor.invoke(crawler, "isCifFile", 
				new Class[]{SupplementaryResourceDescription.class}, new Object[]{sfd1});
		assertTrue(isCif1);
		
		String notCifUri = "http://journals.iucr.org/c/issues/2009/04/00/sq3182/sq3182Isup2.hkl";
		when(sfd1.getURL()).thenReturn(notCifUri);
		// use reflection to access private isCifFile method for testing
		boolean isCif2 = (Boolean) PrivateAccessor.invoke(crawler, "isCifFile", 
				new Class[]{SupplementaryResourceDescription.class}, new Object[]{sfd1});
		assertFalse(isCif2);
	}

}
