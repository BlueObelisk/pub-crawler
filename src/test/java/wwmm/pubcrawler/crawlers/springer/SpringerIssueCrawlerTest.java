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

package wwmm.pubcrawler.crawlers.springer;

import org.junit.Test;
import org.mockito.Mockito;
import uk.ac.cam.ch.wwmm.httpcrawler.httpcrawler.CrawlerRequest;
import uk.ac.cam.ch.wwmm.httpcrawler.httpcrawler.CrawlerResponse;
import uk.ac.cam.ch.wwmm.httpcrawler.httpcrawler.HttpCrawler;
import wwmm.pubcrawler.CrawlerContext;
import wwmm.pubcrawler.crawlers.AbstractCrawlerTest;
import wwmm.pubcrawler.model.Article;
import wwmm.pubcrawler.model.Issue;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Sam Adams
 */
public class SpringerIssueCrawlerTest extends AbstractCrawlerTest {

    private CrawlerResponse prepareZoomorphology_118_2Response() throws IOException {
        return prepareResponse("./zoomorphology-118-2.html",
                URI.create("http://www.springerlink.com/content/0720-213x/118/2/"));
    }

    protected SpringerIssueCrawler getZoomorphologyIssue118_2() throws IOException {
        Issue issue = new Issue();
        issue.setUrl(URI.create("http://www.springerlink.com/content/0720-213x/118/2/"));

        CrawlerResponse response = prepareZoomorphology_118_2Response();

        HttpCrawler crawler = Mockito.mock(HttpCrawler.class);
        Mockito.when(crawler.execute(Mockito.any(CrawlerRequest.class)))
                .thenReturn(response);

        CrawlerContext context = new CrawlerContext(null, crawler, null);
        return new SpringerIssueCrawler(issue, context);
    }

    @Test
    public void testGetVolume() throws IOException {
        SpringerIssueCrawler crawler = getZoomorphologyIssue118_2();
        assertEquals("118", crawler.getVolume());
    }

    @Test
    public void testGetNumber() throws IOException {
        SpringerIssueCrawler crawler = getZoomorphologyIssue118_2();
        assertEquals("2", crawler.getNumber());
    }

    @Test
    public void testGetYear() throws IOException {
        SpringerIssueCrawler crawler = getZoomorphologyIssue118_2();
        assertEquals("1998", crawler.getYear());
    }

    @Test
    public void testGetArticles() throws IOException {
        SpringerIssueCrawler crawler = getZoomorphologyIssue118_2();
        List<Article> articles = crawler.getArticles();
        assertNotNull(articles);
        assertEquals(6, articles.size());
    }

    @Test
    public void testGetArticleTitles() throws IOException {
        SpringerIssueCrawler crawler = getZoomorphologyIssue118_2();
        List<Article> articles = crawler.getArticles();
        assertNotNull(articles);
        assertEquals(6, articles.size());

        assertEquals("A commissural brain! The pattern of 5-HT immunoreactivity in Acoela (Plathelminthes)", articles.get(0).getTitle());
        assertEquals("The cephalic aorta of Sepia officinalis (Cephalopoda, Dibranchiata): structural muscular specialisations of a high-pressure vessel", articles.get(1).getTitle());
        assertEquals("Organization and mechanical behaviour of myocyte–ligament composites in a sea-urchin lantern: the compass depressors of Stylocidaris affinis (Echinodermata, Echinoida)", articles.get(2).getTitle());
        assertEquals("Larval head morphology of Hydroscapha natans (Coleoptera, Myxophaga) with reference to miniaturization and the systematic position of Hydroscaphidae", articles.get(3).getTitle());
        assertEquals("Ultrastructure and formation of hooded hooks in Capitella capitata (Annelida, Capitellida)", articles.get(4).getTitle());
        assertEquals("A safety band prevents falling of the suspended pupa of the butterfly Inachis io (Nymphalidae) during moult. A comparison with the girdled pupa of Pieris brassicae (Pieridae, Lepidoptera)", articles.get(5).getTitle());
    }

    @Test
    public void testGetArticleAuthors() throws IOException {
        SpringerIssueCrawler crawler = getZoomorphologyIssue118_2();
        List<Article> articles = crawler.getArticles();
        assertNotNull(articles);
        assertEquals(6, articles.size());

        assertEquals(Arrays.asList("Olga I. Raikova", "M. Reuter", "Elena A. Kotikova", "Margaretha K. S. Gustafsson"), articles.get(0).getAuthors());
        assertEquals(Arrays.asList("R. Schipp", "Bernhard Versen", "Gerd Magdowski"), articles.get(1).getAuthors());
        assertEquals(Arrays.asList("I. C. Wilkie", "M. Daniela Candia Carnevali", "Francesco Bonasoro"), articles.get(2).getAuthors());
        assertEquals(Arrays.asList("R. G. Beutel", "A. Haas"), articles.get(3).getAuthors());
        assertEquals(Arrays.asList("Margit Schweigkofler", "T. Bartolomaeus", "Luitfried von Salvini-Plawen"), articles.get(4).getAuthors());
        assertEquals(Arrays.asList("Gerhard Starnecker"), articles.get(5).getAuthors());
    }

    @Test
    public void testGetArticleUrls() throws IOException {
        SpringerIssueCrawler crawler = getZoomorphologyIssue118_2();
        List<Article> articles = crawler.getArticles();
        assertNotNull(articles);
        assertEquals(6, articles.size());

        assertEquals(URI.create("http://www.springerlink.com/content/kbmvdnwfbpvtu5kh/"), articles.get(0).getUrl());
        assertEquals(URI.create("http://www.springerlink.com/content/6avnyxb9xu6e9q3f/"), articles.get(1).getUrl());
        assertEquals(URI.create("http://www.springerlink.com/content/1a08wyqbw9mk8m8g/"), articles.get(2).getUrl());
        assertEquals(URI.create("http://www.springerlink.com/content/4ch6enql043mf4ce/"), articles.get(3).getUrl());
        assertEquals(URI.create("http://www.springerlink.com/content/3yr4kwwdkermu9hy/"), articles.get(4).getUrl());
        assertEquals(URI.create("http://www.springerlink.com/content/lemuby8etw5jg3dk/"), articles.get(5).getUrl());
    }

    @Test
    public void testGetArticlePages() throws IOException {
        SpringerIssueCrawler crawler = getZoomorphologyIssue118_2();
        List<Article> articles = crawler.getArticles();
        assertNotNull(articles);
        assertEquals(6, articles.size());

        assertEquals("69-77", articles.get(0).getReference().getPages());
        assertEquals("79-85", articles.get(1).getReference().getPages());
        assertEquals("87-101", articles.get(2).getReference().getPages());
        assertEquals("103-116", articles.get(3).getReference().getPages());
        assertEquals("117-128", articles.get(4).getReference().getPages());
        assertEquals("129-136", articles.get(5).getReference().getPages());
    }

}
