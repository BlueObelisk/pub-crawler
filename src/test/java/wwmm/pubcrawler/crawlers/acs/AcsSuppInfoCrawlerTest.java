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

import org.junit.Test;
import org.mockito.Mockito;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerRequest;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import uk.ac.cam.ch.wwmm.httpcrawler.HttpCrawler;
import wwmm.pubcrawler.CrawlerContext;
import wwmm.pubcrawler.crawlers.AbstractCrawlerTest;
import wwmm.pubcrawler.model.Article;
import wwmm.pubcrawler.model.SupplementaryResource;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Sam Adams
 */
public class AcsSuppInfoCrawlerTest extends AbstractCrawlerTest {

    private CrawlerResponse prepareJe100001bSuppInfoResponse() throws IOException {
        return prepareResponse("./je100001b_supp.html",
                URI.create("http://pubs.acs.org/doi/suppl/10.1021/je100001b"));
    }

    protected AcsSuppInfoCrawler getJe100001bSuppInfo() throws IOException {
        Article article = new Article();
        article.setSupplementaryResourceUrl(URI.create("http://pubs.acs.org/doi/suppl/10.1021/je100001b"));

        CrawlerResponse response = prepareJe100001bSuppInfoResponse();

        HttpCrawler crawler = Mockito.mock(HttpCrawler.class);
        Mockito.when(crawler.execute(Mockito.any(CrawlerRequest.class)))
                .thenReturn(response);

        CrawlerContext context = new CrawlerContext(null, crawler, null);
        return new AcsSuppInfoCrawler(article, context);
    }

    @Test
    public void testGetSupplementaryResources() throws IOException {
        AcsSuppInfoCrawler crawler = getJe100001bSuppInfo();
        List<SupplementaryResource> resources = crawler.getSupplementaryResources();
        assertNotNull(resources);
        assertEquals(1, resources.size());
        SupplementaryResource resource = resources.get(0);
        assertEquals("je100001b_si_001.cif", resource.getFilePath());
        assertEquals(URI.create("http://pubs.acs.org/doi/suppl/10.1021/je100001b/suppl_file/je100001b_si_001.cif"), resource.getUrl());
    }

}
