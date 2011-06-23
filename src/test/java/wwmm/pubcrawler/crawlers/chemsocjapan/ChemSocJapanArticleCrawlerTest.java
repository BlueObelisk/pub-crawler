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
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerRequest;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import uk.ac.cam.ch.wwmm.httpcrawler.HttpCrawler;
import wwmm.pubcrawler.CrawlerContext;
import wwmm.pubcrawler.crawlers.AbstractCrawlerTest;
import wwmm.pubcrawler.model.Article;
import wwmm.pubcrawler.model.FullTextResource;
import wwmm.pubcrawler.model.Reference;
import wwmm.pubcrawler.model.SupplementaryResource;
import wwmm.pubcrawler.model.id.ArticleId;
import wwmm.pubcrawler.types.Doi;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Sam Adams
 */
public class ChemSocJapanArticleCrawlerTest extends AbstractCrawlerTest {

    private CrawlerResponse prepareCl2010_156() throws IOException {
        return prepareResponse("./cl-2010_156.html",
                URI.create("http://www.jstage.jst.go.jp/article/cl/39/3/39_156/_article"));
    }

    private CrawlerResponse prepareCl2010_156_supp() throws IOException {
        return prepareResponse("./cl-2010_156_supp.html",
                URI.create("http://www.jstage.jst.go.jp/article/cl/39/3/39_156/_applist"));
    }

    private CrawlerResponse prepareCl2010_156_bibtex() throws IOException {
        return prepareResponse("./cl-2010_156.bibtex",
                URI.create("http://www.jstage.jst.go.jp/download/cl/39/3/39_156/_bib/"));
    }

    private CrawlerResponse prepareCl2008_682() throws IOException {
        return prepareResponse("./cl-2008_682.html",
                URI.create("http://www.jstage.jst.go.jp/article/cl/37/7/37_682/_article"));
    }

    private CrawlerResponse prepareCl2008_682_supp() throws IOException {
        return prepareResponse("./cl-2008_682_supp.html",
                URI.create("http://www.jstage.jst.go.jp/article/cl/37/7/37_682/_applist"));
    }

    private CrawlerResponse prepareCl2008_682_bibtex() throws IOException {
        return prepareResponse("./cl-2008_682.bibtex",
                URI.create("http://www.jstage.jst.go.jp/download/cl/37/7/37_682/_bib/"));
    }

    protected ChemSocJapanArticleCrawler getCl2010_156() throws IOException {
        Article article = new Article();
        article.setId(new ArticleId("chemsocjapan/cl/39/3/cl.2010.156"));
        article.setDoi(new Doi("10.1246/cl.2010.156"));

        CrawlerResponse response1 = prepareCl2010_156();
        CrawlerResponse response2 = prepareCl2010_156_bibtex();
        CrawlerResponse response3 = prepareCl2010_156_supp();

        HttpCrawler crawler = Mockito.mock(HttpCrawler.class);
        Mockito.when(crawler.execute(Mockito.any(CrawlerRequest.class)))
                .thenReturn(response1, response2, response3);

        CrawlerContext context = new CrawlerContext(null, crawler, null);
        return new ChemSocJapanArticleCrawler(article, context);
    }

    protected ChemSocJapanArticleCrawler getCl2008_682() throws IOException {
        Article article = new Article();
        article.setId(new ArticleId("chemsocjapan/cl/37/7/cl.2008.682"));
        article.setDoi(new Doi("10.1246/cl.2008.682"));

        CrawlerResponse response1 = prepareCl2008_682();
        CrawlerResponse response2 = prepareCl2008_682_bibtex();
        CrawlerResponse response3 = prepareCl2008_682_supp();

        HttpCrawler crawler = Mockito.mock(HttpCrawler.class);
        Mockito.when(crawler.execute(Mockito.any(CrawlerRequest.class)))
                .thenReturn(response1, response2, response3);

        CrawlerContext context = new CrawlerContext(null, crawler, null);
        return new ChemSocJapanArticleCrawler(article, context);
    }


    @Test
    public void testGetArticleId() throws IOException {
        ChemSocJapanArticleCrawler crawler = getCl2010_156();
        assertEquals("chemsocjapan/cl/39/3/cl.2010.156", crawler.getArticleId().getValue());
    }

    @Test
    public void testGetTitleHtmlString() throws IOException {
        ChemSocJapanArticleCrawler crawler = getCl2010_156();
        assertEquals("<h1 xmlns=\"http://www.w3.org/1999/xhtml\">Ratiometric Fluorescent Sensor for "
                +"2,4,6-Trinitrotoluene Designed Based on Energy Transfer between Size-different Quantum "
                +"Dots</h1>", crawler.getTitleHtmlString());
    }

    @Test
    public void testGetAbstractHtmlString() throws IOException {
        ChemSocJapanArticleCrawler crawler = getCl2010_156();
        assertEquals("<p xmlns=\"http://www.w3.org/1999/xhtml\">Aggregation of "
                +"two different sizes of semiconductor quantum dots is "
                +"successfully applied to a novel ratiometric fluorescent "
                +"sensor, in which F\u00F6rster type energy transfer between "
                +"quantum dots is induced by the interaction with 2,4,6-"
                +"trinitrotoluene (TNT) as a binder of quantum dots. The "
                +"detection limit for TNT achieved is 5 pM, and the system "
                +"shows relatively high sensitivity against TNT in comparison "
                +"to 2,4-dinitrotoluene and 2-nitrotoluene at 5 pM\u2013500 nM "
                +"range.</p>", crawler.getAbstractHtmlString());
    }


    @Test
    public void testGetReference() throws IOException {
        ChemSocJapanArticleCrawler crawler = getCl2010_156();
        Reference ref = crawler.getReference();
        assertNotNull(ref);
        assertEquals("Chemistry Letters", ref.getJournalTitle());
        assertEquals("39", ref.getVolume());
        assertEquals("3", ref.getNumber());
        assertEquals("2010", ref.getYear());
        assertEquals("156-158", ref.getPages());
    }


    @Test
    public void testGetAuthors() throws IOException {
        ChemSocJapanArticleCrawler crawler = getCl2010_156();
        List<String> authors = crawler.getAuthors();
        assertNotNull(authors);
        assertEquals(3, authors.size());
        assertEquals("Tomohiro Shiraki", authors.get(0));
        assertEquals("Youichi Tsuchiya", authors.get(1));
        assertEquals("Seiji Shinkai", authors.get(2));
    }

    @Test
    public void testGetSupplementaryResources() throws IOException {
        ChemSocJapanArticleCrawler crawler = getCl2010_156();
        List<SupplementaryResource> resources = crawler.getSupplementaryResources();
        assertNotNull(resources);
        assertEquals(1, resources.size());

        SupplementaryResource r0 = resources.get(0);
        assertEquals(URI.create("http://www.jstage.jst.go.jp/article/cl/39/3/39_156/_appendix/1"), r0.getUrl());
        assertEquals("Supporting Information", r0.getLinkText());
        assertEquals("1", r0.getFilePath());
    }

    @Test
    public void testGetFullTextResources() throws Exception {
        ChemSocJapanArticleCrawler crawler = getCl2010_156();
        List<FullTextResource> resources = crawler.getFullTextResources();
        assertNotNull(resources);
        assertEquals(1, resources.size());

        FullTextResource r0 = resources.get(0);
        assertEquals(URI.create("http://www.jstage.jst.go.jp/article/cl/39/3/156/_pdf"), r0.getUrl());
        assertEquals("PDF (911K)", r0.getLinkText());
    }


    @Test
    public void testGetArticleId_Cl2008_682() throws IOException {
        ChemSocJapanArticleCrawler crawler = getCl2008_682();
        assertEquals("chemsocjapan/cl/37/7/cl.2008.682", crawler.getArticleId().getValue());
    }

    @Test
    public void testGetTitleHtmlString_Cl2008_682() throws IOException {
        ChemSocJapanArticleCrawler crawler = getCl2008_682();
        assertEquals("<h1 xmlns=\"http://www.w3.org/1999/xhtml\">Alternative "
                +"Route to Metal Halide Free Ionic Liquids</h1>",
                crawler.getTitleHtmlString());
    }

    @Test
    public void testGetAbstractHtmlString_Cl2008_682() throws IOException {
        ChemSocJapanArticleCrawler crawler = getCl2008_682();
        assertEquals("<p xmlns=\"http://www.w3.org/1999/xhtml\">An alternative "
                +"synthetic route to metal halide free ionic liquids using "
                +"trialkyloxonium salt is proposed. Utility of this synthetic "
                +"route has been demonstrated by preparing 1-ethyl-3-"
                +"methylimidazolium tetrafluoroborate ionic liquid through the "
                +"reaction between 1-methylimidazole and triethyloxonium "
                +"tetrafluoroborate in anhydrous ether.</p>",
                crawler.getAbstractHtmlString());
    }


    @Test
    public void testGetReference_Cl2008_682() throws IOException {
        ChemSocJapanArticleCrawler crawler = getCl2008_682();
        Reference ref = crawler.getReference();
        assertNotNull(ref);
        assertEquals("Chemistry Letters", ref.getJournalTitle());
        assertEquals("37", ref.getVolume());
        assertEquals("7", ref.getNumber());
        assertEquals("2008", ref.getYear());
        assertEquals("682-683", ref.getPages());
    }


    @Test
    public void testGetAuthors_Cl2008_682() throws IOException {
        ChemSocJapanArticleCrawler crawler = getCl2008_682();
        List<String> authors = crawler.getAuthors();
        assertNotNull(authors);
        assertEquals(2, authors.size());
        assertEquals("Koichiro Takao", authors.get(0));
        assertEquals("Yasuhisa Ikeda", authors.get(1));
    }

    @Test
    public void testGetSupplementaryResources_Cl2008_682() throws IOException {
        ChemSocJapanArticleCrawler crawler = getCl2008_682();
        List<SupplementaryResource> resources = crawler.getSupplementaryResources();
        assertNotNull(resources);
        assertEquals(2, resources.size());

        SupplementaryResource r0 = resources.get(0);
        assertEquals(URI.create("http://www.jstage.jst.go.jp/article/cl/37/7/37_682/_appendix/1"), r0.getUrl());
        assertEquals("Supporting Information", r0.getLinkText());
        assertEquals("1", r0.getFilePath());

        SupplementaryResource r1 = resources.get(1);
        assertEquals(URI.create("http://www.jstage.jst.go.jp/article/cl/37/7/37_682/_appendix/2"), r1.getUrl());
        assertEquals("Crystallographic Information File (CIF)", r1.getLinkText());
        assertEquals("2", r1.getFilePath());

    }



    @Test
    public void testIsOpenAccessFalse_Cl2008_682() throws IOException {
        ChemSocJapanArticleCrawler crawler = getCl2008_682();
        assertFalse(crawler.isOpenAccess());
    }

}
