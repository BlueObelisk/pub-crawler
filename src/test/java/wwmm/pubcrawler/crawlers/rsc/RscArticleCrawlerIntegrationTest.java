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

import org.junit.AfterClass;
import org.junit.BeforeClass;
import wwmm.pubcrawler.CrawlerContext;
import wwmm.pubcrawler.model.Article;
import wwmm.pubcrawler.types.Doi;

import java.io.IOException;

/**
 * @author Sam Adams
 */
public class RscArticleCrawlerIntegrationTest extends RscArticleCrawlerTest {

    private static RscArticleCrawler crawler;

    @BeforeClass
    public static void setUp() throws IOException {
        Article article = new Article();
        article.setId("rsc/CC/47/3/C0CC03247F");
        article.setDoi(new Doi("10.1039/C0CC03247F"));

        CrawlerContext context = new CrawlerContext(null, getHttpCrawler(), null);
        crawler = new RscArticleCrawler(article, context);
    }

    @AfterClass
    public static void cleanUp() {
        crawler = null;
    }

    @Override
    protected RscArticleCrawler getArticleC0CC03247F() throws IOException {
        return crawler;
    }

}
