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

package wwmm.pubcrawler.crawlers.chemsocjapan;

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
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Sam Adams
 */
public class ChemSocJapanIssueCrawlerTest extends AbstractCrawlerTest {

    private CrawlerResponse prepareCl2010_3() throws IOException {
        return prepareResponse("./cl-2010-3.html",
                URI.create("http://www.csj.jp/journals/chem-lett/cl-cont/cl2010-3.html"));
    }

    protected ChemSocJapanIssueCrawler getCl2010_3() throws IOException {
        Issue issue = new Issue();
        issue.setId("chemsocjapan/cl/2010/3");
        issue.setUrl(URI.create("http://www.csj.jp/journals/chem-lett/cl-cont/cl2010-3.html"));

        CrawlerResponse response = prepareCl2010_3();
        
        HttpCrawler crawler = Mockito.mock(HttpCrawler.class);
        Mockito.when(crawler.execute(Mockito.any(CrawlerRequest.class)))
                    .thenReturn(response);

        CrawlerContext context = new CrawlerContext(null, crawler, null);
        return new ChemSocJapanIssueCrawler(issue, context);
    }


    @Test
    public void testGetIssueId() throws IOException {
        ChemSocJapanIssueCrawler crawler = getCl2010_3();
        assertEquals("chemsocjapan/chem-lett/39/3", crawler.getIssueId());
    }

    @Test
    public void testGetVolume() throws IOException {
        ChemSocJapanIssueCrawler crawler = getCl2010_3();
        assertEquals("39", crawler.getVolume());
    }

    @Test
    public void testGetNumber() throws IOException {
        ChemSocJapanIssueCrawler crawler = getCl2010_3();
        assertEquals("3", crawler.getNumber());
    }

    @Test
    public void testGetYear() throws IOException {
        ChemSocJapanIssueCrawler crawler = getCl2010_3();
        assertEquals("2010", crawler.getYear());
    }

    @Test
    public void testGetArticles() throws IOException {
        ChemSocJapanIssueCrawler crawler = getCl2010_3();
        List<Article> articles = crawler.getArticles();
        assertNotNull(articles);
        assertEquals(71, articles.size());

        Article a0 = articles.get(0);
        assertEquals("chemsocjapan/chem-lett/39/3/cl.2010.148", a0.getId());
        assertEquals(new Doi("10.1246/cl.2010.148"), a0.getDoi());

        Article a70 = articles.get(70);
        assertEquals("chemsocjapan/chem-lett/39/3/cl.2010.308", a70.getId());
        assertEquals(new Doi("10.1246/cl.2010.308"), a70.getDoi());
    }

    @Test
    public void testGetArticleTitles() throws IOException {
        ChemSocJapanIssueCrawler crawler = getCl2010_3();
        List<Article> articles = crawler.getArticles();
        assertEquals("Carotenoid Radicals: Cryptochemistry of Natural Colorants", articles.get(0).getTitle());
        assertEquals("Ratiometric Fluorescent Sensor for 2,4,6-Trinitrotoluene Designed Based on Energy Transfer between Size-different Quantum Dots", articles.get(1).getTitle());
    }

    @Test
    public void testGetArticleAuthors() throws IOException {
        ChemSocJapanIssueCrawler crawler = getCl2010_3();
        List<Article> articles = crawler.getArticles();
        assertEquals(Arrays.asList("Lowell D. Kispert", "Nikolay E. Polyakov"), articles.get(0).getAuthors());
        assertEquals(Arrays.asList("Tomohiro Shiraki", "Youichi Tsuchiya", "Seiji Shinkai"), articles.get(1).getAuthors());
    }

    @Test
    public void testGetPreviousIssue() throws IOException {
        ChemSocJapanIssueCrawler crawler = getCl2010_3();
        Issue prev = crawler.getPreviousIssue();
        assertNull(prev);
    }

    @Test
    public void testToIssue() throws IOException {
        ChemSocJapanIssueCrawler crawler = getCl2010_3();
        Issue issue = crawler.toIssue();
        assertEquals("chemsocjapan/chem-lett/39/3", issue.getId());
        assertEquals(URI.create("http://www.csj.jp/journals/chem-lett/cl-cont/cl2010-3.html"), issue.getUrl());
        assertEquals("2010", issue.getYear());
        assertEquals("39", issue.getVolume());
        assertEquals("3", issue.getNumber());
        assertNotNull(issue.getArticles());
        assertEquals(71, issue.getArticles().size());
        assertNull(issue.getPreviousIssue());
    }

}
