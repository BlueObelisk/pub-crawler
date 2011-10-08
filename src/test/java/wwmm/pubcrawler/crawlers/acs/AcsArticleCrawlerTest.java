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

import nu.xom.ParsingException;
import org.apache.commons.io.input.NullInputStream;
import org.apache.http.Header;
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
import wwmm.pubcrawler.utils.BibtexTool;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Sam Adams
 */
public class AcsArticleCrawlerTest extends AbstractCrawlerTest {

    private CrawlerResponse prepareCg100078bResponse() throws IOException {
        return prepareResponse("./cg100078b.html",
                URI.create("http://pubs.acs.org/doi/abs/10.1021/cg100078b"));
    }

    private CrawlerResponse prepareCg100078bSuppResponse() throws IOException {
        return prepareResponse("./cg100078b_supp.html",
                URI.create("http://pubs.acs.org/doi/suppl/10.1021/cg100078b"));
    }

    private CrawlerResponse prepareCg100078bBibtexResponse() throws IOException {
        return prepareResponse("./cg100078b_bibtex.txt",
                URI.create("http://pubs.acs.org/doi/suppl/10.1021/cg100078b"));
    }

    private CrawlerResponse prepareJo1013564Response() throws IOException {
        return prepareResponse("./jo1013564.html",
                URI.create("http://pubs.acs.org/doi/abs/10.1021/jo1013564"));
    }

    private CrawlerResponse prepareJo1013564SuppResponse() throws IOException {
        return prepareResponse("./jo1013564_supp.html",
                URI.create("http://pubs.acs.org/doi/suppl/10.1021/jo1013564"));
    }

    private CrawlerResponse prepareJo1013564BibtexResponse() throws IOException {
        return prepareResponse("./jo1013564_bibtex.txt",
                URI.create("http://pubs.acs.org/doi/suppl/10.1021/jo1013564"));
    }

    private CrawlerResponse prepareBlankResponse() {
        return new CrawlerResponse(URI.create("http://example.com"), Collections.<Header>emptyList(), new NullInputStream(0), true, false);
    }

    protected AcsArticleCrawler getArticleCg100078b() throws IOException {
        Article article = new Article();
        article.setId(new ArticleId("acs/cgdefu/10/8/cg100078b"));
        article.setDoi(new Doi("10.1021/cg100078b"));

        CrawlerResponse response1 = prepareCg100078bResponse();
        CrawlerResponse response2 = prepareCg100078bSuppResponse();
        CrawlerResponse response3 = prepareCg100078bBibtexResponse();

        HttpCrawler crawler = Mockito.mock(HttpCrawler.class);
        Mockito.when(crawler.execute(Mockito.any(CrawlerRequest.class)))
                .thenReturn(response1, response2, prepareBlankResponse(), response3);

        CrawlerContext context = new CrawlerContext(null, crawler, null);
        return new AcsArticleCrawler(article, context);
    }

    protected AcsArticleCrawler getArticleJo1013564() throws IOException {
        Article article = new Article();
        article.setId(new ArticleId("acs/joceah/75/23/jo1013564"));
        article.setDoi(new Doi("10.1021/jo1013564"));

        CrawlerResponse response1 = prepareJo1013564Response();
        CrawlerResponse response2 = prepareJo1013564SuppResponse();
        CrawlerResponse response3 = prepareJo1013564BibtexResponse();

        HttpCrawler crawler = Mockito.mock(HttpCrawler.class);
        Mockito.when(crawler.execute(Mockito.any(CrawlerRequest.class)))
                .thenReturn(response1, response2, prepareBlankResponse(), response3);

        CrawlerContext context = new CrawlerContext(null, crawler, null);
        return new AcsArticleCrawler(article, context);
    }

    @Test
    public void testGetTitleHtml() throws IOException {
        AcsArticleCrawler article = getArticleCg100078b();
        assertEquals("<h1 xmlns=\"http://www.w3.org/1999/xhtml\">Microporous La(III) Metal\u2212Organic Framework Using a Semirigid Tricarboxylic Ligand: "
                +"Synthesis, Single-Crystal to Single-Crystal Sorption Properties, and Gas Adsorption Studies</h1>",
                article.getTitleHtmlString());
    }

    @Test
    public void testGetSupportingInfoUrl() throws IOException {
        AcsArticleCrawler article = getArticleCg100078b();
        assertEquals(URI.create("http://pubs.acs.org/doi/suppl/10.1021/cg100078b"), article.getSuppUrl());
    }

    @Test
    public void testGetSupplementaryResources() throws IOException {
        AcsArticleCrawler article = getArticleCg100078b();
        List<SupplementaryResource> resources = article.getSupplementaryResources();
        assertNotNull(resources);
        assertEquals(2, resources.size());

        SupplementaryResource r0 = resources.get(0);
        assertEquals(URI.create("http://pubs.acs.org/doi/suppl/10.1021/cg100078b/suppl_file/cg100078b_si_001.pdf"), r0.getUrl());
        assertEquals("cg100078b_si_001.pdf (849.93 kB)", clean(r0.getLinkText()));
        assertEquals("PDF", r0.getContentType());
        assertEquals("cg100078b_si_001.pdf", r0.getFilePath());

        SupplementaryResource r1 = resources.get(1);
        assertEquals(URI.create("http://pubs.acs.org/doi/suppl/10.1021/cg100078b/suppl_file/cg100078b_si_002.cif"), r1.getUrl());
        assertEquals("cg100078b_si_002.cif (111.39 kB)", clean(r1.getLinkText()));
        assertEquals("Crystallographic Information File", r1.getContentType());
        assertEquals("cg100078b_si_002.cif", r1.getFilePath());
    }

    private String clean(String linkText) {
        if (linkText == null) {
            return null;
        }
        return linkText.trim().replaceAll("\\s+", " ");
    }

    @Test
    public void testGetFullTextResources() throws IOException {
        AcsArticleCrawler article = getArticleCg100078b();
        List<FullTextResource> resources = article.getFullTextResources();
        assertNotNull(resources);
        assertEquals(3, resources.size());

        FullTextResource r0 = resources.get(0);
        assertEquals(URI.create("http://pubs.acs.org/doi/full/10.1021/cg100078b"), r0.getUrl());
        assertEquals("Full Text HTML", r0.getLinkText());

        FullTextResource r1 = resources.get(1);
        assertEquals(URI.create("http://pubs.acs.org/doi/pdf/10.1021/cg100078b"), r1.getUrl());
        assertEquals("Hi-Res PDF", r1.getLinkText());

        FullTextResource r2 = resources.get(2);
        assertEquals(URI.create("http://pubs.acs.org/doi/pdfplus/10.1021/cg100078b"), r2.getUrl());
        assertEquals("PDF w/ Links", r2.getLinkText());
    }

    @Test
    public void testBibtex() throws IOException {
        AcsArticleCrawler crawler = getArticleCg100078b();
        BibtexTool bibtex = crawler.getBibtex();
        assertNotNull(bibtex);
        assertEquals("Lama, Prem and Aijaz, Arshad and Neogi, Subhadip and Barbour, "
                +"Leonard J. and Bharadwaj, Parimal K.", bibtex.getAuthors());
    }



    @Test
    public void testGetAuthors() throws IOException {
        AcsArticleCrawler article = getArticleCg100078b();
        List<String> authors = article.getAuthors();
        assertEquals(5, authors.size());
        assertEquals("Prem Lama", authors.get(0));
        assertEquals("Arshad Aijaz", authors.get(1));
        assertEquals("Subhadip Neogi", authors.get(2));
        assertEquals("Leonard J. Barbour", authors.get(3));
        assertEquals("Parimal K. Bharadwaj", authors.get(4));
    }

    @Test
    public void testGetReference() throws IOException {
        AcsArticleCrawler article = getArticleCg100078b();
        Reference ref = article.getReference();
        assertEquals("Cryst. Growth Des.", ref.getJournalTitle());
        assertEquals("2010", ref.getYear());
        assertEquals("10", ref.getVolume());
        assertEquals("8", ref.getNumber());
        assertEquals("3410\u20133417", ref.getPages()); // NDASH
    }

    @Test
    public void testIsOpenAccessFalse() throws IOException {
        AcsArticleCrawler article = getArticleCg100078b();
        assertFalse(article.isOpenAccess());
    }

    @Test
    public void testIsOpenAccessTrue() throws IOException, ParsingException {
        AcsArticleCrawler article = getArticleJo1013564();
        assertTrue(article.isOpenAccess());
    }

}
