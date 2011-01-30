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
import wwmm.pubcrawler.httpcrawler.CrawlerRequest;
import wwmm.pubcrawler.httpcrawler.CrawlerResponse;
import wwmm.pubcrawler.httpcrawler.HttpCrawler;
import wwmm.pubcrawler.model.Article;
import wwmm.pubcrawler.model.Issue;
import wwmm.pubcrawler.model.SupplementaryResource;
import wwmm.pubcrawler.types.Doi;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Sam Adams
 */
public class ActaIssueCrawlerTest extends AbstractCrawlerTest {

    private CrawlerResponse prepareActaC2005_10Head() throws IOException {
        return prepareResponse("./c-2005-10-head.html",
                URI.create("http://journals.iucr.org/c/issues/2005/10/00/isscontshdr.html"));
    }

    private CrawlerResponse prepareActaC2005_10Body() throws IOException {
        return prepareResponse("./c-2005-10-body.html",
                URI.create("http://journals.iucr.org/c/issues/2005/10/00/isscontsbdy.html"));
    }

    private CrawlerResponse prepareActaB1997_01Head() throws IOException {
        return prepareResponse("./b-1997-01-head.html",
                URI.create("http://journals.iucr.org/b/issues/1997/01/00/isscontshdr.html"));
    }

    private CrawlerResponse prepareActaB1997_01Body() throws IOException {
        return prepareResponse("./b-1997-01-body.html",
                URI.create("http://journals.iucr.org/b/issues/1997/01/00/isscontsbdy.html"));
    }

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

    protected ActaIssueCrawler getActaB1997_01() throws IOException {
        Issue issue = new Issue();
        issue.setId("acta/b/1997/01-00");
        issue.setUrl(URI.create("http://journals.iucr.org/b/issues/1997/01/00/isscontsbdy.html"));

        CrawlerResponse response1 = prepareActaB1997_01Body();
        CrawlerResponse response2 = prepareActaB1997_01Head();

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


    protected ActaIssueCrawler getActaC2005_10() throws IOException {
        Issue issue = new Issue();
        issue.setId("acta/c/2005/10-00");
        issue.setUrl(URI.create("http://journals.iucr.org/c/issues/2005/10/00/isscontsbdy.html"));

        CrawlerResponse response1 = prepareActaC2005_10Body();
        CrawlerResponse response2 = prepareActaC2005_10Head();

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
    public void testArticleTitles() throws IOException {
        ActaIssueCrawler crawler = getActaB2010_01();
        List<Article> articles = crawler.getArticles();
        assertEquals("<h1>Polysomatic apatites</h1>", articles.get(0).getTitleHtml());
        assertEquals("<h1>A complicated quasicrystal approximant &#x3B5;<sub>16</sub> predicted by the strong-reflections approach</h1>", articles.get(1).getTitleHtml());
        assertEquals("<h1>Structures of incommensurate and commensurate composite crystals Rb<sub><i>x</i></sub>MnO<sub>2</sub> (<i>x</i> = 1.3711, 1.3636)</h1>", articles.get(2).getTitleHtml());
        assertEquals("<h1>Orientational disorder and phase transitions in crystals of dioxofluoromolybdate, (NH<sub>4</sub>)<sub>2</sub>MoO<sub>2</sub>F<sub>4</sub></h1>", articles.get(3).getTitleHtml());
        assertEquals("<h1>Octahedral tilting in cation-ordered Jahn-Teller distorted perovskites - a group-theoretical analysis</h1>", articles.get(4).getTitleHtml());
        assertEquals("<h1>Revision of the structure of Cs<sub>2</sub>CuSi<sub>5</sub>O<sub>12</sub> leucite as orthorhombic <i>Pbca</i></h1>", articles.get(5).getTitleHtml());
        assertEquals("<h1>Structure solution of the new titanate Li<sub>4</sub>Ti<sub>8</sub>Ni<sub>3</sub>O<sub>21</sub> using precession electron diffraction</h1>", articles.get(6).getTitleHtml());
        assertEquals("<h1>Concomitant polymorphic behavior of di-&#x3BC;-thiocyanato-&#x3BA;<sup>2</sup><i>N</i>:<i>S</i>;&#x3BA;<sup>2</sup><i>S</i>:<i>N</i>-bis[bis(tri-<i>p</i>-fluorophenylphosphine-&#x3BA;<i>P</i>)silver(I)]</h1>", articles.get(7).getTitleHtml());
        assertEquals("<h1><i>Ab initio</i> structure determination of phase II of racemic ibuprofen by X-ray powder diffraction</h1>", articles.get(8).getTitleHtml());
        assertEquals("<h1>Zur Kristallchemie von Graphen und Graphit</h1>", articles.get(9).getTitleHtml());
        assertEquals("<h1>Structures of dipeptides: the head-to-tail story</h1>", articles.get(10).getTitleHtml());
        assertEquals("<h1>A list of organic kryptoracemates</h1>", articles.get(11).getTitleHtml());
        assertEquals("<h1>Notes for authors 2010</h1>", articles.get(12).getTitleHtml());
    }

    @Test
    public void testArticleAuthors() throws IOException {
        ActaIssueCrawler crawler = getActaB2010_01();
        List<Article> articles = crawler.getArticles();
        assertEquals(Arrays.asList("T. Baikie", "S. S. Pramana", "C. Ferraris", "Y. Huang", "E. Kendrick", "K. Knight", "Z. Ahmad", "T. J. White"), articles.get(0).getAuthors());
        assertEquals(Arrays.asList("M. Li", "J. Sun", "P. Oleynikov", "S. Hovm\u00f6ller", "X. Zou", "B. Grushko"), articles.get(1).getAuthors());
        assertEquals(Arrays.asList("J. Nuss", "S. Pfeiffer", "S. van Smaalen", "M. Jansen"), articles.get(2).getAuthors());
        assertEquals(Arrays.asList("A. A. Udovenko", "A. D. Vasiliev", "N. M. Laptash"), articles.get(3).getAuthors());
        assertEquals(Arrays.asList("C. J. Howard", "M. A. Carpenter"), articles.get(4).getAuthors());
        assertEquals(Arrays.asList("A. M. T. Bell", "K. S. Knight", "C. M. B. Henderson", "A. N. Fitch"), articles.get(5).getAuthors());
        assertEquals(Arrays.asList("M. Gemmi", "H. Klein", "A. Rageau", "P. Strobel", "F. Le Cras"), articles.get(6).getAuthors());
        assertEquals(Arrays.asList("B. Omondi", "R. Meijboom"), articles.get(7).getAuthors());
        assertEquals(Arrays.asList("P. Derollez", "E. Dudognon", "F. Affouard", "F. Dan\u00e8de", "N. T. Correia", "M. Descamps"), articles.get(8).getAuthors());
        assertEquals(Arrays.asList("M. Tr\u00f6mel"), articles.get(9).getAuthors());
        assertEquals(Arrays.asList("C. H. G\u00f6rbitz"), articles.get(10).getAuthors());
        assertEquals(Arrays.asList("L. F\u00e1bi\u00e1n", "C. P. Brock"), articles.get(11).getAuthors());
        assertEquals(Arrays.asList(), articles.get(12).getAuthors());
    }

    @Test
    public void testArticleSuppInfo() throws IOException {
        ActaIssueCrawler crawler = getActaB2010_01();
        List<Article> articles = crawler.getArticles();
        assertEquals(3, articles.get(0).getSupplementaryResources().size());
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

    @Test
    public void testGetArticlesWithoutLink() throws IOException {
        ActaIssueCrawler crawler = getActaC2005_10();
        List<Article> articles = crawler.getArticles();
        assertNotNull(articles);
        assertEquals(21, articles.size());
    }

    @Test
    public void testReadActaB1997_01GetArticles() throws IOException {
        ActaIssueCrawler crawler = getActaB1997_01();
        List<Article> articles = crawler.getArticles();
        assertEquals(21, articles.size());
    }

    @Test
    public void testReadActaB1997_01GetArticleSuppInfo() throws IOException {
        ActaIssueCrawler crawler = getActaB1997_01();
        List<Article> articles = crawler.getArticles();
        assertEquals(2, articles.get(1).getSupplementaryResources().size());
    }

}
