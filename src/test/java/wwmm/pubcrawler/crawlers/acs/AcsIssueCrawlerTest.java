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

package wwmm.pubcrawler.crawlers.acs;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.mockito.Mockito;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerRequest;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import uk.ac.cam.ch.wwmm.httpcrawler.HttpCrawler;
import wwmm.pubcrawler.CrawlerContext;
import wwmm.pubcrawler.crawlers.AbstractCrawlerTest;
import wwmm.pubcrawler.model.Article;
import wwmm.pubcrawler.model.Issue;
import wwmm.pubcrawler.types.Doi;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Sam Adams
 */
public class AcsIssueCrawlerTest extends AbstractCrawlerTest {

    private CrawlerResponse prepareJacsIssueResponse() throws IOException {
        return prepareResponse("./jacs-132-51.html",
                URI.create("http://pubs.acs.org/toc/jacsat/132/51"));
    }

    private CrawlerResponse prepareInocajLegacyIssueResponse() throws IOException {
        return prepareResponse("./inocaj-34-25.html",
                URI.create("http://pubs.acs.org/toc/inocaj/34/25"));
    }

    private CrawlerResponse prepareInocajIssue39_19Response() throws IOException {
        return prepareResponse("./inocaj-39-19.html",
                URI.create("http://pubs.acs.org/toc/inocaj/39/19"));
    }

    protected AcsIssueCrawler getJacsIssue132_51() throws IOException {
        Issue issue = new Issue();
        issue.setUrl(URI.create("http://pubs.acs.org/toc/jacsat/132/51"));

        CrawlerResponse response = prepareJacsIssueResponse();

        HttpCrawler crawler = Mockito.mock(HttpCrawler.class);
        Mockito.when(crawler.execute(Mockito.any(CrawlerRequest.class)))
                .thenReturn(response);

        CrawlerContext context = new CrawlerContext(null, crawler, null);
        return new AcsIssueCrawler(issue, context);
    }

    protected AcsIssueCrawler getInocajLegacyIssue() throws IOException {
        Issue issue = new Issue();
        issue.setUrl(URI.create("http://pubs.acs.org/toc/inocaj/34/25"));

        CrawlerResponse response = prepareInocajLegacyIssueResponse();

        HttpCrawler crawler = Mockito.mock(HttpCrawler.class);
        Mockito.when(crawler.execute(Mockito.any(CrawlerRequest.class)))
                .thenReturn(response);

        CrawlerContext context = new CrawlerContext(null, crawler, null);
        return new AcsIssueCrawler(issue, context);
    }

    protected AcsIssueCrawler getInocajIssue39_19() throws IOException {
        Issue issue = new Issue();
        issue.setUrl(URI.create("http://pubs.acs.org/toc/inocaj/39/19"));

        CrawlerResponse response = prepareInocajIssue39_19Response();

        HttpCrawler crawler = Mockito.mock(HttpCrawler.class);
        Mockito.when(crawler.execute(Mockito.any(CrawlerRequest.class)))
                .thenReturn(response);

        CrawlerContext context = new CrawlerContext(null, crawler, null);
        return new AcsIssueCrawler(issue, context);
    }

    @Test
    public void testGetArticleDois() throws IOException {
        AcsIssueCrawler crawler = getJacsIssue132_51();
        List<Article> articles = crawler.getArticles();
        assertEquals(64, articles.size());
        assertEquals(new Doi("10.1021/ja104809x"), articles.get(0).getDoi());
        assertEquals(new Doi("10.1021/ja110154r"), articles.get(63).getDoi());
    }

    @Test
    public void testGetBasicArticleHtmlTitles() throws IOException {
        AcsIssueCrawler crawler = getJacsIssue132_51();
        List<Article> articles = crawler.getArticles();
        assertEquals(64, articles.size());
        assertEquals("<h1 xmlns=\"http://www.w3.org/1999/xhtml\">Accumulative Charge Separation Inspired by Photosynthesis</h1>",
                articles.get(0).getTitleHtml());
    }

    @Test
    public void testGetArticleHtmlTitlesWithEntities() throws IOException {
        AcsIssueCrawler crawler = getJacsIssue132_51();
        List<Article> articles = crawler.getArticles();
        assertEquals(64, articles.size());
        assertEquals("<h1 xmlns=\"http://www.w3.org/1999/xhtml\">Direct Assembly of Polyarenes via C\u2212C Coupling Using PIFA/BF<sub>3</sub>\u00B7Et<sub>2</sub>O</h1>",
                articles.get(1).getTitleHtml());
    }

    @Test
    public void testGetArticleAuthorsWithEntities() throws IOException {
        AcsIssueCrawler crawler = getJacsIssue132_51();
        List<Article> articles = crawler.getArticles();
        assertEquals(64, articles.size());
        List<String> authors = articles.get(0).getAuthors();
        assertEquals(7, authors.size());
        assertEquals("Susanne Karlsson", authors.get(0));
        assertEquals("Julien Boixel", authors.get(1));
        assertEquals("Yann Pellegrin", authors.get(2));
        assertEquals("Errol Blart", authors.get(3));
        assertEquals("Hans-Christian Becker", authors.get(4));
        assertEquals("Fabrice Odobel", authors.get(5));
        assertEquals("Leif Hammarstr\u00f6m", authors.get(6));
    }

    @Test
    public void testGetPreviousIssue() throws IOException {
        AcsIssueCrawler crawler = getJacsIssue132_51();
        Issue prev = crawler.getPreviousIssue();
        assertNotNull(prev);
        assertEquals("acs/jacsat/132/50", prev.getId());
        assertEquals(URI.create("http://pubs.acs.org/toc/jacsat/132/50"), prev.getUrl());
    }

    @Test
    public void testGetVolume() throws IOException {
        AcsIssueCrawler crawler = getJacsIssue132_51();
        assertEquals("132", crawler.getVolume());
    }

    @Test
    public void testGetNumber() throws IOException {
        AcsIssueCrawler crawler = getJacsIssue132_51();
        assertEquals("51", crawler.getNumber());
    }

    @Test
    public void testGetYear() throws IOException {
        AcsIssueCrawler crawler = getJacsIssue132_51();
        assertEquals("2010", crawler.getYear());
    }

    @Test
    public void testGetJournalAbbreviation() throws IOException {
        AcsIssueCrawler crawler = getJacsIssue132_51();
        assertEquals("jacsat", crawler.getJournalAbbreviation());
    }

    @Test
    public void testGetDate() throws IOException {
        AcsIssueCrawler issue = getJacsIssue132_51();
        assertEquals(new LocalDate(2010, 12, 29), issue.getDate());
    }

    @Test
    public void testToIssue() throws IOException {
        AcsIssueCrawler crawler = getJacsIssue132_51();
        Issue issue = crawler.toIssue();
        assertNotNull(issue);
        assertEquals("acs/jacsat/132/51", issue.getId());
        assertEquals("2010", issue.getYear());
        assertEquals("132", issue.getVolume());
        assertEquals("51", issue.getNumber());
        assertNotNull(issue.getArticles());
        assertEquals(64, issue.getArticles().size());
        assertNotNull(issue.getPreviousIssue());
        assertEquals("acs/jacsat/132/50", issue.getPreviousIssue().getId());
    }


    @Test
    public void testGetYearFromLegacyIssue() throws IOException {
        AcsIssueCrawler crawler = getInocajLegacyIssue();
        assertEquals("1995", crawler.getYear());
    }

    @Test
    public void testGetInocaj39_19ArticleDois() throws IOException {
        AcsIssueCrawler crawler = getInocajIssue39_19();
        List<Article> articles = crawler.getArticles();
        assertEquals(30, articles.size());
    }

}
