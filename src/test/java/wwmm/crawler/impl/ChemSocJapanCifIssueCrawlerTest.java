package wwmm.crawler.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import junitx.util.PrivateAccessor;

import org.junit.Test;

import wwmm.pubcrawler.core.ChemSocJapanIssueCrawler;
import wwmm.pubcrawler.core.SupplementaryResourceDetails;
import wwmm.pubcrawler.impl.ChemSocJapanCifIssueCrawler;

public class ChemSocJapanCifIssueCrawlerTest {
	
	/**
	 * Test makes sure that files that are CIFs are recognised as such
	 * in the isCifFile() method.
	 */
	@Test
	public void testIsCifFile() throws Throwable {
		ChemSocJapanCifIssueCrawler crawler = new ChemSocJapanCifIssueCrawler(mock(ChemSocJapanIssueCrawler.class));
		SupplementaryResourceDetails sfd1 = mock(SupplementaryResourceDetails.class);
		String cifLinkText = "CIF";
		when(sfd1.getLinkText()).thenReturn(cifLinkText);
		// use reflection to access private isCifFile method for testing
		boolean isCif1 = (Boolean) PrivateAccessor.invoke(crawler, "isCifFile", 
				new Class[]{SupplementaryResourceDetails.class}, new Object[]{sfd1});
		assertTrue(isCif1);
		
		String notCifLinkText = "Supplementary Details";
		when(sfd1.getLinkText()).thenReturn(notCifLinkText);
		// use reflection to access private isCifFile method for testing
		boolean isCif2 = (Boolean) PrivateAccessor.invoke(crawler, "isCifFile", 
				new Class[]{SupplementaryResourceDetails.class}, new Object[]{sfd1});
		assertFalse(isCif2);
	}

}
