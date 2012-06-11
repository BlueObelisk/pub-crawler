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
import org.junit.Assert;
import org.junit.Test;
import wwmm.pubcrawler.crawlers.AbstractCrawlerTest;
import wwmm.pubcrawler.model.Article;
import wwmm.pubcrawler.model.Author;
import wwmm.pubcrawler.model.FullTextResource;
import wwmm.pubcrawler.model.Reference;
import wwmm.pubcrawler.model.id.ArticleId;
import wwmm.pubcrawler.types.Doi;

import java.io.InputStream;
import java.net.URI;
import java.util.List;

import static java.util.Arrays.asList;
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
    public void testGetDoi() throws Exception {
        AcsArticleSplashPageParser article = getArticleCg100078b();
        assertEquals(new Doi("10.1021/cg100078b"), article.getDoi());
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

    @Test
    public void testGetAuthorDetails() throws Exception {
        Author author1 = new Author("Prem Lama");
        author1.setAffiliation("Department of Chemistry, Indian Institute of Technology, Kanpur, 208016, India");
        Author author2 = new Author("Arshad Aijaz");
        author2.setAffiliation("Department of Chemistry, Indian Institute of Technology, Kanpur, 208016, India");
        Author author3 = new Author("Subhadip Neogi");
        author3.setAffiliation("Department of Chemistry and Polymer Science, Stellenbosch University, Private Bag X1, Matieland, 7602, South Africa");
        Author author4 = new Author("Leonard J. Barbour");
        author4.setAffiliation("Department of Chemistry and Polymer Science, Stellenbosch University, Private Bag X1, Matieland, 7602, South Africa");
        Author author5 = new Author("Parimal K. Bharadwaj");
        author5.setAffiliation("Department of Chemistry, Indian Institute of Technology, Kanpur, 208016, India");
        author5.setEmailAddress("pkb@iitk.ac.in");

        AcsArticleSplashPageParser parser = getArticleCg100078b();
        assertEquals(asList(author1, author2, author3, author4, author5), parser.getAuthorDetails());
    }

    @Test
    public void testGetAbstract() throws Exception {
        String abstractHtml = "<p>Two La(III) coordination polymers, {[La(cpia)(2H<sub>2</sub>O)]&#xB7;4H" +
            "<sub>2</sub>O}<sub><i>n</i></sub> (<b>1</b>) and {[La(cpia)(H<sub>2</sub>O)(2DMF)}<sub><i>n</i></sub> " +
            "(<b>2</b>) (cpiaH<sub>3</sub> = 5-(4-carboxy-phenoxy)-isophthalic acid, DMF = N,N&#x2032;-dimethylformamide), " +
            "have been synthesized under hydro- and solvothermal conditions, respectively. The lattice water molecules " +
            "in compound <b>1</b> could be partially replaced at RT with different solvent molecules such as ethanol, " +
            "acetone, and pyridine leading to three new daughter crystals {[La(cpia)(2H<sub>2</sub>O)]&#xB7;2H<sub>2</sub>" +
            "O&#xB7;C<sub>2</sub>H<sub>6</sub>O}<sub><i>n</i></sub> (<b>1a</b>), {[La(cpia)(2H<sub>2</sub>O)]&#xB7;2H" +
            "<sub>2</sub>O&#xB7;C<sub>3</sub>H<sub>6</sub>O}<sub><i>n</i></sub> (<b>1b</b>), and {[La(cpia)(2H<sub>2</sub>O)]" +
            "&#xB7;2H<sub>2</sub>O&#xB7;C<sub>5</sub>H<sub>5</sub>N}<sub><i>n</i></sub> (<b>1c</b>) in a single-crystal " +
            "to single-crystal (SC-SC) manner. All five compounds were further characterized by IR spectroscopy, elemental " +
            "analysis, X-ray powder diffraction, and thermogravimetry. Each polymer forms a carboxylate-bridged " +
            "three-dimensional structure with each metal adopting LaO<sub>9</sub> geometry. Thermogravimetric analysis " +
            "(TGA) shows that compound <b>1</b> loses water molecules beginning at &#x223C;80 " +
            "&#xB0;C and continues until 300 &#xB0;C, and it is thermally stable up to 400 &#xB0;C. All the daughter compounds " +
            "<b>1a</b>&#x2212;<b>1c</b> lose solvent molecules in a stepwise manner and become fully desolvated at 320 &#xB0;C. " +
            "Like <b>1</b>, all the daughter crystals are also stable up to 400 &#xB0;C and beyond that it starts to decompose. " +
            "On the other hand, <b>2</b> starts to lose weight continuously beginning at &#x223C;80 &#xB0;C " +
            "and breaks down without showing any plateau. The sorption measurements performed on desolvated <b>1</b> show " +
            "that N<sub>2</sub> molecules are adsorbed at the surface only at 77 K; however, appreciable amounts of " +
            "CO<sub>2</sub> are readily and reversibly incorporated at 273 K.</p>";

        AcsArticleSplashPageParser parser = getArticleCg100078b();
        assertEquals(abstractHtml, parser.getAbstractAsHtml());
    }

    protected AcsArticleSplashPageParser getArticleCg100078b() throws Exception {
        final ArticleId articleRef = new ArticleId("acs/cgdefu/10/8/cg100078b");

        Document doc = cg100078b;
        if (doc == null) {
            doc = loadDocument("cg100078b.html");
            cg100078b = doc;
        }
        return new AcsArticleSplashPageParser(articleRef, doc, URI.create("http://pubs.acs.org/doi/abs/10.1021/cg100078b"));
    }

    protected AcsArticleSplashPageParser getArticleJo1013564() throws Exception {
        final ArticleId articleRef = new ArticleId("acs/joceah/75/23/jo1013564");

        Document doc = jo1013564;
        if (doc == null) {
            doc = loadDocument("jo1013564.html");
            jo1013564 = doc;
        }
        return new AcsArticleSplashPageParser(articleRef, doc, URI.create("http://pubs.acs.org/doi/abs/10.1021/jo1013564"));
    }

    private Document loadDocument(String path) throws Exception {
        final InputStream in = getClass().getResourceAsStream(path);
        try {
            return new Builder(new Parser()).build(in);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

}
