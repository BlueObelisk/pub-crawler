package wwmm.crawler.core;

import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Before;
import org.junit.Test;

import wwmm.pubcrawler.core.SupplementaryResourceDetails;

public class SupplementaryFileDetailsTest {

	SupplementaryResourceDetails sfd;
	URI uri;
	String filename;
	String linkText;
	String contentType;

	@Before
	public void createInstance() throws URISyntaxException, NullPointerException {
		uri = new URI("http://something/filename.txt");
		filename = "filename.txt";
		linkText = "Any old link text here";
		contentType = "image/png";
		sfd = new SupplementaryResourceDetails(uri, filename, linkText, contentType);
	}

	@Test
	public void testAttemptCreateInstanceWithWrongFilename() throws URISyntaxException, NullPointerException {
		uri = new URI("http://something/filename.txt");
		filename = "wrongname";
		linkText = "Any old link text here";
		contentType = "image/png";
		try {
			sfd = new SupplementaryResourceDetails(uri, filename, linkText, contentType);
			fail("Should have failed as filename is not the latter part of the" +
			"provided URI.");
		} catch (Exception e) {
			assertTrue(true);
		}
	}

	@Test
	public void testGetURI() {
		assertSame(uri, sfd.getURI());
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
