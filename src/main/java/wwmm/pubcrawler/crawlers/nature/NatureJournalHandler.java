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
package wwmm.pubcrawler.crawlers.nature;

import nu.xom.Document;
import org.apache.log4j.Logger;
import wwmm.pubcrawler.CrawlerContext;
import wwmm.pubcrawler.CrawlerRuntimeException;
import wwmm.pubcrawler.crawlers.AbstractJournalHandler;
import wwmm.pubcrawler.model.Issue;
import wwmm.pubcrawler.model.Journal;
import wwmm.pubcrawler.model.id.IssueId;
import wwmm.pubcrawler.model.id.JournalId;
import wwmm.pubcrawler.utils.XPathUtils;

import java.io.IOException;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Sam Adams
 */
public class NatureJournalHandler extends AbstractJournalHandler {

    private static final Logger LOG = Logger.getLogger(NatureJournalHandler.class);

    public NatureJournalHandler(Journal journal, CrawlerContext context) {
        super(journal, context);
    }

    @Override
    protected Logger log() {
        return LOG;
    }

    public Issue fetchCurrentIssue() throws IOException {
        log().debug("Fetching current issue of " + getJournal().getTitle());
        URI url = getCurrentIssueUrl();
        Issue issue = new Issue();
        issue.setId(getIssueId(url));
        issue.setUrl(url);
        return fetchIssue(issue);
    }

    private IssueId getIssueId(URI url) {
        // http://www.nature.com/nchem/journal/v3/n1/
        Pattern p = Pattern.compile("www.nature.com/(\\S+)/journal/v(\\d+)/n(\\d+)/");
        Matcher m = p.matcher(url.toString());
        if (!m.find()) {
            throw new CrawlerRuntimeException("No match: "+url.toString());
        };
        return new IssueId("nature/"+m.group(1)+'/'+m.group(2)+'/'+m.group(3));
    }

    protected URI getCurrentIssueUrl() throws IOException {
        URI homepageUrl = getHomepageUrl();
        JournalId journalId = new JournalId("nature/"+getJournal().getAbbreviation());
        Document html = readHtml(homepageUrl, journalId, "home", AGE_1DAY);
        String href = XPathUtils.getString(html, ".//x:a[text() = 'Current issue table of contents']/@href");
        return homepageUrl.resolve(href);
    }

    private URI getHomepageUrl() {
        return URI.create("http://www.nature.com/" + getJournal().getAbbreviation() + "/");
    }

}