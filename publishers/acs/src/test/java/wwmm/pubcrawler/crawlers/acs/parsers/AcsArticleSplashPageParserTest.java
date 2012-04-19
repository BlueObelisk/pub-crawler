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

package wwmm.pubcrawler.crawlers.acs.parsers;

import nu.xom.Builder;
import nu.xom.Document;
import org.apache.commons.io.IOUtils;
import org.ccil.cowan.tagsoup.Parser;
import org.junit.AfterClass;
import org.junit.Test;
import wwmm.pubcrawler.crawlers.AbstractCrawlerTest;
import wwmm.pubcrawler.model.Article;
import wwmm.pubcrawler.model.FullTextResource;
import wwmm.pubcrawler.model.Reference;
import wwmm.pubcrawler.model.id.ArticleId;
import wwmm.pubcrawler.types.Doi;
import wwmm.pubcrawler.utils.ResourceUtil;

import java.io.InputStream;
import java.net.URI;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Sam Adams
 */
public class AcsArticleSplashPageParserTest extends AbstractCrawlerTest {

    private static Document cg100078b;
    private static Document jo1013564;

    @AfterClass
    public static void afterAllTests() {
        cg100078b = null;
        jo1013564 = null;
    }
    
    @Test
    public void testGetTitleHtml() throws Exception {
        AcsArticleSplashPageParser article = getArticleCg100078b();
        assertEquals("<h1 xmlns=\"http://www.w3.org/1999/xhtml\">Microporous La(III) Metal\u2212Organic Framework Using a Semirigid Tricarboxylic Ligand: "
            + "Synthesis, Single-Crystal to Single-Crystal Sorption Properties, and Gas Adsorption Studies</h1>",
            article.getTitleAsHtml());
    }

    @Test
    public void testGetSupportingInfoUrl() throws Exception {
        AcsArticleSplashPageParser article = getArticleCg100078b();
        assertEquals(URI.create("http://pubs.acs.org/doi/suppl/10.1021/cg100078b"), article.getSupportingInfoUrl());
    }

    @Test
    public void testGetFullTextResources() throws Exception {
        AcsArticleSplashPageParser article = getArticleCg100078b();
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
    public void testGetAuthors() throws Exception {
        AcsArticleSplashPageParser article = getArticleCg100078b();
        List<String> authors = article.getAuthors();
        assertEquals(5, authors.size());
        assertEquals("Prem Lama", authors.get(0));
        assertEquals("Arshad Aijaz", authors.get(1));
        assertEquals("Subhadip Neogi", authors.get(2));
        assertEquals("Leonard J. Barbour", authors.get(3));
        assertEquals("Parimal K. Bharadwaj", authors.get(4));
    }

    @Test
    public void testGetReference() throws Exception {
        AcsArticleSplashPageParser article = getArticleCg100078b();
        Reference ref = article.getReference();
        assertEquals("Cryst. Growth Des.", ref.getJournalTitle());
        assertEquals("2010", ref.getYear());
        assertEquals("10", ref.getVolume());
        assertEquals("8", ref.getNumber());
        assertEquals("3410\u20133417", ref.getPages()); // NDASH
    }

    @Test
    public void testIsOpenAccessFalse() throws Exception {
        AcsArticleSplashPageParser article = getArticleCg100078b();
        assertFalse(article.isOpenAccess());
    }

    @Test
    public void testIsOpenAccessTrue() throws Exception {
        AcsArticleSplashPageParser article = getArticleJo1013564();
        assertTrue(article.isOpenAccess());
    }

    protected AcsArticleSplashPageParser getArticleCg100078b() throws Exception {
        Article article = new Article();
        article.setId(new ArticleId("acs/cgdefu/10/8/cg100078b"));
        article.setDoi(new Doi("10.1021/cg100078b"));

        Document doc = cg100078b;
        if (doc == null) {
            doc = loadDocument("cg100078b.html");
            cg100078b = doc;
        }
        return new AcsArticleSplashPageParser(article, doc, URI.create("http://pubs.acs.org/doi/abs/10.1021/cg100078b"));
    }

    protected AcsArticleSplashPageParser getArticleJo1013564() throws Exception {
        Article article = new Article();
        article.setId(new ArticleId("acs/joceah/75/23/jo1013564"));
        article.setDoi(new Doi("10.1021/jo1013564"));

        Document doc = jo1013564;
        if (doc == null) {
            doc = loadDocument("jo1013564.html");
            jo1013564 = doc;
        }
        return new AcsArticleSplashPageParser(article, doc, URI.create("http://pubs.acs.org/doi/abs/10.1021/jo1013564"));
    }

    private Document loadDocument(String path) throws Exception {
        final InputStream in = ResourceUtil.open(getClass(), "/wwmm/pubcrawler/crawlers/acs/" + path);
        try {
            return new Builder(new Parser()).build(in);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

}
