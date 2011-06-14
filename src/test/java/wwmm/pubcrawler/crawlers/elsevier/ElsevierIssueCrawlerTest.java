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
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerRequest;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import uk.ac.cam.ch.wwmm.httpcrawler.HttpCrawler;
import wwmm.pubcrawler.CrawlerContext;
import wwmm.pubcrawler.crawlers.AbstractCrawlerTest;
import wwmm.pubcrawler.crawlers.AbstractIssueCrawler;
import wwmm.pubcrawler.model.Article;
import wwmm.pubcrawler.model.Issue;
import wwmm.pubcrawler.model.Journal;
import wwmm.pubcrawler.types.Doi;
import wwmm.pubcrawler.types.DoiTest;

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
public class ElsevierIssueCrawlerTest extends AbstractCrawlerTest {

    private CrawlerResponse prepareCompBioChemIssue34_4Response() throws IOException {
        return prepareResponse("./compbiochem-34-4.html",
                URI.create("http://www.sciencedirect.com/science?_ob=PublicationURL&_tockey="
                        +"%23TOC%2311514%232010%23999659995%232526737%23FLA%23&_cdi=11514&_pubType=J&_auth=y&_prev=y"
                        +"&_acct=C000050221&_version=1&_urlVersion=0&_userid=10&md5=cd07924074df81e203366810e8242eb2"));
    }

    private CrawlerResponse prepareCompBioChemIssue34_4DownloadResponse() throws IOException {
        return prepareResponse("./compbiochem-34-4-download.html",
                URI.create("http://www.sciencedirect.com/science?_ob=DownloadURL&_method=confirm&_tockey=%23toc%2311514%232010%23999659995%232526737%23FLA%23&_auth=y&_version=1&refSource=toc&_pubType=J&PDF_DDM_MAX=20&_cdi=11514&chunk=0&view=c&go=&count=8&export=Export+citations&pdfDownload=&count=8&_acct=C000053194&_version=1&_userid=1495569&md5=58b8fcd21e7a5845f6cf4105ce3cd6ca"));
    }

    private CrawlerResponse prepareCompBioChemIssue34_4BibtexResponse() throws IOException {
        return prepareResponse("./compbiochem-34-4-bibtex.txt",
                URI.create("http://www.sciencedirect.com/science"));
    }

    protected ElsevierIssueCrawler getCompBioChemIssue34_4() throws IOException {
        Issue issue = new Issue();
        issue.setUrl(URI.create("http://www.sciencedirect.com/science?_ob=PublicationURL&_tockey="
                        +"%23TOC%2311514%232010%23999659995%232526737%23FLA%23&_cdi=11514&_pubType=J&_auth=y&_prev=y"
                        +"&_acct=C000050221&_version=1&_urlVersion=0&_userid=10&md5=cd07924074df81e203366810e8242eb2"));

        CrawlerResponse response1 = prepareCompBioChemIssue34_4Response();
        CrawlerResponse response2 = prepareCompBioChemIssue34_4DownloadResponse();
        CrawlerResponse response3 = prepareCompBioChemIssue34_4BibtexResponse();

        HttpCrawler crawler = Mockito.mock(HttpCrawler.class);
        Mockito.when(crawler.execute(Mockito.any(CrawlerRequest.class)))
                .thenReturn(response1, response2, response3);

        CrawlerContext context = new CrawlerContext(null, crawler, null);
        return new ElsevierIssueCrawler(issue, new Journal("foo", "Acta Foo"), context);
    }

    @Test
    public ElsevierIssueCrawler testRunCrawler() throws IOException {
        ElsevierIssueCrawler crawler = getCompBioChemIssue34_4();
        return crawler;
    }

    @Test
    @Given("#testRunCrawler")
    public void testGetVolume(ElsevierIssueCrawler crawler) throws IOException {
        assertEquals("34", crawler.getVolume());
    }

    @Test
    @Given("#testRunCrawler")
    public void testGetNumber(ElsevierIssueCrawler crawler) throws IOException {
        assertEquals("4", crawler.getNumber());
    }

    @Test
    @Given("#testRunCrawler")
    public void testGetYear(ElsevierIssueCrawler crawler) throws IOException {
        assertEquals("2010", crawler.getYear());
    }

    @Test
    @Given("#testRunCrawler")
    public void testGetJournalTitle(ElsevierIssueCrawler crawler) throws IOException {
        assertEquals("Computational Biology and Chemistry", crawler.getJournalTitle());
    }

    @Test
    @Given("#testRunCrawler")
    public List<Article> testGetArticles(ElsevierIssueCrawler crawler) throws IOException {
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
