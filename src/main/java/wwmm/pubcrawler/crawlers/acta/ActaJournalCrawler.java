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
import org.apache.log4j.Logger;
import wwmm.pubcrawler.CrawlerContext;
import wwmm.pubcrawler.CrawlerRuntimeException;
import wwmm.pubcrawler.DefaultCrawlerContext;
import wwmm.pubcrawler.crawlers.AbstractJournalCrawler;
import wwmm.pubcrawler.model.Journal;
import wwmm.pubcrawler.model.Issue;
import wwmm.pubcrawler.utils.XPathUtils;
import wwmm.pubcrawler.journals.ActaJournalIndex;

import java.io.IOException;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Sam Adams
 */
public class ActaJournalCrawler extends AbstractJournalCrawler {

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
        log().debug("Fetching current issue of " + getJournal().getFullTitle());
        URI url = getCurrentIssueUrl();
        log().debug("current issue URL: "+url);
        Issue issue = new Issue();
        issue.setId(getIssueId(url));
        issue.setYear(getIssueYear(url));
        issue.setUrl(url);
        return fetchIssue(issue);
    }

    private String getIssueYear(URI url) {
        // http://journals.iucr.org/e/issues/2011/01/00/issconts.html
        Pattern p = Pattern.compile("/issues/(\\d+)/(\\d+)/(\\d+)/");
        Matcher m = p.matcher(url.toString());
        if (!m.find()) {
            throw new CrawlerRuntimeException("No match: "+url);
        }
        return m.group(1);
    }

    private String getIssueId(URI url) {
        // http://journals.iucr.org/e/issues/2011/01/00/issconts.html
        Pattern p = Pattern.compile("/issues/(\\d+)/(\\d+)/(\\d+)/");
        Matcher m = p.matcher(url.toString());
        if (!m.find()) {
            throw new CrawlerRuntimeException("No match: "+url);
        }
        return "acta/"+getJournal().getAbbreviation()+'/'+m.group(1)+'/'+m.group(2)+'-'+m.group(3);
    }

    private URI getCurrentIssueUrl() throws IOException {
        URI url = getHomepageUrl();
        Document html = readHtml(url, "acta/"+getJournal().getAbbreviation()+"_home.html", AGE_1DAY);
        String href = XPathUtils.getString(html, ".//x:a[.='Current issue']/@href");
        URI frameUri = url.resolve(href);
        return frameUri.resolve("./isscontsbdy.html");
    }

    private URI getHomepageUrl() {
        return URI.create("http://journals.iucr.org/" + getJournal().getAbbreviation() + "/journalhomepagebdy.html");
    }

    public static void main(String[] args) throws IOException {

        for (Journal journal : ActaJournalIndex.getIndex().values()) {
            CrawlerContext context = new DefaultCrawlerContext(new ActaCrawlerFactory());
            ActaJournalCrawler crawler = new ActaJournalCrawler(journal, context);
            crawler.crawlJournal();
        }

    }

}
