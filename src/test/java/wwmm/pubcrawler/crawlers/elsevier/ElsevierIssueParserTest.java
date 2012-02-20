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

package wwmm.pubcrawler.crawlers.elsevier;

import ch.unibe.jexample.Given;
import ch.unibe.jexample.Injection;
import ch.unibe.jexample.InjectionPolicy;
import ch.unibe.jexample.JExample;
import nu.xom.Builder;
import nu.xom.Document;
import org.apache.commons.io.IOUtils;
import org.ccil.cowan.tagsoup.Parser;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerRequest;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import uk.ac.cam.ch.wwmm.httpcrawler.HttpCrawler;
import wwmm.pubcrawler.CrawlerContext;
import wwmm.pubcrawler.crawlers.AbstractCrawlerTest;
import wwmm.pubcrawler.model.Article;
import wwmm.pubcrawler.model.Issue;
import wwmm.pubcrawler.model.Journal;
import wwmm.pubcrawler.model.id.IssueId;
import wwmm.pubcrawler.model.id.JournalId;
import wwmm.pubcrawler.model.id.PublisherId;
import wwmm.pubcrawler.types.Doi;
import wwmm.pubcrawler.utils.ResourceUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
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
public class ElsevierIssueParserTest {

    private static final PublisherId ELSEVIER = new PublisherId("elsevier");
    private static final Journal JOURNAL = new Journal(ELSEVIER, "compbiochem", "Computational Biology and Chemistry");
    private static final IssueId ISSUE = new IssueId(JOURNAL.getId(), "34", "4");
    
    private Document loadDocument(String path) throws Exception {
        final InputStream in  = ResourceUtil.open(getClass(), "/wwmm/pubcrawler/crawlers/elsevier/" + path);
        try {
            String s = IOUtils.toString(in);
            return new Builder(new Parser()).build(new StringReader(
                s.replace("_http://www.w3.org/TR/html4/loose.dtd", "http://www.w3.org/TR/html4/loose.dtd")));
        } finally {
            IOUtils.closeQuietly(in);
        }
    }


    protected ElsevierIssueParser getCompBioChemIssue34_4() throws Exception {
        Issue issue = new Issue();
        issue.setId(new IssueId("elsevier/compbiochem/34/4"));
        issue.setUrl(URI.create("http://www.sciencedirect.com/science?_ob=PublicationURL&_tockey="
                        +"%23TOC%2311514%232010%23999659995%232526737%23FLA%23&_cdi=11514&_pubType=J&_auth=y&_prev=y"
                        +"&_acct=C000050221&_version=1&_urlVersion=0&_userid=10&md5=cd07924074df81e203366810e8242eb2"));

        return new ElsevierIssueParser(issue, loadDocument("compbiochem-34-4.html"), JOURNAL);
    }

    @Test
    public ElsevierIssueParser testRunCrawler() throws Exception {
        ElsevierIssueParser crawler = getCompBioChemIssue34_4();
        return crawler;
    }

    @Test
    @Given("#testRunCrawler")
    public void testGetVolume(ElsevierIssueParser crawler) throws IOException {
        assertEquals("34", crawler.getVolume());
    }

    @Test
    @Given("#testRunCrawler")
    public void testGetNumber(ElsevierIssueParser crawler) throws IOException {
        assertEquals("4", crawler.getNumber());
    }

    @Test
    @Given("#testRunCrawler")
    public void testGetYear(ElsevierIssueParser crawler) throws IOException {
        assertEquals("2010", crawler.getYear());
    }

    @Test
    @Given("#testRunCrawler")
    public void testGetJournalTitle(ElsevierIssueParser crawler) throws IOException {
        assertEquals("Computational Biology and Chemistry", crawler.getJournalTitle());
    }

    @Test
    @Given("#testRunCrawler")
    public List<Article> testGetArticles(ElsevierIssueParser crawler) throws IOException {
        List<Article> articles = crawler.getArticles();
        assertNotNull(articles);
        assertEquals(8, articles.size());
        return articles;
    }

    @Test
    @Given("#testGetArticles")
    public void testGetArticleTitles(List<Article> articles) throws IOException {
        assertEquals("Editorial Board", articles.get(0).getTitle());
        assertEquals("Editorial Board", articles.get(1).getTitle());
        assertEquals("Stable feature selection for biomarker discovery", articles.get(2).getTitle());
        assertEquals("Homology modeling, binding site identification and docking in flavone hydroxylase CYP105P2 in Streptomyces peucetius ATCC 27952", articles.get(3).getTitle());
        assertEquals("Computational identification and characterization of primate-specific microRNAs in human genome", articles.get(4).getTitle());
        assertEquals("Ped_Outlier software for automatic identification of within-family outliers", articles.get(5).getTitle());
        assertEquals("Compact cancer biomarkers discovery using a swarm intelligence feature selection algorithm", articles.get(6).getTitle());
        assertEquals("Systematic analysis of an amidase domain CHAP in 12 Staphylococcus aureus genomes and 44 staphylococcal phage genomes", articles.get(7).getTitle());
    }

    @Test
    @Ignore
    @Given("#testGetArticles")
    public void testGetArticleDois(List<Article> articles) throws IOException {
        assertEquals(new Doi("10.1016/S1476-9271(10)00080-0"), articles.get(0).getDoi());
        assertEquals(new Doi("10.1016/S1476-9271(10)00082-4"), articles.get(1).getDoi());
        assertEquals(new Doi("10.1016/j.compbiolchem.2010.07.002"), articles.get(2).getDoi());
        assertEquals(new Doi("10.1016/j.compbiolchem.2010.08.002"), articles.get(3).getDoi());
        assertEquals(new Doi("10.1016/j.compbiolchem.2010.08.001"), articles.get(4).getDoi());
        assertEquals(new Doi("10.1016/j.compbiolchem.2010.08.004"), articles.get(5).getDoi());
        assertEquals(new Doi("10.1016/j.compbiolchem.2010.08.003"), articles.get(6).getDoi());
        assertEquals(new Doi("10.1016/j.compbiolchem.2010.07.001"), articles.get(7).getDoi());

    }

    @Test
    @Given("#testGetArticles")
    public void testGetArticleAuthors(List<Article> articles) throws IOException {
        assertEquals(Arrays.asList(), articles.get(0).getAuthors());
        assertEquals(Arrays.asList(), articles.get(1).getAuthors());
        assertEquals(Arrays.asList("Zengyou He", "Weichuan Yu"), articles.get(2).getAuthors());
        assertEquals(Arrays.asList("Bashistha Kumar Kanth", "Kwangkyoung Liou", "Jae Kyung Sohng"), articles.get(3).getAuthors());
        assertEquals(Arrays.asList("Sheng Lin", "William K.C. Cheung", "Shen Chen", "Gang Lu", "Zifeng Wang", "Dan Xie", "Kui Li", "Marie C.M. Lin", "Hsiang-fu Kung"), articles.get(4).getAuthors());
        assertEquals(Arrays.asList("Irina V. Zorkoltseva", "Yurii S. Aulchenko", "Cornelia M. van Duijn", "Tatiana I. Axenovich"), articles.get(5).getAuthors());
        assertEquals(Arrays.asList("Emmanuel Martinez", "Mario Moises Alvarez", "Victor Trevino"), articles.get(6).getAuthors());
        assertEquals(Arrays.asList("Yanming Zou", "Chun Hou"), articles.get(7).getAuthors());        
    }

    @Test
    @Given("#testGetArticles")
    public void testGetArticlePages(List<Article> articles) throws IOException {
        assertEquals("CO2", articles.get(0).getReference().getPages());
        assertEquals("iii", articles.get(1).getReference().getPages());
        assertEquals("215-225", articles.get(2).getReference().getPages());
        assertEquals("226-231", articles.get(3).getReference().getPages());
        assertEquals("232-241", articles.get(4).getReference().getPages());
        assertEquals("242-243", articles.get(5).getReference().getPages());
        assertEquals("244-250", articles.get(6).getReference().getPages());
        assertEquals("251-257", articles.get(7).getReference().getPages());
    }

}
