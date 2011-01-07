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

import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Node;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import wwmm.pubcrawler.CrawlerContext;
import wwmm.pubcrawler.CrawlerRuntimeException;
import wwmm.pubcrawler.crawlers.AbstractCrawler;
import wwmm.pubcrawler.data.DataStore;
import wwmm.pubcrawler.model.Journal;
import wwmm.pubcrawler.model.Issue;
import wwmm.pubcrawler.utils.XPathUtils;
import wwmm.pubcrawler.journals.AcsJournalIndex;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Sam Adams
 */
public class AcsIndexCrawler extends AbstractCrawler {

private static final Logger LOG = Logger.getLogger(AcsIndexCrawler.class);

    private Document html;
    private URI url;
    private Journal journal;

    public AcsIndexCrawler(Journal journal, CrawlerContext context) throws IOException {
        super(context);
        this.journal = journal;
        this.html = fetchHtml();
        this.url = URI.create(html.getBaseURI());
    }


    @Override
    protected Logger log() {
        return LOG;
    }

    public Document getHtml() {
        return html;
    }

    public URI getUrl() {
        return url;
    }

    public Journal getJournal() {
        return journal;
    }


    private Document fetchHtml() throws IOException {
        URI url = URI.create("http://pubs.acs.org/loi/"+getJournal().getAbbreviation());
        return readHtml(url, "acs/"+getJournal().getAbbreviation()+"_index.html", AGE_1DAY);
    }

    public List<Issue> getIssues() {
        // December 29, 2010 (Volume 132, Issue 51, pp. 17977�18430)
        Pattern p = Pattern.compile("(\\d{4}) \\(Volume (\\d+), Issue (\\d+)");

        // get volumes
        // fetch issue list:
        // http://pubs.acs.org/action/showPage?page=/widgets/volume_ul.jsp&journalCode=bichaw&volume=12
        
        List<Issue> issues = new ArrayList<Issue>();
        List<Node> nodes = XPathUtils.queryHTML(getHtml(), ".//x:ul[contains(@class,'volumes')]//x:ul//x:a");
        for (Node node : nodes) {
            Element address = (Element) node;
            Matcher m = p.matcher(address.getValue());
            if (!m.find()) {
                throw new CrawlerRuntimeException("No match: "+address.getValue());
            }
            String year = m.group(1);
            String volume = m.group(2);
            String number = m.group(3);
            String href = address.getAttributeValue("href");
            URI url = getUrl().resolve(href);

            Issue issue = new Issue();
            issue.setId(generateIssueId(volume, number));
            issue.setYear(year);
            issue.setVolume(volume);
            issue.setNumber(number);
            issue.setUrl(url);
            issues.add(issue);
        }

        return issues;
    }

    private String generateIssueId(String volume, String number) {
        return "acs/"+getJournal().getAbbreviation()+'/'+volume+'/'+number;
    }


    public static void main(String[] args) throws IOException {
        HttpClient client = new DefaultHttpClient();
        DataStore store = new DataStore(new File("data/"));
        CrawlerContext context = new CrawlerContext(store, null, new AcsCrawlerFactory());

        for (Issue issue : new AcsIndexCrawler(AcsJournalIndex.BIOCHEMISTRY, context).getIssues()) {
            System.out.println(issue.getId()+"\t"+issue.getUrl());
        }
    }

}
