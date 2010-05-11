package wwmm.crawler.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.httpclient.URIException;
import org.junit.Before;
import org.junit.Test;

import wwmm.pubcrawler.core.DOI;

public class DOITest {

	String invalidUriUrl;
	String invalidDoiUrl;
	String validDoiUrl;

	@Before
	public void setupUrlsAndUris() throws URIException, NullPointerException {
		invalidUriUrl = "thisisnotavaliduri";
		invalidDoiUrl = "http://www.google.com";
		validDoiUrl = "http://dx.doi.org/10.1039/b815603d";
	}

	@Test
	public void testStringConstructor() {
		try {
			DOI doi1 = new DOI(invalidUriUrl);
			fail("Invalid URI string provided ("+invalidUriUrl+") constructor should have failed.");
		} catch(Exception e) {
			assertTrue("Should throw like this if the URI is invalid.", true);
		}
		try {
			DOI doi2 = new DOI(invalidDoiUrl);
			fail("Invalid DOI string provided ("+invalidDoiUrl+") constructor should have failed.");
		} catch(Exception e) {
			assertTrue("Should throw like this if the DOI is invalid.", true);
		}
		DOI doi3 = new DOI(validDoiUrl);
	}

	@Test
	public void testUriConstructor() {
		try {
			DOI doi1 = new DOI(invalidUriUrl);
			fail("Invalid URI provided ("+invalidUriUrl.toString()+") constructor should have failed.");
		} catch(Exception e) {
			assertTrue("Should throw like this if the URI is invalid.", true);
		}
		try {
			DOI doi2 = new DOI(invalidDoiUrl);
			fail("Invalid DOI provided ("+invalidDoiUrl.toString()+") constructor should have failed.");
		} catch(Exception e) {
			assertTrue("Should throw like this if the DOI is invalid.", true);
		}
		DOI doi3 = new DOI(validDoiUrl);
	}

	@Test
	public void testGetURI() {
		DOI doi1 = new DOI(validDoiUrl);
		String url = doi1.getURL();
		assertSame(validDoiUrl, url);
	}

	@Test
	public void testToString() {
		DOI doi1 = new DOI(validDoiUrl);
		assertEquals(validDoiUrl, doi1.toString());
		DOI doi2 = new DOI(validDoiUrl);
		assertEquals(validDoiUrl, doi2.toString());
	}

	@Test
	public void testDoiWithNoPrefix() {
		try {
			DOI doi1 = new DOI(DOI.DOI_SITE_URL);
			fail("Invalid DOI should have thrown an exception up object creation.");
		} catch (Exception e) {
			assertTrue(true);
		}
		try {
			DOI doi2 = new DOI(DOI.DOI_SITE_URL+"/");
			fail("Invalid DOI should have thrown an exception up object creation.");
		} catch (Exception e) {
			assertTrue(true);
		}
	}
}
