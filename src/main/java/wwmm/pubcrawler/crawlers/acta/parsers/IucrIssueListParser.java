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
package wwmm.pubcrawler.crawlers.acta.parsers;

import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Node;
import org.apache.log4j.Logger;
import wwmm.pubcrawler.CrawlerRuntimeException;
import wwmm.pubcrawler.crawlers.IssueListParser;
import wwmm.pubcrawler.model.Issue;
import wwmm.pubcrawler.model.id.IssueId;
import wwmm.pubcrawler.model.id.JournalId;
import wwmm.pubcrawler.utils.XPathUtils;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Sam Adams
 */
public class IucrIssueListParser implements IssueListParser {

    private static final Logger LOG = Logger.getLogger(IucrIssueListParser.class);

    private final Document html;
    private final URI url;
    private final JournalId journalId;

    public IucrIssueListParser(final Document html, final URI url, final JournalId journalId) {
        this.journalId = journalId;
        this.url = url;
        this.html = html;
    }

    @Override
    public List<Issue> findIssues() {
        final IucrIssueListVolumeNumberParser volumeNumberParser = IucrIssueListVolumeNumberParser.getInstance();

        final List<Node> nodes = XPathUtils.queryHTML(html, ".//x:li[x:img]/x:a");
        final List<Issue> issues = new ArrayList<Issue>();
        for (final Node node : nodes) {
            final Element element = (Element) node;
            final String href = element.getAttributeValue("href");
            if (href.contains("/issues/")) {
                final URI url = this.url.resolve(href);
                final Issue issue = new Issue();
                issue.setId(getIssueId(url));
                issue.setVolume(volumeNumberParser.getVolume(node.getValue()));
                issue.setNumber(volumeNumberParser.getNumber(node.getValue()));
                issue.setYear(getIssueYear(url));
                issue.setUrl(url);
                issues.add(issue);
            }
        }
        return issues;
    }

    private String getIssueYear(final URI url) {
        // http://journals.iucr.org/e/issues/2011/01/00/issconts.html
        final Pattern p = Pattern.compile("/issues/(\\d+)/");
        final Matcher m = p.matcher(url.toString());
        if (!m.find()) {
            throw new CrawlerRuntimeException("No match: "+url);
        }
        return m.group(1);
    }

    private IssueId getIssueId(final URI url) {
        // http://journals.iucr.org/e/issues/2011/01/00/issconts.html
        final Pattern p = Pattern.compile("/issues/(\\w+)/(\\w+)/(\\w+)/");
        final Matcher m = p.matcher(url.toString());
        if (!m.find()) {
            throw new CrawlerRuntimeException("No match: "+url);
        }
        return new IssueId(journalId, m.group(1), m.group(2)+'-'+m.group(3));
    }

}
