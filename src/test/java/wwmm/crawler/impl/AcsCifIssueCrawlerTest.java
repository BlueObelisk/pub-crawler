package wwmm.crawler.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import junitx.util.PrivateAccessor;

import org.junit.Test;

import wwmm.pubcrawler.core.AcsIssueCrawler;
import wwmm.pubcrawler.core.SupplementaryResourceDescription;
import wwmm.pubcrawler.impl.AcsCifIssueCrawler;

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
