/*
 * Copyright 2010-2011 Nick Day, Sam Adams
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package wwmm.pubcrawler.crawlers.springer;

import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Node;
import org.apache.log4j.Logger;
import wwmm.pubcrawler.CrawlerContext;
import wwmm.pubcrawler.crawlers.AbstractCrawler;
import wwmm.pubcrawler.model.Issue;
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
public class SpringerJournalIndexCrawler extends AbstractCrawler {

    private static final Logger LOG = Logger.getLogger(SpringerJournalIndexCrawler.class);

    private boolean fetchArchiveIndexes = false;

    private final Document html;
    private final URI url;
    private final String id;
    private static final Pattern P__VOLUME = Pattern.compile("Volume\\s+(\\S+)");
    private static final Pattern P_ISSUE = Pattern.compile("Numbers?\\s+(\\S+) .*? (\\d{4})");
    private static final Pattern P_SUPP = Pattern.compile("Supplement\\s+(\\S+) .*? (\\d{4})");

    public SpringerJournalIndexCrawler(CrawlerContext context, URI url, String id) throws IOException {
        super(context);
        this.id = id;
        this.html = fetchHtml(url, id+"/index.html");
        this.url = URI.create(html.getBaseURI());
    }

    protected Document fetchHtml(URI url, String id) throws IOException {
        return readHtml(url, id, AGE_1DAY);
    }


    protected Document getHtml() {
        return html;
    }

    @Override
    protected Logger log() {
        return LOG;
    }

    public List<Issue> listIssues() throws IOException {
        List<Issue> list = new ArrayList<Issue>();

        List<Node> volumeNodes = XPathUtils.queryHTML(getHtml(), "//x:li[x:a/x:span[starts-with(text(), 'Volume')]]");
        if (volumeNodes.isEmpty()) {
            log().warn("Unable to locate volume list in index "+this.url);
        } else {

            for (Node node : volumeNodes) {
                String s = XPathUtils.getString(node, "./x:a/x:span");
                Matcher m = P__VOLUME.matcher(s);
                if (!m.find()) {
                    log().warn("Unable to locate volume identifier: "+s);
                    continue;
                }
                String volume = m.group(1);
                findIssues(list, node, volume);
            }

        }

        return list;
    }

    private void findIssues(List<Issue> list, Node node, String volume) throws IOException {
        List<Node> issues = XPathUtils.queryHTML(node, "./x:ul/x:li");
        if (issues.isEmpty() && fetchArchiveIndexes) {
            findArchiveIssues(list, node, volume);
        } else {
            boolean first = true;
            for (Node n : issues) {
                String href = XPathUtils.getString(n, "./x:a/@href");
                // First is often current page, and therefore not clickable
                if (href == null) {
                    if (first) {
                        first = false;
                    } else {
                        log().warn("Unable to locate issue href in index "+this.url);
                    }
                    continue;
                }
                first = false;

                String s = XPathUtils.getString(n, "./x:a/x:span/text()");
                if (s == null) {
                    log().warn("Unable to locate issue descriptor in index "+this.url);
                }

                String number;
                String year;
                Matcher m1 = P_ISSUE.matcher(s);
                if (m1.find()) {
                    number = m1.group(1);
                    year = m1.group(2);
                } else {
                    m1 = P_SUPP.matcher(s);
                    if (m1.find()) {
                        number = "supp"+m1.group(1);
                        year = m1.group(2);
                    } else {
                        log().warn("Unable to locate issue identifiers: "+s);
                        continue;
                    }
                }



                URI url = this.url.resolve(href);
                Issue issue = new Issue();
                issue.setId(this.id+'/'+volume+'/'+number);
                issue.setUrl(url);
                issue.setVolume(volume);
                issue.setNumber(number);
                issue.setYear(year);

                list.add(issue);
            }
        }
    }

    private void findArchiveIssues(List<Issue> list, Node node, String volume) throws IOException {
        List<Node> issues;
        String href = XPathUtils.getString(node, "./x:a/@href");
        URI url = this.url.resolve(href);
        Document html = readHtml(url, this.id+"_"+volume+".html", AGE_MAX);

        issues = XPathUtils.queryHTML(html, "//x:p[@class='title']/x:a");
        for (Node n : issues) {
            Element addr = (Element) n;
            String h = addr.getAttributeValue("href");
            String s = addr.getValue();

            Matcher m1 = P_ISSUE.matcher(s);
            if (!m1.find()) {
                log().warn("Unable to locate issue identifiers: "+s);
                continue;
            }

            String number = m1.group(1);
            String year = m1.group(2);

            URI u = url.resolve(h);
            Issue issue = new Issue();
            issue.setId(this.id+'/'+volume+'/'+number);
            issue.setUrl(u);
            issue.setVolume(volume);
            issue.setNumber(number);
            issue.setYear(year);

            list.add(issue);
        }
    }

}
