/*
 * Copyright 2010-2011 Nick Day, Sam Adams
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
 */

package wwmm.pubcrawler.crawlers.rsc;

import org.junit.Test;
import wwmm.pubcrawler.CrawlerContext;
import wwmm.pubcrawler.crawlers.AbstractCrawlerTest;
import wwmm.pubcrawler.model.Issue;
import wwmm.pubcrawler.model.Journal;
import wwmm.pubcrawler.model.id.PublisherId;

import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * @author Sam Adams
 */
public class RscJournalCrawlerIntegrationTest extends AbstractCrawlerTest {

    public static final Journal CHEM_COMM = new Journal(new PublisherId("rsc"), "cc", "Chemical communications");

    @Test
    public void testGetCurrentIssueChemicalCommunications() throws IOException {
        CrawlerContext context = new CrawlerContext(null, getHttpCrawler(), new RscCrawlerFactory());
        RscJournalHandler crawler = new RscJournalHandler(CHEM_COMM, context);
        Issue issue = crawler.fetchCurrentIssue();
        assertNotNull(issue);
        assertNotNull(issue.getId());
        assertNotNull(issue.getArticles());
        assertFalse(issue.getArticles().isEmpty());
    }



}
