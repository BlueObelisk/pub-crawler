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

package wwmm.pubcrawler.crawlers.acta;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import wwmm.pubcrawler.CrawlerContext;
import wwmm.pubcrawler.model.Issue;

import java.io.IOException;
import java.net.URI;

/**
 * @author Sam Adams
 */
public class ActaIssueCrawlerIntegrationTest extends ActaIssueCrawlerTest {

    private static ActaIssueCrawler crawler;

    @BeforeClass
    public static void setUp() throws IOException {
        Issue issue = new Issue();
        issue.setId("acta/e/2010/01-00");
        issue.setUrl(URI.create("http://journals.iucr.org/b/issues/2010/01/00/isscontsbdy.html"));
        CrawlerContext context = new CrawlerContext(null, getHttpCrawler(), null);
        crawler = new ActaIssueCrawler(issue, context);
    }

    @AfterClass
    public static void cleanUp() {
        crawler = null;
    }

    @Override
    protected ActaIssueCrawler getActaB2010_01() throws IOException {
        return crawler;
    }

}
