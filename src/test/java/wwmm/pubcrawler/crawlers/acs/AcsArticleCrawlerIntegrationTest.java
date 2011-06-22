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

import org.junit.AfterClass;
import org.junit.BeforeClass;
import wwmm.pubcrawler.CrawlerContext;
import wwmm.pubcrawler.model.Article;
import wwmm.pubcrawler.model.id.ArticleId;
import wwmm.pubcrawler.types.Doi;

import java.io.IOException;

/**
 * @author Sam Adams
 */
public class AcsArticleCrawlerIntegrationTest extends AcsArticleCrawlerTest {

    private static AcsArticleCrawler crawlerCg100078b;
    private static AcsArticleCrawler crawlerJo1013564;

    @BeforeClass
    public static void setUpCg100078b() throws IOException {
        Article article = new Article();
        article.setId(new ArticleId("acs/cgdefu/10/8/cg100078b"));
        article.setDoi(new Doi("10.1021/cg100078b"));
        CrawlerContext context = new CrawlerContext(null, getHttpCrawler(), null);
        crawlerCg100078b = new AcsArticleCrawler(article, context);
    }

    @BeforeClass
    public static void setUpJo1013564() throws IOException {
        Article article = new Article();
        article.setId(new ArticleId("acs/joceah/75/23/jo1013564"));
        article.setDoi(new Doi("10.1021/jo1013564"));
        CrawlerContext context = new CrawlerContext(null, getHttpCrawler(), null);
        crawlerJo1013564 = new AcsArticleCrawler(article, context);
    }

    @AfterClass
    public static void cleanUp() {
        crawlerCg100078b = null;
        crawlerJo1013564 = null;
    }

    @Override
    protected AcsArticleCrawler getArticleCg100078b() throws IOException {
        return crawlerCg100078b;
    }

    @Override
    protected AcsArticleCrawler getArticleJo1013564() throws IOException {
        return crawlerJo1013564;
    }
}
