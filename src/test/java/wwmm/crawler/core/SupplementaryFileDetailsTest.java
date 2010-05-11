package wwmm.crawler.core;

import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import org.apache.commons.httpclient.URIException;
import org.junit.Before;
import org.junit.Test;

import wwmm.pubcrawler.core.SupplementaryResourceDescription;

public class SupplementaryFileDetailsTest {

	SupplementaryResourceDescription sfd;
	String url;
	String filename;
	String linkText;
	String contentType;

	@Before
	public void createInstance() throws URIException, NullPointerException {
		url = "http://something/filename.txt";
		filename = "filename.txt";
		linkText = "Any old link text here";
		contentType = "image/png";
		sfd = new SupplementaryResourceDescription(url, filename, linkText, contentType);
	}

	@Test
	public void testAttemptCreateInstanceWithWrongFilename() throws URIException, NullPointerException {
		url = "http://something/filename.txt";
		filename = "wrongname";
		linkText = "Any old link text here";
		contentType = "image/png";
		try {
			sfd = new SupplementaryResourceDescription(url, filename, linkText, contentType);
			fail("Should have failed as filename is not the latter part of the" +
			"provided URI.");
		} catch (Exception e) {
			assertTrue(true);
		}
	}

	@Test
	public void testGetURI() {
		assertSame(url, sfd.getURL());
	}

	@Test
	public void testGetFilename() {
		assertSame(filename, sfd.getFileId());
	}

	@Test
	public void testGetLinkText() {
		assertSame(linkText, sfd.getLinkText());
	}

	@Test
	public void testGetContentType() {
		assertSame(contentType, sfd.getContentType());
	}

}
