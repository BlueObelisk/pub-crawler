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
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerRequest;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import uk.ac.cam.ch.wwmm.httpcrawler.HttpCrawler;
import wwmm.pubcrawler.CrawlerContext;
import wwmm.pubcrawler.crawlers.AbstractCrawlerTest;
import wwmm.pubcrawler.model.Article;
import wwmm.pubcrawler.model.Reference;
import wwmm.pubcrawler.model.SupplementaryResource;
import wwmm.pubcrawler.model.id.ArticleId;
import wwmm.pubcrawler.types.Doi;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Sam Adams
 */
public class NatureSuppInfoCrawlerTest extends AbstractCrawlerTest {

    private CrawlerResponse prepareNchem111() throws IOException {
        return prepareResponse("./nchem-111_S1.html",
                URI.create("http://www.nature.com/nchem/journal/v1/n1/suppinfo/nchem.111_S1.html"));
    }

    protected NatureSuppInfoCrawler getNchem111() throws IOException {
        Article article = new Article();
        article.setId(new ArticleId("nature/nchem/1/1/nchem.111"));
        article.setDoi(new Doi("10.1038/nchem.111"));
        article.setSupplementaryResourceUrl(URI.create("http://www.nature.com/nchem/journal/v1/n1/suppinfo/nchem.111_S1.html"));

        CrawlerResponse response = prepareNchem111();

        HttpCrawler crawler = Mockito.mock(HttpCrawler.class);
        Mockito.when(crawler.execute(Mockito.any(CrawlerRequest.class)))
                .thenReturn(response);

        CrawlerContext context = new CrawlerContext(null, crawler, null);
        return new NatureSuppInfoCrawler(article, context);
    }


//    private CrawlerResponse prepareNchem943() throws IOException {
//        return prepareResponse("./nchem-943.html",
//                URI.create("http://www.nature.com/nchem/journal/v3/n1/full/nchem.943.html"));
//    }
//
//    protected NatureSuppInfoCrawler getNchem943() throws IOException {
//        Article article = new Article();
//        article.setId("nature/nchem/3/1/nchem.943");
//        article.setDoi(new Doi("10.1038/nchem.943"));
//
//        CrawlerResponse response = prepareNchem943();
//
//        HttpCrawler crawler = Mockito.mock(HttpCrawler.class);
//        Mockito.when(crawler.execute(Mockito.any(CrawlerRequest.class)))
//                .thenReturn(response);
//
//        CrawlerContext context = new CrawlerContext(null, crawler, null);
//        return new NatureSuppInfoCrawler(article, context);
//    }
//
////    @Test
////    public void testGetArticleId() throws IOException {
////        AbstractArticleCrawler crawler = getNchem943();
////        assertEquals("nature/nchem/3/1/nchem.943", crawler.getArticleId());
////    }
//
//    @Test
//    public void testGetTitleHtmlString() throws IOException {
//        NatureSuppInfoCrawler crawler = getNchem943();
//        assertEquals("<h1 xmlns=\"http://www.w3.org/1999/xhtml\">Exchange-enhanced reactivity in bond activation by "
//                +"metal\u2013oxo enzymes and synthetic reagents</h1>", crawler.getTitleHtmlString());
//    }
//
//    @Test
//    public void testGetAbstractHtmlString() throws IOException {
//        NatureSuppInfoCrawler crawler = getNchem943();
//        assertEquals("<p xmlns=\"http://www.w3.org/1999/xhtml\">Reactivity principles based on orbital overlap and "
//                +"bonding/antibonding interactions are well established to describe the reactivity of organic "
//                +"species, and atomic structures are typically predicted by Hund's rules to have maximum "
//                +"single-electron occupancy of degenerate orbitals in the ground state. Here, we extend the role "
//                +"of exchange to transition states and discuss how, for reactions and kinetics of bioinorganic "
//                +"species, the analogue of Hund's rules is exchange-controlled reactivity. Pathways that increase "
//                +"the number of unpaired and spin-identical electrons on a metal centre will be favoured by exchange "
//                +"stabilization. Such exchange-enhanced reactivity endows transition states with a stereochemistry "
//                +"different from that observed in cases that are not exchange-enhanced, and is in good agreement "
//                +"with the reactivity observed for iron-based enzymes and synthetic analogues. We discuss the "
//                +"interplay between orbital- and exchange-controlled principles, and how this depends on the "
//                +"identity of the transition metal, its oxidation number and its coordination sphere.</p>", crawler.getAbstractHtmlString());
//    }
//
//    @Test
//    public void testGetReference() throws IOException {
//        NatureSuppInfoCrawler crawler = getNchem943();
//        Reference ref = crawler.getReference();
//        assertNotNull(ref);
//        assertEquals("Nature Chemistry", ref.getJournalTitle());
//        assertEquals("3", ref.getVolume());
//        assertEquals("1", ref.getNumber());
//        assertEquals("2011", ref.getYear());
//        assertEquals("19\u201327", ref.getPages());
//    }
//
//
//    @Test
//    public void testGetAuthors() throws IOException {
//        NatureSuppInfoCrawler crawler = getNchem943();
//        List<String> authors = crawler.getAuthors();
//        assertNotNull(authors);
//        assertEquals(3, authors.size());
//        assertEquals("Sason Shaik", authors.get(0));
//        assertEquals("Hui Chen", authors.get(1));
//        assertEquals("Deepa Janardanan", authors.get(2));
//    }
//
//    @Test
//    public void testGetSupplementaryResources() throws IOException {
//        NatureSuppInfoCrawler crawler = getNchem943();
//        List<SupplementaryResource> resources = crawler.getSupplementaryResources();
//        assertNotNull(resources);
//        assertEquals(1, resources.size());
//
//        SupplementaryResource r0 = resources.get(0);
//        assertEquals(URI.create("http://www.nature.com/nchem/journal/v3/n1/extref/nchem.943-s1.pdf"), r0.getUrl());
//        assertEquals("Supplementary information (309K)", r0.getLinkText());
//        assertEquals("nchem.943-s1.pdf", r0.getFilePath());
//    }
//
//
//    @Test
//    public void testIsOpenAccessFalse() throws IOException {
//        NatureSuppInfoCrawler crawler = getNchem943();
//        assertFalse(crawler.isOpenAccess());
//    }


    @Test
    public void testGetReference111() throws IOException {
        NatureSuppInfoCrawler crawler = getNchem111();
        Reference ref = crawler.getReference();
        assertNotNull(ref);
        assertEquals("Nature Chemistry", ref.getJournalTitle());
        assertEquals("1", ref.getVolume());
        assertEquals("2009", ref.getYear());
        assertEquals("87-90", ref.getPages());
    }

    @Test
    public void testGetSuppInfo111() throws IOException {
        NatureSuppInfoCrawler crawler = getNchem111();
        List<SupplementaryResource> resources = crawler.getSupplementaryResources();
        assertEquals(1, resources.size());
        assertEquals(URI.create("http://www.nature.com/nchem/journal/v1/n1/extref/nchem.111-s1.pdf"), resources.get(0).getUrl());
        assertEquals("Supplementary information - Download PDF file (1,404 KB)", resources.get(0).getLinkText());
    }

}
