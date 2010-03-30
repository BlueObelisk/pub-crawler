package wwmm.crawler.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import junitx.util.PrivateAccessor;

import org.junit.Test;

import wwmm.pubcrawler.core.RscIssueCrawler;
import wwmm.pubcrawler.core.SupplementaryResourceDescription;
import wwmm.pubcrawler.impl.RscCifIssueCrawler;

public class RscCifIssueCrawlerTest {
	
	/**
	 * Test makes sure that files that are CIFs are recognised as such
	 * in the isCifFile() method.
	 */
	@Test
	public void testIsCifFile() throws Throwable {
		RscCifIssueCrawler crawler = new RscCifIssueCrawler(mock(RscIssueCrawler.class));
		SupplementaryResourceDescription sfd1 = mock(SupplementaryResourceDescription.class);
		String cifLinkText = "Crystal structure";
		when(sfd1.getLinkText()).thenReturn(cifLinkText);
		// use reflection to access private isCifFile method for testing
		boolean isCif1 = (Boolean) PrivateAccessor.invoke(crawler, "isCifFile", 
				new Class[]{SupplementaryResourceDescription.class}, new Object[]{sfd1});
		assertTrue(isCif1);
		
		String notCifLinkText = "NMR spectra";
		when(sfd1.getLinkText()).thenReturn(notCifLinkText);
		// use reflection to access private isCifFile method for testing
		boolean isCif2 = (Boolean) PrivateAccessor.invoke(crawler, "isCifFile", 
				new Class[]{SupplementaryResourceDescription.class}, new Object[]{sfd1});
		assertFalse(isCif2);
	}

}
