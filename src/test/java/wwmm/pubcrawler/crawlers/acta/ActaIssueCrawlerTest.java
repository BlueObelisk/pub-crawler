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

package wwmm.pubcrawler.crawlers.acta;

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
public class ActaIssueCrawlerTest extends AbstractCrawlerTest {

    private CrawlerResponse prepareActaE2004_11Head() throws IOException {
        return prepareResponse("./e-2004-11-head.html",
                URI.create("http://journals.iucr.org/e/issues/2004/11/00/isscontshdr.html"));
    }

    private CrawlerResponse prepareActaE2004_11Body() throws IOException {
        return prepareResponse("./e-2004-11-body.html",
                URI.create("http://journals.iucr.org/e/issues/2004/11/00/isscontsbdy.html"));
    }


    private CrawlerResponse prepareActaA2010_06Head() throws IOException {
        return prepareResponse("./a-2010-06-head.html",
                URI.create("http://journals.iucr.org/a/issues/2010/06/00/isscontshdr.html"));
    }

    private CrawlerResponse prepareActaA2010_06Body() throws IOException {
        return prepareResponse("./a-2010-06-body.html",
                URI.create("http://journals.iucr.org/a/issues/2010/06/00/isscontsbdy.html"));
    }


    private CrawlerResponse prepareActaB2010_01Body() throws IOException {
        return prepareResponse("./b-2010-01-body.html",
                URI.create("http://journals.iucr.org/b/issues/2010/01/00/isscontsbdy.html"));
    }

    private CrawlerResponse prepareActaB2010_01Head() throws IOException {
        return prepareResponse("./b-2010-01-head.html",
                URI.create("http://journals.iucr.org/b/issues/2010/01/00/isscontshead.html"));
    }


    protected ActaIssueCrawler getActaE2004_11() throws IOException {
        Issue issue = new Issue();
        issue.setId("acta/e/2004/11-00");
        issue.setUrl(URI.create("http://journals.iucr.org/e/issues/2004/11/00/isscontsbdy.html"));

        CrawlerResponse response1 = prepareActaE2004_11Body();
        CrawlerResponse response2 = prepareActaE2004_11Head();

        HttpCrawler crawler = Mockito.mock(HttpCrawler.class);
        Mockito.when(crawler.execute(Mockito.any(CrawlerRequest.class)))
                .thenReturn(response1, response2);

        CrawlerContext context = new CrawlerContext(null, crawler, null);
        return new ActaIssueCrawler(issue, context);
    }


    protected ActaIssueCrawler getActaB2010_01() throws IOException {
        Issue issue = new Issue();
        issue.setId("acta/e/2010/01-00");
        issue.setUrl(URI.create("http://journals.iucr.org/b/issues/2010/01/00/isscontsbdy.html"));

        CrawlerResponse response1 = prepareActaB2010_01Body();
        CrawlerResponse response2 = prepareActaB2010_01Head();

        HttpCrawler crawler = Mockito.mock(HttpCrawler.class);
        Mockito.when(crawler.execute(Mockito.any(CrawlerRequest.class)))
                .thenReturn(response1, response2);

        CrawlerContext context = new CrawlerContext(null, crawler, null);
        return new ActaIssueCrawler(issue, context);
    }

    protected ActaIssueCrawler getActaA2010_06() throws IOException {
        Issue issue = new Issue();
        issue.setId("acta/a/2010/06-00");
        issue.setUrl(URI.create("http://journals.iucr.org/a/issues/2010/06/00/isscontsbdy.html"));

        CrawlerResponse response1 = prepareActaA2010_06Body();
        CrawlerResponse response2 = prepareActaA2010_06Head();

        HttpCrawler crawler = Mockito.mock(HttpCrawler.class);
        Mockito.when(crawler.execute(Mockito.any(CrawlerRequest.class)))
                .thenReturn(response1, response2);

        CrawlerContext context = new CrawlerContext(null, crawler, null);
        return new ActaIssueCrawler(issue, context);
    }

    @Test
    public void testPageWithVolumeInTitle() throws IOException {
        ActaIssueCrawler crawler = getActaA2010_06();
        assertEquals("66", crawler.getVolume());
    }


    @Test
    public void testGetIssueId() throws IOException {
        ActaIssueCrawler crawler = getActaB2010_01();
        assertEquals("acta/b/2010/01-00", crawler.getIssueId());
    }

    @Test
    public void testGetVolume() throws IOException {
        ActaIssueCrawler crawler = getActaB2010_01();
        assertEquals("66", crawler.getVolume());
    }

    @Test
    public void testGetNumber() throws IOException {
        ActaIssueCrawler crawler = getActaB2010_01();
        assertEquals("1", crawler.getNumber());
    }

    @Test
    public void testGetYear() throws IOException {
        ActaIssueCrawler crawler = getActaB2010_01();
        assertEquals("2010", crawler.getYear());
    }

    @Test
    public void testGetArticles() throws IOException {
        ActaIssueCrawler crawler = getActaB2010_01();
        List<Article> articles = crawler.getArticles();
        assertNotNull(articles);
        assertEquals(13, articles.size());

        Article a0 = articles.get(0);
        assertEquals("acta/b/2010/01-00/bk5091", a0.getId());
        assertEquals(new Doi("10.1107/S0108768109053981"), a0.getDoi());

        Article a7 = articles.get(7);
        assertEquals("acta/b/2010/01-00/zb5008", a7.getId());
        assertEquals(new Doi("10.1107/S0108768109048769"), a7.getDoi());

        Article a12 = articles.get(12);
        assertEquals("acta/b/2010/01-00/me0395", a12.getId());
        assertEquals(new Doi("10.1107/S0108768109047855"), a12.getDoi());
    }

    @Test
    public void testGetPreviousIssue() throws IOException {
        ActaIssueCrawler crawler = getActaB2010_01();
        Issue prev = crawler.getPreviousIssue();
        assertNotNull(prev);
        assertEquals("acta/b/2009/06-00", prev.getId());
        assertEquals(URI.create("http://journals.iucr.org/b/issues/2009/06/00/isscontsbdy.html"), prev.getUrl());
    }

    @Test
    public void testToIssue() throws IOException {
        ActaIssueCrawler crawler = getActaB2010_01();
        Issue issue = crawler.toIssue();
        assertEquals("acta/b/2010/01-00", issue.getId());
        assertEquals(URI.create("http://journals.iucr.org/b/issues/2010/01/00/isscontsbdy.html"), issue.getUrl());
        assertEquals("2010", issue.getYear());
        assertEquals("66", issue.getVolume());
        assertEquals("1", issue.getNumber());
        assertNotNull(issue.getArticles());
        assertEquals(13, issue.getArticles().size());
        assertNotNull(issue.getPreviousIssue());
        assertEquals("acta/b/2009/06-00", issue.getPreviousIssue().getId());
    }

    @Test
    public void testGetOldFormatIssueId() throws IOException {
        ActaIssueCrawler crawler = getActaE2004_11();
        assertEquals("acta/e/2004/11-00", crawler.getIssueId());
    }

    @Test
    public void testGetOldFormatVolume() throws IOException {
        ActaIssueCrawler crawler = getActaE2004_11();
        assertEquals("60", crawler.getVolume());
    }

    @Test
    public void testGetOldFormatNumber() throws IOException {
        ActaIssueCrawler crawler = getActaE2004_11();
        assertEquals("11", crawler.getNumber());
    }

    @Test
    public void testGetOldFormatArticles() throws IOException {
        ActaIssueCrawler crawler = getActaE2004_11();
        List<Article> articles = crawler.getArticles();
        assertNotNull(articles);
        assertEquals(178, articles.size());
    }

}
