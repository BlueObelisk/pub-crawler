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
package wwmm.crawler.core;

import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import wwmm.pubcrawler.core.model.SupplementaryResourceDescription;

public class SupplementaryFileDetailsTest {

	SupplementaryResourceDescription sfd;
	String url;
	String filename;
	String linkText;
	String contentType;

	@Before
	public void createInstance() throws NullPointerException {
		url = "http://something/filename.txt";
		filename = "filename.txt";
		linkText = "Any old link text here";
		contentType = "image/png";
		sfd = new SupplementaryResourceDescription(url, filename, linkText, contentType);
	}

	@Test
	public void testAttemptCreateInstanceWithWrongFilename() throws NullPointerException {
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
