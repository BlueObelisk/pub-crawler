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
import wwmm.pubcrawler.journals.RscInfo;
import wwmm.pubcrawler.model.Article;
import wwmm.pubcrawler.model.Issue;
import wwmm.pubcrawler.model.Journal;
import wwmm.pubcrawler.model.id.IssueId;
import wwmm.pubcrawler.model.id.PublisherId;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * @author Sam Adams
 */
public class RscIssueCrawlerIntegrationTest extends RscIssueCrawlerTest {

    public static final Journal CHEM_COMM = new Journal(new PublisherId("rsc"), "cc", "Chemical communications");

    private static RscIssueCrawler cc_47_03_crawler;
    private static RscIssueCrawler jm_15_2728_crawler;

    @BeforeClass
    public static void setUp() throws IOException {
        Issue issue = new Issue();
        issue.setId(new IssueId("rsc/cc/47/3"));
        issue.setUrl(URI.create("cc047003"));

        CrawlerContext context = new CrawlerContext(null, getHttpCrawler(), null);
        cc_47_03_crawler = new RscIssueCrawler(issue, CHEM_COMM, context);
    }

    @BeforeClass
    public static void setUp2() throws IOException {
        Issue issue = new Issue();
        issue.setId(new IssueId("rsc/jm/15/27"));
        issue.setUrl(URI.create("jm015027"));

        CrawlerContext context = new CrawlerContext(null, getHttpCrawler(), null);
        jm_15_2728_crawler = new RscIssueCrawler(issue, J_MAT_CHEM, context);
    }

    @AfterClass
    public static void cleanUp() {
        cc_47_03_crawler = null;
    }

    @Override
    protected RscIssueCrawler getCcIssue() throws IOException {
        return cc_47_03_crawler;
    }

    @Override
    protected RscIssueCrawler getJm1527Issue() throws IOException {
        return jm_15_2728_crawler;
    }

    @Test
    public void testCrawlLatestCc() throws IOException {
        Issue issue = new Issue();
        issue.setId(new IssueId("rsc/cc/Latest"));
        issue.setCurrent(true);
        issue.setUrl(URI.create("Latest"));
        CrawlerContext context = new CrawlerContext(null, getHttpCrawler(), null);
        RscIssueCrawler crawler = new RscIssueCrawler(issue, CHEM_COMM, context);

        List<Article> articles = crawler.getArticles();
        assertNotNull(articles);
        assertFalse(articles.isEmpty());

        Issue previousIssue = crawler.getPreviousIssue();
        assertNotNull(previousIssue);
        assertNotNull(previousIssue.getId());
        assertNotNull(previousIssue.getUrl());
    }

}
