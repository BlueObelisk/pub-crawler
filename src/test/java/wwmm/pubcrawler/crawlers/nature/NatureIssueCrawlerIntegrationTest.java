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

import org.junit.AfterClass;
import org.junit.BeforeClass;
import wwmm.pubcrawler.CrawlerContext;
import wwmm.pubcrawler.model.Issue;

import java.io.IOException;
import java.net.URI;

/**
 * @author Sam Adams
 */
public class NatureIssueCrawlerIntegrationTest extends NatureIssueCrawlerTest {

    private static NatureIssueCrawler crawler;

    @BeforeClass
    public static void setUp() throws IOException {
        Issue issue = new Issue();
        issue.setId("nature/nchem/3/1");
        issue.setUrl(URI.create("http://www.nature.com/nchem/journal/v3/n1/index.html"));
        CrawlerContext context = new CrawlerContext(null, getHttpCrawler(), null);
        crawler = new NatureIssueCrawler(issue, context);
    }

    @AfterClass
    public static void cleanUp() {
        crawler = null;
    }

    @Override
    protected NatureIssueCrawler getNchem3_1() throws IOException {
        return crawler;
    }
}
