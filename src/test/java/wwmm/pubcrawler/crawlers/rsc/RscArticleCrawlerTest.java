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

package wwmm.pubcrawler.crawlers.rsc;

import org.junit.Ignore;
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
import wwmm.pubcrawler.types.Doi;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Sam Adams
 */
@Ignore
public class RscArticleCrawlerTest extends AbstractCrawlerTest {

    private CrawlerResponse prepareC0CC03247FResponse() throws IOException {
        return prepareResponse("./cc-C0CC03247F.html",
                URI.create("http://pubs.rsc.org/en/Content/ArticleLanding/2011/CC/c0cc03247f"));
    }

    protected RscArticleCrawler getArticleC0CC03247F() throws IOException {
        Article article = new Article();
        article.setDoi(new Doi("10.1039/C0CC03247F"));

        CrawlerResponse response = prepareC0CC03247FResponse();

        HttpCrawler crawler = Mockito.mock(HttpCrawler.class);
        Mockito.when(crawler.execute(Mockito.any(CrawlerRequest.class)))
                .thenReturn(response);

        CrawlerContext context = new CrawlerContext(null, crawler, null);
        return new RscArticleCrawler(article, context);
    }

    @Test
    public void testGetTitleHtml() throws IOException {
        RscArticleCrawler crawler = getArticleC0CC03247F();
        assertEquals("<h1 xmlns=\"http://www.w3.org/1999/xhtml\">Chemical tailoring of fullerene acceptors: synthesis, "
                +"structures and electrochemical properties of perfluoroisopropylfullerenes</h1>",
                crawler.getTitleHtmlString());
    }

    @Test
    public void testGetSupplementaryResources() throws IOException {
        RscArticleCrawler crawler = getArticleC0CC03247F();
        List<SupplementaryResource> resources = crawler.getSupplementaryResources();
        assertNotNull(resources);
        assertEquals(2, resources.size());

        SupplementaryResource r0 = resources.get(0);
        assertEquals(URI.create("http://www.rsc.org/suppdata/CC/C0/C0CC03247F/C0CC03247F.PDF"), r0.getUrl());
        assertEquals("Experimental details, spectroscopic data, notations and IUPAC numbering of isomers and DFT calculated LUMO and EA", r0.getLinkText());
        assertEquals("C0CC03247F.PDF", r0.getFilePath());

        SupplementaryResource r1 = resources.get(1);
        assertEquals(URI.create("http://www.rsc.org/suppdata/CC/C0/C0CC03247F/C0CC03247F.TXT"), r1.getUrl());
        assertEquals("Crystal structure data", r1.getLinkText());
        assertEquals("C0CC03247F.TXT", r1.getFilePath());
    }

    @Test
    public void testGetFullTextResources() throws IOException {
        RscArticleCrawler crawler = getArticleC0CC03247F();
        List<FullTextResource> resources = crawler.getFullTextResources();
        assertNotNull(resources);
        assertEquals(2, resources.size());

        FullTextResource r0 = resources.get(0);
        assertEquals(URI.create("http://pubs.rsc.org/en/Content/ArticleHTML/2011/CC/C0CC03247F"), r0.getUrl());

        FullTextResource r1 = resources.get(1);
        assertEquals(URI.create("http://pubs.rsc.org/en/Content/ArticlePDF/2011/CC/C0CC03247F"), r1.getUrl());
    }



    @Test
    public void testGetAuthors() throws IOException {
        RscArticleCrawler crawler = getArticleC0CC03247F();
        List<String> authors = crawler.getAuthors();
        assertNotNull(authors);
        assertEquals(11, authors.size());
        assertEquals("Natalia B. Shustova", authors.get(0));
        assertEquals("Igor V. Kuvychko", authors.get(1));
        assertEquals("Dmitry V. Peryshkov", authors.get(2));
        assertEquals("James B. Whitaker", authors.get(3));
        assertEquals("Bryon W. Larson", authors.get(4));
        assertEquals("Yu-Sheng Chen", authors.get(5));
        assertEquals("Lothar Dunsch", authors.get(6));
        assertEquals("Konrad Seppelt", authors.get(7));
        assertEquals("Alexey A. Popov", authors.get(8));
        assertEquals("Steven H. Strauss", authors.get(9));
        assertEquals("Olga V. Boltalina", authors.get(10));
    }

    @Test
    public void testGetReference() throws IOException {
        RscArticleCrawler crawler = getArticleC0CC03247F();
        Reference ref = crawler.getReference();
        assertNotNull(ref);
        assertEquals("Chem. Commun.", ref.getJournalTitle());
        assertEquals("2010", ref.getYear());
        assertEquals("47", ref.getVolume());
        assertEquals("3", ref.getNumber());
        assertEquals("875-877", ref.getPages());
    }

}
