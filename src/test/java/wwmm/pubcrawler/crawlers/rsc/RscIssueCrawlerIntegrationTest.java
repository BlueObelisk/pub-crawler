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
import org.junit.Test;
import wwmm.pubcrawler.CrawlerContext;
import wwmm.pubcrawler.journals.RscJournalIndex;
import wwmm.pubcrawler.model.Article;
import wwmm.pubcrawler.model.Issue;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * @author Sam Adams
 */
public class RscIssueCrawlerIntegrationTest extends RscIssueCrawlerTest {

    private static RscIssueCrawler crawler;

    @BeforeClass
    public static void setUp() throws IOException {
        Issue issue = new Issue();
        issue.setId("rsc/CC/47/3");
        issue.setUrl(URI.create("CC047003"));

        CrawlerContext context = new CrawlerContext(null, getHttpCrawler(), null);
        crawler = new RscIssueCrawler(issue, RscJournalIndex.CHEMICAL_COMMUNICATIONS, context);
    }

    @AfterClass
    public static void cleanUp() {
        crawler = null;
    }

    @Override
    protected RscIssueCrawler getCcIssue() throws IOException {
        return crawler;
    }

    @Test
    public void testCrawlLatestCc() throws IOException {
        Issue issue = new Issue();
        issue.setId("rsc/cc/Latest");
        issue.setCurrent(true);
        issue.setUrl(URI.create("Latest"));
        CrawlerContext context = new CrawlerContext(null, getHttpCrawler(), null);
        RscIssueCrawler crawler = new RscIssueCrawler(issue, RscJournalIndex.CHEMICAL_COMMUNICATIONS, context);

        List<Article> articles = crawler.getArticles();
        assertNotNull(articles);
        assertFalse(articles.isEmpty());

        Issue previousIssue = crawler.getPreviousIssue();
        assertNotNull(previousIssue);
        assertNotNull(previousIssue.getId());
        assertNotNull(previousIssue.getUrl());
    }

}
