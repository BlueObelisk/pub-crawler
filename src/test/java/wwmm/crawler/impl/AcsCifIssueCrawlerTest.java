package wwmm.crawler.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URI;

import junitx.util.PrivateAccessor;

import org.junit.Test;

import wwmm.pubcrawler.core.AcsIssueCrawler;
import wwmm.pubcrawler.core.SupplementaryResourceDetails;
import wwmm.pubcrawler.impl.AcsCifIssueCrawler;


public class AcsCifIssueCrawlerTest {
	
	/**
	 * Test makes sure that files that are CIFs are recognised as such
	 * in the isCifFile() method.
	 */
	@Test
	public void testIsCifFile() throws Throwable {
		AcsCifIssueCrawler crawler = new AcsCifIssueCrawler(mock(AcsIssueCrawler.class));
		SupplementaryResourceDetails sfd1 = mock(SupplementaryResourceDetails.class);
		URI cifFileUri = new URI("http://www.fake.com/this-is-a-cif-file.cif");
		when(sfd1.getURI()).thenReturn(cifFileUri);
		// use reflection to access private isCifFile method for testing
		boolean isCif1 = (Boolean) PrivateAccessor.invoke(crawler, "isCifFile", 
				new Class[]{SupplementaryResourceDetails.class}, new Object[]{sfd1});
		assertTrue(isCif1);
		
		URI notCifFileUri = new URI("http://www.fake.com/not-a-cif.txt");
		when(sfd1.getURI()).thenReturn(notCifFileUri);
		// use reflection to access private isCifFile method for testing
		boolean isCif2 = (Boolean) PrivateAccessor.invoke(crawler, "isCifFile", 
				new Class[]{SupplementaryResourceDetails.class}, new Object[]{sfd1});
		assertFalse(isCif2);
	}

}
