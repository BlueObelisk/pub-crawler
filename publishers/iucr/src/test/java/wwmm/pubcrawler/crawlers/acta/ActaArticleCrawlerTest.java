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
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerRequest;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import uk.ac.cam.ch.wwmm.httpcrawler.DefaultHttpFetcher;
import uk.ac.cam.ch.wwmm.httpcrawler.HttpFetcher;
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
public class ActaArticleCrawlerTest extends AbstractCrawlerTest {

    private CrawlerResponse prepareBt5401() throws IOException {
        return prepareResponse("./bt5401.html",
                URI.create("http://scripts.iucr.org/cgi-bin/paper?S1600536810045198"));
    }

    private CrawlerResponse prepareBt5401Bibtex() throws IOException {
        return prepareResponse("./bt5401.bibtex",
                URI.create("http://scripts.iucr.org/cgi-bin/biblio"));
    }

    private CrawlerResponse prepareBk5081() throws IOException {
        return prepareResponse("./bk5081.html",
                URI.create("http://scripts.iucr.org/cgi-bin/paper?S0108768109004066"));
    }

    private CrawlerResponse prepareBk5081Bibtex() throws IOException {
        return prepareResponse("./bk5081.bibtex",
                URI.create("http://scripts.iucr.org/cgi-bin/biblio"));
    }

    private CrawlerResponse prepareBk5081Cifs() throws IOException {
        return prepareResponse("./bk5081-cifs.html",
                URI.create("http://scripts.iucr.org/cgi-bin/sendcif?bk5081"));
    }

    private CrawlerResponse prepareBk5081Supp() throws IOException {
        return prepareResponse("./bk5081_supp.html",
                URI.create("http://scripts.iucr.org/cgi-bin/sendsup?cnor=bk5081&type=supplementarymaterials"));
    }

    protected ActaArticleCrawler getBt5401() throws IOException {
        Article article = new Article();
        article.setId(new ArticleId("acta/e/2010/12-00/bt5401"));
        article.setDoi(new Doi("10.1107/S1600536810045198"));

        CrawlerResponse response1 = prepareBt5401();
//        CrawlerResponse response2 = prepareBt5401Bibtex();

        HttpFetcher crawler = Mockito.mock(DefaultHttpFetcher.class);
        Mockito.when(crawler.execute(Mockito.any(CrawlerRequest.class)))
                .thenReturn(response1);

        CrawlerContext context = new CrawlerContext(null, crawler, null);
        return new ActaArticleCrawler(article, context);
    }

    protected ActaArticleCrawler getBk5801() throws IOException {
        Article article = new Article();
        article.setId(new ArticleId("acta/b/2009/02-00/bk5081"));
        article.setDoi(new Doi("10.1107/S0108768109004066"));

        CrawlerResponse response1 = prepareBk5081();
//        CrawlerResponse response2 = prepareBk5081Bibtex();
        CrawlerResponse response3 = prepareBk5081Cifs();
        CrawlerResponse response4 = prepareBk5081Supp();

        HttpFetcher crawler = Mockito.mock(DefaultHttpFetcher.class);
        Mockito.when(crawler.execute(Mockito.any(CrawlerRequest.class)))
                .thenReturn(response1, response3, response4);

        CrawlerContext context = new CrawlerContext(null, crawler, null);
        return new ActaArticleCrawler(article, context);
    }

    @Test
    public void testGetArticleId() throws IOException {
        ActaArticleCrawler crawler = getBt5401();
        assertEquals("acta/e/2010/12-00/bt5401", crawler.getArticleId().getUid());
    }

    @Test
    public void testGetTitleHtmlString() throws IOException {
        ActaArticleCrawler crawler = getBt5401();
        assertEquals("<h1 xmlns=\"http://www.w3.org/1999/xhtml\">K<sub>2</sub>LaCl<sub>5</sub></h1>", crawler.getTitleHtmlString());
    }


    @Test
    public void testGetReference() throws IOException {
        ActaArticleCrawler crawler = getBt5401();
        Reference ref = crawler.getReference();
        assertNotNull(ref);
        assertEquals("Acta Crystallographica Section E", ref.getJournalTitle());
        assertEquals("66", ref.getVolume());
        assertEquals("12", ref.getNumber());
        assertEquals("2010", ref.getYear());
        assertEquals("i78", ref.getPages());
    }


    @Test
    public void testGetAuthors() throws IOException {
        ActaArticleCrawler crawler = getBt5401();
        List<String> authors = crawler.getAuthors();
        assertNotNull(authors);
        assertEquals(3, authors.size());
        assertEquals("C. M.  Schurz", authors.get(0));
        assertEquals("T.  Schleid", authors.get(1));
        assertEquals("G.  Meyer", authors.get(2));
    }

    @Test
    public void testGetSupplementaryResources() throws IOException {
        ActaArticleCrawler crawler = getBt5401();
        List<SupplementaryResource> resources = crawler.getSupplementaryResources();
        assertNotNull(resources);
        assertEquals(4, resources.size());

        SupplementaryResource r0 = resources.get(0);
        assertEquals(URI.create("http://scripts.iucr.org/cgi-bin/sendcif?bt5401sup1"), r0.getUrl());
        assertEquals("cif file", r0.getLinkText());
        assertEquals("bt5401sup1.cif", r0.getFilePath());

//        SupplementaryResource r1 = resources.get(1);
//        assertEquals(URI.create("http://scripts.iucr.org/cgi-bin/sendcif?bt5401sup1&Qmime=cif"), r1.getUrl());
//        assertEquals("3d view", r1.getLinkText());

        SupplementaryResource r1 = resources.get(1);
        assertEquals(URI.create("http://journals.iucr.org/e/issues/2010/12/00/bt5401/bt5401Isup2.hkl"), r1.getUrl());
        assertEquals("structure factors", r1.getLinkText());
        assertEquals("bt5401Isup2.hkl", r1.getFilePath());

        SupplementaryResource r2 = resources.get(2);
        assertEquals(URI.create("http://journals.iucr.org/e/issues/2010/12/00/bt5401/bt5401sup0.html"), r2.getUrl());
        assertEquals("supplementary materials", r2.getLinkText());
        assertEquals("bt5401sup0.html", r2.getFilePath());

        SupplementaryResource r3 = resources.get(3);
        assertEquals(URI.create("http://scripts.iucr.org/cgi-bin/paper?cnor=bt5401&checkcif=yes"), r3.getUrl());
        assertEquals("CIF check report", r3.getLinkText());
        assertEquals("bt5401_checkcif.html", r3.getFilePath());
    }

    @Test
    public void testGetFullTextResources() throws IOException {
        ActaArticleCrawler crawler = getBt5401();
        List<FullTextResource> resources = crawler.getFullTextResources();
        assertNotNull(resources);
        assertEquals(2, resources.size());

        FullTextResource r0 = resources.get(0);
        assertEquals(URI.create("http://journals.iucr.org/e/issues/2010/12/00/bt5401/index.html"), r0.getUrl());
        assertEquals("HTML version", r0.getLinkText());

        FullTextResource r1 = resources.get(1);
        assertEquals(URI.create("http://journals.iucr.org/e/issues/2010/12/00/bt5401/bt5401.pdf"), r1.getUrl());
        assertEquals("pdf version", r1.getLinkText());
    }


    @Test
    public void testGetSupplementaryResourcesMultiCif() throws IOException {
        ActaArticleCrawler crawler = getBk5801();
        List<SupplementaryResource> resources = crawler.getSupplementaryResources();
        assertNotNull(resources);
        assertEquals(5, resources.size());

        SupplementaryResource r0 = resources.get(0);
        assertEquals(URI.create("http://scripts.iucr.org/cgi-bin/sendcif?bk5081sup1"), r0.getUrl());
//        assertEquals("cif for data block bk5081sup1.cif", r0.getLinkText());
        assertEquals("Experimentally determined crystal structures", r0.getContentType());
        assertEquals("bk5081sup1.cif", r0.getFilePath());

        SupplementaryResource r1 = resources.get(1);
        assertEquals(URI.create("http://scripts.iucr.org/cgi-bin/sendcif?bk5081sup2"), r1.getUrl());
//        assertEquals("cif for data block bk5081sup2.cif", r1.getLinkText());
        assertEquals("Predicted crystal structures from each participant, for each target", r1.getContentType());
        assertEquals("bk5081sup2.cif", r1.getFilePath());

        SupplementaryResource r2 = resources.get(2);
        assertEquals(URI.create("http://scripts.iucr.org/cgi-bin/sendsupfiles?bk5081&file=bk5081sup3.pdf&mime=application/pdf"), r2.getUrl());
        assertEquals("Discussion of results and methodology", r2.getLinkText());

        SupplementaryResource r3 = resources.get(3);
        assertEquals(URI.create("http://scripts.iucr.org/cgi-bin/sendsupfiles?bk5081&file=bk5081sup4.pdf&mime=application/pdf"), r3.getUrl());
        assertEquals("Supplementary material", r3.getLinkText());

        SupplementaryResource r4 = resources.get(4);
        assertEquals(URI.create("http://scripts.iucr.org/cgi-bin/sendsupfiles?bk5081&file=bk5081sup5.pdf&mime=application/pdf"), r4.getUrl());
        assertEquals("Pdf of cif of predicted crystal structures", r4.getLinkText());
    }


    @Test
    public void testIsOpenAccessTrue() throws IOException {
        ActaArticleCrawler crawler = getBt5401();
        assertTrue(crawler.isOpenAccess());
    }

}
