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

import nu.xom.Document;
import nu.xom.Node;
import org.apache.log4j.Logger;
import wwmm.pubcrawler.CrawlerContext;
import wwmm.pubcrawler.CrawlerRuntimeException;
import wwmm.pubcrawler.crawlers.JournalHandler;
import wwmm.pubcrawler.model.Issue;
import wwmm.pubcrawler.model.Journal;
import wwmm.pubcrawler.model.id.IssueId;
import wwmm.pubcrawler.model.id.JournalId;
import wwmm.pubcrawler.utils.XPathUtils;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Sam Adams
 */
public class ActaJournalCrawler extends JournalHandler {

    private static final Logger LOG = Logger.getLogger(ActaJournalCrawler.class);

    public ActaJournalCrawler(Journal journal, CrawlerContext context) {
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
        issue.setId(getIssueId(url));
        issue.setYear(getIssueYear(url));
        issue.setUrl(url);
        return fetchIssue(issue);
    }

    @Override
    public List<Issue> fetchIssueList() throws IOException {
        log().debug("Fetching issue list for " + getJournal().getTitle());
        URI url = getIssueListUrl();
        JournalId journalId = new JournalId("acta/"+getJournal().getAbbreviation());
        Document html = readHtml(url, journalId, "issuelist", AGE_1DAY);
        List<Node> nodes = XPathUtils.queryHTML(html, ".//x:li[x:img]/x:a/@href");
        List<Issue> issues = new ArrayList<Issue>();
        for (Node node : nodes) {
            String href = node.getValue();
            if (href.contains("/issues/")) {
                URI frameUrl = url.resolve(href);
                Issue issue = new Issue();
                issue.setId(getIssueId(frameUrl));
                issue.setYear(getIssueYear(frameUrl));
                issue.setUrl(frameUrl.resolve("./isscontsbdy.html"));
                issues.add(issue);
            }
        }
        return issues;
    }

    private URI getIssueListUrl() {
        URI url = URI.create("http://journals.iucr.org/"+getJournal().getAbbreviation()+"/contents/backissuesbdy.html");
        return url;
    }

    private String getIssueYear(URI url) {
        // http://journals.iucr.org/e/issues/2011/01/00/issconts.html
        Pattern p = Pattern.compile("/issues/(\\d+)/");
        Matcher m = p.matcher(url.toString());
        if (!m.find()) {
            throw new CrawlerRuntimeException("No match: "+url);
        }
        return m.group(1);
    }

    private IssueId getIssueId(URI url) {
        // http://journals.iucr.org/e/issues/2011/01/00/issconts.html
        Pattern p = Pattern.compile("/issues/(\\w+)/(\\w+)/(\\w+)/");
        Matcher m = p.matcher(url.toString());
        if (!m.find()) {
            throw new CrawlerRuntimeException("No match: "+url);
        }
        return new IssueId("acta/"+getJournal().getAbbreviation()+'/'+m.group(1)+'/'+m.group(2)+'-'+m.group(3));
    }

    private URI getCurrentIssueUrl() throws IOException {
        URI url = getHomepageUrl();
        JournalId journalId = new JournalId("acta/"+getJournal().getAbbreviation());
        Document html = readHtml(url, journalId, "home", AGE_1DAY);
        String href = XPathUtils.getString(html, ".//x:a[.='Current issue']/@href");
        URI frameUri = url.resolve(href);
        return frameUri.resolve("./isscontsbdy.html");
    }

    private URI getHomepageUrl() {
        return URI.create("http://journals.iucr.org/" + getJournal().getAbbreviation() + "/journalhomepagebdy.html");
    }

}
