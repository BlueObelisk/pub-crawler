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

package wwmm.pubcrawler.crawlers.nature;

import org.junit.Test;
import org.mockito.Mockito;
import wwmm.pubcrawler.CrawlerContext;
import wwmm.pubcrawler.crawlers.AbstractCrawlerTest;
import wwmm.pubcrawler.model.Article;
import wwmm.pubcrawler.model.Issue;
import wwmm.pubcrawler.types.Doi;
import wwmm.pubcrawler.httpcrawler.CrawlerRequest;
import wwmm.pubcrawler.httpcrawler.CrawlerResponse;
import wwmm.pubcrawler.httpcrawler.HttpCrawler;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Sam Adams
 */
public class NatureIssueCrawlerTest extends AbstractCrawlerTest {

    private CrawlerResponse prepareJacsIssueResponse() throws IOException {
        return prepareResponse("./nchem-issue-3-1.html",
                URI.create("http://www.nature.com/nchem/journal/v3/n1/index.html"));
    }

    protected NatureIssueCrawler getNchem3_1() throws IOException {
        Issue issue = new Issue();
        issue.setId("nature/nchem/3/1");
        issue.setUrl(URI.create("http://www.nature.com/nchem/journal/v3/n1/index.html"));

        CrawlerResponse response = prepareJacsIssueResponse();

        HttpCrawler crawler = Mockito.mock(HttpCrawler.class);
        Mockito.when(crawler.execute(Mockito.any(CrawlerRequest.class))).thenReturn(response);
//        HttpClient client = Mockito.mock(HttpClient.class);
//        Mockito.when(client.execute(Mockito.any(HttpUriRequest.class), Mockito.any(HttpContext.class)))
//                    .thenReturn(response);

        CrawlerContext context = new CrawlerContext(null, crawler, null);
        return new NatureIssueCrawler(issue, context);
    }

    @Test
    public void testGetArticleIds() throws IOException {
        NatureIssueCrawler crawler = getNchem3_1();
        List<Article> ids = crawler.getArticles();
        assertEquals(25, ids.size());
        assertEquals(new Doi("10.1038/nchem.933"), ids.get(0).getDoi());
        assertEquals(new Doi("10.1038/nchem.944"), ids.get(24).getDoi());
    }

    @Test
    public void testGetPreviousIssue() throws IOException {
        NatureIssueCrawler crawler = getNchem3_1();
        Issue prev = crawler.getPreviousIssue();
        assertNotNull(prev);
        assertEquals("nature/nchem/2/12", prev.getId());
        assertEquals(URI.create("http://www.nature.com/nchem/journal/v2/n12/index.html"), prev.getUrl());
    }

    @Test
    public void testGetVolume() throws IOException {
        NatureIssueCrawler crawler = getNchem3_1();
        assertEquals("3", crawler.getVolume());
    }

    @Test
    public void testGetNumber() throws IOException {
        NatureIssueCrawler crawler = getNchem3_1();
        assertEquals("1", crawler.getNumber());
    }

    @Test
    public void testGetYear() throws IOException {
        NatureIssueCrawler crawler = getNchem3_1();
        assertEquals("2011", crawler.getYear());
    }

    @Test
    public void testGetJournalAbbreviation() throws IOException {
        NatureIssueCrawler crawler = getNchem3_1();
        assertEquals("nchem", crawler.getJournalAbbreviation());
    }

    @Test
    public void testToIssue() throws IOException {
        NatureIssueCrawler crawler = getNchem3_1();
        Issue issue = crawler.toIssue();
        assertNotNull(issue);
        assertEquals("nature/nchem/3/1", issue.getId());
        assertEquals("2011", issue.getYear());
        assertEquals("3", issue.getVolume());
        assertEquals("1", issue.getNumber());
        assertNotNull(issue.getArticles());
        assertEquals(25, issue.getArticles().size());
        assertNotNull(issue.getPreviousIssue());
        assertEquals("nature/nchem/2/12", issue.getPreviousIssue().getId());
    }

}