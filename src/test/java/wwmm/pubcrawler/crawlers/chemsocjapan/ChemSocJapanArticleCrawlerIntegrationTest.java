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
import wwmm.pubcrawler.model.Article;
import wwmm.pubcrawler.types.Doi;

import java.io.IOException;

/**
 * @author Sam Adams
 */
public class ChemSocJapanArticleCrawlerIntegrationTest extends ChemSocJapanArticleCrawlerTest {

    private static ChemSocJapanArticleCrawler crawler2010_156;
    private static ChemSocJapanArticleCrawler crawler2008_682;

    @BeforeClass
    public static void setUp2010_156() throws IOException {
        Article article = new Article();
        article.setId("chemsocjapan/cl/39/3/cl.2010.156");
        article.setDoi(new Doi("10.1246/cl.2010.156"));
        CrawlerContext context = new CrawlerContext(null, getHttpCrawler(), null);
        crawler2010_156 = new ChemSocJapanArticleCrawler(article, context);
    }

    @BeforeClass
    public static void setUp2008_682() throws IOException {
        Article article = new Article();
        article.setId("chemsocjapan/cl/37/7/cl.2008.682");
        article.setDoi(new Doi("10.1246/cl.2008.682"));
        CrawlerContext context = new CrawlerContext(null, getHttpCrawler(), null);
        crawler2008_682 = new ChemSocJapanArticleCrawler(article, context);
    }

    @AfterClass
    public static void cleanUp() {
        crawler2010_156 = null;
        crawler2008_682 = null;
    }

    @Override
    protected ChemSocJapanArticleCrawler getCl2010_156() throws IOException {
        return crawler2010_156;
    }

    @Override
    protected ChemSocJapanArticleCrawler getCl2008_682() throws IOException {
        return crawler2008_682;
    }

}
