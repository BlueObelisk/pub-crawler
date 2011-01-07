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
import org.mockito.Mockito;
import wwmm.pubcrawler.crawlers.AbstractCrawlerTest;
import wwmm.pubcrawler.CrawlerContext;
import wwmm.pubcrawler.model.Article;
import wwmm.pubcrawler.model.Issue;
import wwmm.pubcrawler.types.Doi;
import wwmm.pubcrawler.httpcrawler.CrawlerRequest;
import wwmm.pubcrawler.httpcrawler.CrawlerResponse;
import wwmm.pubcrawler.httpcrawler.HttpCrawler;
import wwmm.pubcrawler.journals.RscJournalIndex;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Sam Adams
 */
public class RscIssueCrawlerTest extends AbstractCrawlerTest {

    private CrawlerResponse prepareCcIssueResponse() throws IOException {
        return prepareResponse("./cc-issue-47-03.html",
                URI.create("http://pubs.rsc.org/en/Journals/JournalIssues/CC"));
    }

    protected RscIssueCrawler getCcIssue() throws IOException {
        Issue issue = new Issue();
        issue.setId("rsc/CC/47/3");
        issue.setUrl(URI.create("CC047002"));

        CrawlerResponse response = prepareCcIssueResponse();

        HttpCrawler crawler = Mockito.mock(HttpCrawler.class);
        Mockito.when(crawler.execute(Mockito.any(CrawlerRequest.class)))
                    .thenReturn(response);

        CrawlerContext context = new CrawlerContext(null, crawler, null);
        return new RscIssueCrawler(issue, RscJournalIndex.CHEMICAL_COMMUNICATIONS, context);
    }

    @Test
    public void testGetArticleIds() throws IOException {
        RscIssueCrawler crawler = getCcIssue();
        List<Article> ids = crawler.getArticles();
        assertEquals(74, ids.size());
        assertEquals(new Doi("10.1039/C0CC90154G"), ids.get(0).getDoi());
        assertEquals(new Doi("10.1039/C0CC90157A"), ids.get(73).getDoi());
    }

    @Test
    public void testGetPreviousIssue() throws IOException {
        RscIssueCrawler crawler = getCcIssue();
        Issue prev = crawler.getPreviousIssue();
        assertNotNull(prev);
        assertEquals("rsc/CC/47/2", prev.getId());
        assertEquals(URI.create("CC047002"), prev.getUrl());
    }

    @Test
    public void testGetVolume() throws IOException {
        RscIssueCrawler crawler = getCcIssue();
        assertEquals("47", crawler.getVolume());
    }

    @Test
    public void testGetNumber() throws IOException {
        RscIssueCrawler crawler = getCcIssue();
        assertEquals("3", crawler.getNumber());
    }

    @Test
    public void testGetYear() throws IOException {
        RscIssueCrawler crawler = getCcIssue();
        assertEquals("2011", crawler.getYear());
    }

//    @Test
//    public void testGetJournalAbbreviation() throws IOException {
//        RscIssueCrawler crawler = getCcIssue();
//        assertEquals("jacsat", crawler.getJournalAbbreviation());
//    }
//
//    @Test
//    public void testGetDate() throws IOException {
//        RscIssueCrawler crawler = getCcIssue();
//        assertEquals(new LocalDate(2010, 12, 29), issue.getDate());
//    }
    
    @Test
    public void testToIssue() throws IOException {
        RscIssueCrawler crawler = getCcIssue();
        Issue issue = crawler.toIssue();
        assertNotNull(issue);
        assertEquals("rsc/CC/47/3", issue.getId());
        assertEquals("2011", issue.getYear());
        assertEquals("47", issue.getVolume());
        assertEquals("3", issue.getNumber());
        assertNotNull(issue.getArticles());
        assertEquals(74, issue.getArticles().size());
        assertNotNull(issue.getPreviousIssue());
        assertEquals("rsc/CC/47/2", issue.getPreviousIssue().getId());
    }

}
