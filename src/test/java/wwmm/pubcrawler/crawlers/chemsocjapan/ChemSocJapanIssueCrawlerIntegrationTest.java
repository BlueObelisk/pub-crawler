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

package wwmm.pubcrawler.crawlers.chemsocjapan;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import wwmm.pubcrawler.CrawlerContext;
import wwmm.pubcrawler.model.Issue;

import java.io.IOException;
import java.net.URI;

/**
 * @author Sam Adams
 */
public class ChemSocJapanIssueCrawlerIntegrationTest extends ChemSocJapanIssueCrawlerTest {

    private static ChemSocJapanIssueCrawler crawler;

    @BeforeClass
    public static void setUp() throws IOException {
        Issue issue = new Issue();
        issue.setId("chemsocjapan/cl/37/7/3");
        issue.setUrl(URI.create("http://www.csj.jp/journals/chem-lett/cl-cont/cl2010-3.html"));
        CrawlerContext context = new CrawlerContext(null, getHttpCrawler(), null);
        crawler = new ChemSocJapanIssueCrawler(issue, context);
    }

    @AfterClass
    public static void cleanUp() {
        crawler = null;
    }

    @Override
    protected ChemSocJapanIssueCrawler getCl2010_3() throws IOException {
        return crawler;
    }
    
}
