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

import nu.xom.Document;
import org.apache.log4j.Logger;
import wwmm.pubcrawler.CrawlerContext;
import wwmm.pubcrawler.crawlers.JournalHandler;
import wwmm.pubcrawler.model.Issue;
import wwmm.pubcrawler.model.Journal;
import wwmm.pubcrawler.model.id.IssueId;
import wwmm.pubcrawler.utils.XPathUtils;

import java.io.IOException;
import java.net.URI;
import java.util.List;

/**
 * @author Sam Adams
 */
public class ChemSocJapanJournalCrawler extends JournalHandler {

    private static final Logger LOG = Logger.getLogger(ChemSocJapanJournalCrawler.class);

    public ChemSocJapanJournalCrawler(Journal journal, CrawlerContext context) {
        super(journal, context);
    }

    @Override
    protected Logger log() {
        return LOG;
    }

    @Override
    public Issue fetchCurrentIssue() throws IOException {
        log().debug("Fetching current issue of " + getJournal().getTitle());
        URI url = getCurrentIssueUrl();
        log().debug("current issue URL: "+url);
        Issue issue = new Issue();
        issue.setUrl(url);
        return fetchIssue(issue);
    }

    private URI getCurrentIssueUrl() throws IOException {
        URI url = getHomepageUrl();
        Document html = readHtml(url, new IssueId("chemsocjapan/chem-lett/current"), AGE_1DAY);
        String href = XPathUtils.getString(html, ".//x:a[.='Current issue']/@href");
        URI frameUri = url.resolve(href);
        return frameUri.resolve("./isscontsbdy.html");
    }

    private URI getHomepageUrl() {
        return URI.create("http://www.chemistry.or.jp/journals/chem-lett/cl-cont/newissue.html");
    }


    @Override
    public List<Issue> fetchIssueList() throws IOException {
        ChemSocJapanIndexCrawler crawler = new ChemSocJapanIndexCrawler(getJournal(), getContext());
        return crawler.getIssues();
    }

}
