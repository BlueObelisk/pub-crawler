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

import ch.unibe.jexample.Given;
import ch.unibe.jexample.Injection;
import ch.unibe.jexample.InjectionPolicy;
import ch.unibe.jexample.JExample;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerRequest;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import uk.ac.cam.ch.wwmm.httpcrawler.DefaultHttpFetcher;
import uk.ac.cam.ch.wwmm.httpcrawler.HttpFetcher;
import wwmm.pubcrawler.CrawlerContext;
import wwmm.pubcrawler.crawlers.AbstractCrawlerTest;
import wwmm.pubcrawler.journals.ActaInfo;
import wwmm.pubcrawler.model.Article;
import wwmm.pubcrawler.model.Issue;
import wwmm.pubcrawler.model.id.ArticleId;
import wwmm.pubcrawler.model.id.IssueId;
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
@RunWith(JExample.class)
@Injection(InjectionPolicy.NONE)
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

    private CrawlerResponse prepareActaE2011_03Head() throws IOException {
        return prepareResponse("./e-2011-03-head.html",
                URI.create("http://journals.iucr.org/3/issues/2011/03/00/isscontshdr.html"));
    }

    private CrawlerResponse prepareActaE2011_03Body() throws IOException {
        return prepareResponse("./e-2011-03-body.html",
                URI.create("http://journals.iucr.org/3/issues/2011/03/00/isscontsbdy.html"));
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


    private CrawlerResponse prepareActaC1997_03Body() throws IOException {
        return prepareResponse("./c-1997-03-body.html",
                URI.create("http://journals.iucr.org/c/issues/1997/03/00/isscontsbdy.html"));
    }

    private CrawlerResponse prepareActaC1997_03Head() throws IOException {
        return prepareResponse("./c-1997-03-head.html",
                URI.create("http://journals.iucr.org/c/issues/1997/03/00/isscontshead.html"));
    }


    private CrawlerResponse prepareActaA2009_06Body() throws IOException {
        return prepareResponse("./a-2009-06-body.html",
                URI.create("http://journals.iucr.org/a/issues/2009/06/00/isscontsbdy.html"));
    }

    private CrawlerResponse prepareActaA2009_06Head() throws IOException {
        return prepareResponse("./a-2009-06-head.html",
                URI.create("http://journals.iucr.org/a/issues/2009/06/00/isscontshead.html"));
    }


    private CrawlerResponse prepareActaC1991_10Body() throws IOException {
        return prepareResponse("./c-1991-10-body.html",
                URI.create("http://journals.iucr.org/c/issues/1991/10/00/isscontsbdy.html"));
    }

    private CrawlerResponse prepareActaC1991_10Head() throws IOException {
        return prepareResponse("./c-1991-10-head.html",
                URI.create("http://journals.iucr.org/c/issues/1991/10/00/isscontshead.html"));
    }


    protected ActaIssueCrawler getActaC1997_03() throws IOException {
        Issue issue = new Issue();
        issue.setId(new IssueId("acta/c/1997/03-00"));
        issue.setUrl(URI.create("http://journals.iucr.org/c/issues/1997/03/00/isscontsbdy.html"));

        CrawlerResponse response1 = prepareActaC1997_03Body();
        CrawlerResponse response2 = prepareActaC1997_03Head();

        HttpFetcher crawler = Mockito.mock(DefaultHttpFetcher.class);
        Mockito.when(crawler.execute(Mockito.any(CrawlerRequest.class)))
                .thenReturn(response1, response2);

        CrawlerContext context = new CrawlerContext(null, crawler, null);
        return new ActaIssueCrawler(issue, ActaInfo.C, context);
    }

    protected ActaIssueCrawler getActaA2009_06() throws IOException {
        Issue issue = new Issue();
        issue.setId(new IssueId("acta/a/2009/06-00"));
        issue.setUrl(URI.create("http://journals.iucr.org/a/issues/2009/06/00/isscontsbdy.html"));

        CrawlerResponse response1 = prepareActaA2009_06Body();
        CrawlerResponse response2 = prepareActaA2009_06Head();

        HttpFetcher crawler = Mockito.mock(DefaultHttpFetcher.class);
        Mockito.when(crawler.execute(Mockito.any(CrawlerRequest.class)))
                .thenReturn(response1, response2);

        CrawlerContext context = new CrawlerContext(null, crawler, null);
        return new ActaIssueCrawler(issue, ActaInfo.A, context);
    }

    protected ActaIssueCrawler getActaE2004_11() throws IOException {
        Issue issue = new Issue();
        issue.setId(new IssueId("acta/e/2004/11-00"));
        issue.setUrl(URI.create("http://journals.iucr.org/e/issues/2004/11/00/isscontsbdy.html"));

        CrawlerResponse response1 = prepareActaE2004_11Body();
        CrawlerResponse response2 = prepareActaE2004_11Head();

        HttpFetcher crawler = Mockito.mock(DefaultHttpFetcher.class);
        Mockito.when(crawler.execute(Mockito.any(CrawlerRequest.class)))
                .thenReturn(response1, response2);

        CrawlerContext context = new CrawlerContext(null, crawler, null);
        return new ActaIssueCrawler(issue, ActaInfo.E, context);
    }

    protected ActaIssueCrawler getActaE2011_03() throws IOException {
        Issue issue = new Issue();
        issue.setId(new IssueId("acta/e/2011/03-00"));
        issue.setUrl(URI.create("http://journals.iucr.org/e/issues/2011/03/00/isscontsbdy.html"));

        CrawlerResponse response1 = prepareActaE2011_03Body();
        CrawlerResponse response2 = prepareActaE2011_03Head();

        HttpFetcher crawler = Mockito.mock(DefaultHttpFetcher.class);
        Mockito.when(crawler.execute(Mockito.any(CrawlerRequest.class)))
                .thenReturn(response1, response2);

        CrawlerContext context = new CrawlerContext(null, crawler, null);
        return new ActaIssueCrawler(issue, ActaInfo.E, context);
    }


    protected ActaIssueCrawler getActaB1997_01() throws IOException {
        Issue issue = new Issue();
        issue.setId(new IssueId("acta/b/1997/01-00"));
        issue.setUrl(URI.create("http://journals.iucr.org/b/issues/1997/01/00/isscontsbdy.html"));

        CrawlerResponse response1 = prepareActaB1997_01Body();
        CrawlerResponse response2 = prepareActaB1997_01Head();

        HttpFetcher crawler = Mockito.mock(DefaultHttpFetcher.class);
        Mockito.when(crawler.execute(Mockito.any(CrawlerRequest.class)))
                .thenReturn(response1, response2);

        CrawlerContext context = new CrawlerContext(null, crawler, null);
        return new ActaIssueCrawler(issue, ActaInfo.B, context);
    }

    protected ActaIssueCrawler getActaB2010_01() throws IOException {
        Issue issue = new Issue();
        issue.setId(new IssueId("acta/b/2010/01-00"));
        issue.setUrl(URI.create("http://journals.iucr.org/b/issues/2010/01/00/isscontsbdy.html"));

        CrawlerResponse response1 = prepareActaB2010_01Body();
        CrawlerResponse response2 = prepareActaB2010_01Head();

        HttpFetcher crawler = Mockito.mock(DefaultHttpFetcher.class);
        Mockito.when(crawler.execute(Mockito.any(CrawlerRequest.class)))
                .thenReturn(response1, response2);

        CrawlerContext context = new CrawlerContext(null, crawler, null);
        return new ActaIssueCrawler(issue, ActaInfo.B, context);
    }

    protected ActaIssueCrawler getActaA2010_06() throws IOException {
        Issue issue = new Issue();
        issue.setId(new IssueId("acta/a/2010/06-00"));
        issue.setUrl(URI.create("http://journals.iucr.org/a/issues/2010/06/00/isscontsbdy.html"));

        CrawlerResponse response1 = prepareActaA2010_06Body();
        CrawlerResponse response2 = prepareActaA2010_06Head();

        HttpFetcher crawler = Mockito.mock(DefaultHttpFetcher.class);
        Mockito.when(crawler.execute(Mockito.any(CrawlerRequest.class)))
                .thenReturn(response1, response2);

        CrawlerContext context = new CrawlerContext(null, crawler, null);
        return new ActaIssueCrawler(issue, ActaInfo.A, context);
    }


    protected ActaIssueCrawler getActaC2005_10() throws IOException {
        Issue issue = new Issue();
        issue.setId(new IssueId("acta/c/2005/10-00"));
        issue.setUrl(URI.create("http://journals.iucr.org/c/issues/2005/10/00/isscontsbdy.html"));

        CrawlerResponse response1 = prepareActaC2005_10Body();
        CrawlerResponse response2 = prepareActaC2005_10Head();

        HttpFetcher crawler = Mockito.mock(DefaultHttpFetcher.class);
        Mockito.when(crawler.execute(Mockito.any(CrawlerRequest.class)))
                .thenReturn(response1, response2);

        CrawlerContext context = new CrawlerContext(null, crawler, null);
        return new ActaIssueCrawler(issue, ActaInfo.C, context);
    }


    protected ActaIssueCrawler getActaC1991_10() throws IOException {
        Issue issue = new Issue();
        issue.setId(new IssueId("acta/c/1991/10-00"));
        issue.setUrl(URI.create("http://journals.iucr.org/c/issues/1991/10/00/isscontsbdy.html"));

        CrawlerResponse response1 = prepareActaC1991_10Body();
        CrawlerResponse response2 = prepareActaC1991_10Head();

        HttpFetcher crawler = Mockito.mock(DefaultHttpFetcher.class);
        Mockito.when(crawler.execute(Mockito.any(CrawlerRequest.class)))
                .thenReturn(response1, response2);

        CrawlerContext context = new CrawlerContext(null, crawler, null);
        return new ActaIssueCrawler(issue, ActaInfo.C, context);
    }


    @Test
    public ActaIssueCrawler testCrawlA201006() throws IOException {
        ActaIssueCrawler crawler = getActaA2010_06();
        return crawler;
    }

    @Test
    public ActaIssueCrawler testCrawlB201001() throws IOException {
        ActaIssueCrawler crawler = getActaB2010_01();
        return crawler;
    }

    @Test
    @Given("#testCrawlA201006")
    public void testPageWithVolumeInTitle(ActaIssueCrawler crawler) {
        assertEquals("66", crawler.getVolume());
    }


    @Test
    @Given("#testCrawlB201001")
    public void testGetIssueId(ActaIssueCrawler crawler) throws IOException {
        assertEquals("acta/b/2010/01-00", crawler.getIssueId().getValue());
    }

    @Test
    @Given("#testCrawlA201006")
    public void testGetVolume(ActaIssueCrawler crawler) throws IOException {
        assertEquals("66", crawler.getVolume());
    }

    @Test
    @Given("#testCrawlB201001")
    public void testGetNumber(ActaIssueCrawler crawler) throws IOException {
        assertEquals("1", crawler.getNumber());
    }

    @Test
    @Given("#testCrawlB201001")
    public void testGetYear(ActaIssueCrawler crawler) throws IOException {
        assertEquals("2010", crawler.getYear());
    }

    @Test
    @Given("#testCrawlB201001")
    public List<Article> testGetArticles(ActaIssueCrawler crawler) throws IOException {
        List<Article> articles = crawler.getArticles();
        assertNotNull(articles);
        assertEquals(13, articles.size());
        return articles;
    }

    @Test
    @Given("#testGetArticles")
    public void testGetArticleIds(List<Article> articles) {
        Article a0 = articles.get(0);
        assertEquals("acta/b/2010/01-00/bk5091", a0.getId().getValue());

        Article a7 = articles.get(7);
        assertEquals("acta/b/2010/01-00/zb5008", a7.getId().getValue());

        Article a12 = articles.get(12);
        assertEquals("acta/b/2010/01-00/me0395", a12.getId().getValue());
    }

    @Test
    @Given("#testGetArticles")
    public void testGetArticleDois(List<Article> articles) {
        Article a0 = articles.get(0);
        assertEquals(new Doi("10.1107/S0108768109053981"), a0.getDoi());

        Article a7 = articles.get(7);
        assertEquals(new Doi("10.1107/S0108768109048769"), a7.getDoi());

        Article a12 = articles.get(12);
        assertEquals(new Doi("10.1107/S0108768109047855"), a12.getDoi());
    }

    @Test
    @Given("#testGetArticles")
    public void testGetArticleReferences(List<Article> articles) {
        Article a0 = articles.get(0);
        assertEquals("1-16", a0.getReference().getPages());

        Article a7 = articles.get(7);
        assertEquals("69-75", a7.getReference().getPages());

        Article a12 = articles.get(12);
        assertEquals("104-108", a12.getReference().getPages());
    }


    @Test
    @Given("#testCrawlB201001")
    public void testArticleTitles(ActaIssueCrawler crawler) throws IOException {
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
    @Given("#testCrawlB201001")
    public void testArticleAuthors(ActaIssueCrawler crawler) throws IOException {
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
    @Given("#testCrawlB201001")
    public void testArticleSuppInfo(ActaIssueCrawler crawler) throws IOException {
        List<Article> articles = crawler.getArticles();
        assertEquals(3, articles.get(0).getSupplementaryResources().size());
    }

    @Test
    @Given("#testCrawlB201001")
    public void testGetPreviousIssue(ActaIssueCrawler crawler) throws IOException {
        Issue prev = crawler.getPreviousIssue();
        assertNotNull(prev);
        assertEquals("acta/b/2009/06-00", prev.getId().getValue());
        assertEquals(URI.create("http://journals.iucr.org/b/issues/2009/06/00/isscontsbdy.html"), prev.getUrl());
    }

    @Test
    @Given("#testCrawlB201001")
    public void testToIssue(ActaIssueCrawler crawler) throws IOException {
        Issue issue = crawler.toIssue();
        assertEquals("acta/b/2010/01-00", issue.getId().getValue());
        assertEquals(URI.create("http://journals.iucr.org/b/issues/2010/01/00/isscontsbdy.html"), issue.getUrl());
        assertEquals("2010", issue.getYear());
        assertEquals("66", issue.getVolume());
        assertEquals("1", issue.getNumber());
        assertNotNull(issue.getArticles());
        assertEquals(13, issue.getArticles().size());
        assertNotNull(issue.getPreviousIssue());
        assertEquals("acta/b/2009/06-00", issue.getPreviousIssue().getId().getValue());
    }

    @Test
    public ActaIssueCrawler testCrawlE200411() throws IOException {
        ActaIssueCrawler crawler = getActaE2004_11();
        return crawler;
    }

    @Test
    @Given("#testCrawlE200411")
    public void testGetOldFormatIssueId(ActaIssueCrawler crawler) throws IOException {
        assertEquals("acta/e/2004/11-00", crawler.getIssueId().getValue());
    }

    @Test
    @Given("#testCrawlE200411")
    public void testGetOldFormatVolume(ActaIssueCrawler crawler) throws IOException {
        assertEquals("60", crawler.getVolume());
    }

    @Test
    @Given("#testCrawlE200411")
    public void testGetOldFormatNumber(ActaIssueCrawler crawler) throws IOException {
        assertEquals("11", crawler.getNumber());
    }

    @Test
    @Given("#testCrawlE200411")
    public void testGetOldFormatArticles(ActaIssueCrawler crawler) throws IOException {
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
        assertEquals(0, articles.get(0).getSupplementaryResources().size());
        assertEquals(2, articles.get(1).getSupplementaryResources().size());
        assertEquals(1, articles.get(2).getSupplementaryResources().size());
        assertEquals(1, articles.get(3).getSupplementaryResources().size());
        assertEquals(0, articles.get(4).getSupplementaryResources().size());
        assertEquals(0, articles.get(5).getSupplementaryResources().size());
        assertEquals(1, articles.get(6).getSupplementaryResources().size());
        assertEquals(1, articles.get(7).getSupplementaryResources().size());
        assertEquals(1, articles.get(8).getSupplementaryResources().size());
        assertEquals(0, articles.get(9).getSupplementaryResources().size());
        assertEquals(1, articles.get(10).getSupplementaryResources().size());
        assertEquals(1, articles.get(11).getSupplementaryResources().size());
        assertEquals(0, articles.get(12).getSupplementaryResources().size());
        assertEquals(1, articles.get(13).getSupplementaryResources().size());
        assertEquals(1, articles.get(14).getSupplementaryResources().size());
        assertEquals(0, articles.get(15).getSupplementaryResources().size());
        assertEquals(2, articles.get(16).getSupplementaryResources().size());
        assertEquals(0, articles.get(17).getSupplementaryResources().size());
        assertEquals(1, articles.get(18).getSupplementaryResources().size());
        assertEquals(1, articles.get(19).getSupplementaryResources().size());
        assertEquals(0, articles.get(20).getSupplementaryResources().size());
    }


    @Test
    public ActaIssueCrawler testCrawlE201103() throws IOException {
        ActaIssueCrawler crawler = getActaE2011_03();
        return crawler;
    }

    @Test
    @Given("#testCrawlE201103")
    public List<Article> testGetE201103Articles(ActaIssueCrawler crawler) {
        List<Article> articles = crawler.getArticles();
        assertNotNull(articles);
        assertEquals(257, articles.size());
        return articles;
    }

    @Test
    @Given("#testGetE201103Articles")
    public void testGetE201103ArticleTitles(List<Article> articles) {
        assertEquals("Vanadium(V) oxide arsenate(V), VOAsO4", articles.get(0).getTitle());
        assertEquals("Redetermination of AgPO3", articles.get(1).getTitle());
    }

    @Test
    @Given("#testGetE201103Articles")
    public void testGetE201103ArticleAuthors(List<Article> articles) {
        assertEquals(Arrays.asList("S. Ezzine Yahmed", "M. F. Zid", "A. Driss"), articles.get(0).getAuthors());
        assertEquals(Arrays.asList("K. V. Terebilenko", "I. V. Zatovsky", "I. V. Ogorodnyk", "V. N. Baumer", "N. S. Slobodyanik"), articles.get(1).getAuthors());
    }

    @Test
    @Given("#testGetE201103Articles")
    public void testGetE201103ArticleDois(List<Article> articles) {
        assertEquals(new Doi("10.1107/S1600536811004053"), articles.get(0).getDoi());
        assertEquals(new Doi("10.1107/S1600536811003977"), articles.get(1).getDoi());
    }


    @Test
    public ActaIssueCrawler testCrawlC199703() throws IOException {
        ActaIssueCrawler crawler = getActaC1997_03();
        return crawler;
    }

    @Test
    @Given("#testCrawlC199703")
    public void testGetC199703Volume(ActaIssueCrawler crawler) {
        assertEquals("53", crawler.getVolume());
    }

    @Test
    @Given("#testCrawlC199703")
    public void testGetC199703Number(ActaIssueCrawler crawler) {
        assertEquals("3", crawler.getNumber());
    }

    @Test
    @Given("#testCrawlC199703")
    public void testGetC199703Year(ActaIssueCrawler crawler) {
        assertEquals("1997", crawler.getYear());
    }

    @Test
    @Given("#testCrawlC199703")
    public void testGetC199703Articles(ActaIssueCrawler crawler) {
        List<Article> articles = crawler.getArticles();
        assertNotNull(articles);
        assertEquals(58, articles.size());
    }

    @Test
    public ActaIssueCrawler testCrawlA200906() throws IOException {
        ActaIssueCrawler crawler = getActaA2009_06();
        return crawler;
    }

    @Test
    @Given("#testCrawlA200906")
    public void testGetA200906Volume(ActaIssueCrawler crawler) {
        assertEquals("65", crawler.getVolume());
    }

    @Test
    @Given("#testCrawlA200906")
    public void testGetA200906Number(ActaIssueCrawler crawler) {
        assertEquals("6", crawler.getNumber());
    }

    @Test
    @Given("#testCrawlA200906")
    public void testGetA200906Year(ActaIssueCrawler crawler) {
        assertEquals("2009", crawler.getYear());
    }

    @Test
    @Given("#testCrawlA200906")
    public void testGetA200906Articles(ActaIssueCrawler crawler) {
        List<Article> articles = crawler.getArticles();
        assertNotNull(articles);
        assertEquals(11, articles.size());

        assertEquals("Foundations of Crystallography with Computer Applications", articles.get(8).getTitle());
    }


    @Test
    public ActaIssueCrawler testCrawlC199110() throws IOException {
        ActaIssueCrawler crawler = getActaC1991_10();
        return crawler;
    }

    @Test
    @Given("#testCrawlC199110")
    public List<Article> testGetC199110Articles(ActaIssueCrawler crawler) {
        List<Article> articles = crawler.getArticles();
        assertNotNull(articles);
        assertEquals(93, articles.size());
        return articles;
    }

    @Test
    @Given("#testGetC199110Articles")
    public void testGetC199101ArticleIds(List<Article> articles) {
        assertEquals(new ArticleId("acta/c/1991/10-00/du0272"), articles.get(0).getId());
    }

}
