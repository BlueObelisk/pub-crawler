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
import wwmm.pubcrawler.model.Article;
import wwmm.pubcrawler.model.id.ArticleId;
import wwmm.pubcrawler.types.Doi;

import java.io.IOException;

/**
 * @author Sam Adams
 */
public class ActaArticleCrawlerIntegrationTest extends ActaArticleCrawlerTest {

    private static ActaArticleCrawler crawlerBt5401;

    private static ActaArticleCrawler crawlerBk5081;

    @BeforeClass
    public static void setUpBt5401() throws IOException {
        Article article = new Article();
        article.setId(new ArticleId("acta/e/2010/12-00/bt5401"));
        article.setDoi(new Doi("10.1107/S1600536810045198"));
        CrawlerContext context = new CrawlerContext(null, getHttpCrawler(), null);
        crawlerBt5401 = new ActaArticleCrawler(article, context);
    }

    @BeforeClass
    public static void setUpBk5801() throws IOException {
        Article article = new Article();
        article.setId(new ArticleId("acta/b/2009/02-00/bk5081"));
        article.setDoi(new Doi("10.1107/S0108768109004066"));
        CrawlerContext context = new CrawlerContext(null, getHttpCrawler(), null);
        crawlerBk5081 = new ActaArticleCrawler(article, context);
    }

    @AfterClass
    public static void cleanUp() {
        crawlerBt5401 = null;
        crawlerBk5081 = null;
    }

    @Override
    protected ActaArticleCrawler getBt5401() throws IOException {
        return crawlerBt5401;
    }

    @Override
    protected ActaArticleCrawler getBk5801() throws IOException {
        return crawlerBk5081;
    }
    
}
