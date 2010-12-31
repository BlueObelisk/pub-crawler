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

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import wwmm.pubcrawler.core.crawler.IssueCrawler;
import wwmm.pubcrawler.core.model.IssueDescription;

public class IssueDetailsTest {
	
	IssueCrawler issueCrawler;

	@Test
	public void testConstructorAndGetters() {
		String year = "2009";
		String issueId = "28";
		IssueDescription id = new IssueDescription(year, issueId);
		assertSame(year, id.getYear());
		assertSame(issueId, id.getIssueId());
	}

	@Test
	public void testConstructorWithInvalidYear() {
		String invalidYear1 = "200";
		String invalidYear2 = "dave";
		String invalidYear3 = "99999";
		String issueId = "28";
		try {
			IssueDescription id1 = new IssueDescription(invalidYear1, issueId);
			fail("Invalid year string provided ("+invalidYear1+") constructor should have failed.");
		} catch(IllegalStateException e) {
			assertTrue("Should throw like this if the year is invalid.", true);
		}
		try {
			IssueDescription id2 = new IssueDescription(invalidYear2, issueId);
			fail("Invalid year string provided ("+invalidYear2+") constructor should have failed.");
		} catch(IllegalStateException e) {
			assertTrue("Should throw like this if the year in invalid.", true);
		}
		try {
			IssueDescription id3 = new IssueDescription(invalidYear3, issueId);
			fail("Invalid year string provided ("+invalidYear3+") constructor should have failed.");
		} catch(IllegalStateException e) {
			assertTrue("Should throw like this if the year in invalid.", true);
		}
	}

}
