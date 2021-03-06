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

package wwmm.pubcrawler.crawlers.springer.parsers;

import nu.xom.Document;
import nu.xom.Node;
import org.apache.log4j.Logger;
import wwmm.pubcrawler.model.IssueLink;
import wwmm.pubcrawler.model.IssueLinkBuilder;
import wwmm.pubcrawler.model.id.JournalId;
import wwmm.pubcrawler.parsers.IssueListParser;
import wwmm.pubcrawler.utils.XPathUtils;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Sam Adams
 */
public class SpringerIssueListParser implements IssueListParser {

    private static final Logger LOG = Logger.getLogger(SpringerIssueListParser.class);

    private boolean fetchArchiveIndexes = false;

    private final Document html;
    private final URI url;
    private final JournalId journalId;
    
    private static final Pattern P__VOLUME = Pattern.compile("Volume\\s+(\\S+)");
    private static final Pattern P_ISSUE = Pattern.compile("Numbers?\\s+(\\S+) .*? (\\d{4})");
    private static final Pattern P_SUPP = Pattern.compile("Supplement\\s+(\\S+) .*? (\\d{4})");

    public SpringerIssueListParser(final JournalId journalId, final Document html, final URI url) {
        this.journalId = journalId;
        this.html = html;
        this.url = url;
    }

    @Override
    public List<IssueLink> findIssues() {
        final List<IssueLink> list = new ArrayList<IssueLink>();

        final List<Node> volumeNodes = XPathUtils.queryHTML(html, "//x:li[x:a/x:span[starts-with(text(), 'Volume')]]");
        if (volumeNodes.isEmpty()) {
            LOG.warn("Unable to locate volume list in index "+this.url);
        } else {

            for (final Node node : volumeNodes) {
                final String s = XPathUtils.getString(node, "./x:a/x:span");
                final Matcher m = P__VOLUME.matcher(s);
                if (!m.find()) {
                    LOG.warn("Unable to locate volume identifier: "+s);
                    continue;
                }
                final String volume = m.group(1);
                findIssues(list, node, volume);
            }

        }

        return list;
    }

    private void findIssues(final List<IssueLink> list, final Node node, final String volume) {
        final List<Node> issues = XPathUtils.queryHTML(node, "./x:ul/x:li");
        boolean first = true;
        for (final Node n : issues) {
            final String href = XPathUtils.getString(n, "./x:a/@href");
            // First is often current page, and therefore not clickable
            if (href == null) {
                if (first) {
                    first = false;
                } else {
                    LOG.warn("Unable to locate issue href in index "+this.url);
                }
                continue;
            }
            first = false;

            final String s = XPathUtils.getString(n, "./x:a/x:span/text()");
            if (s == null) {
                LOG.warn("Unable to locate issue descriptor in index "+this.url);
            }

            final String number;
            final String year;
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
                    LOG.warn("Unable to locate issue identifiers: "+s);
                    continue;
                }
            }

            final URI url = this.url.resolve(href);

            final IssueLink issue = new IssueLinkBuilder()
                .withJournalId(journalId)
                .withVolume(volume)
                .withNumber(number)
                .withUrl(url)
                .build();

            list.add(issue);
        }
    }

}
