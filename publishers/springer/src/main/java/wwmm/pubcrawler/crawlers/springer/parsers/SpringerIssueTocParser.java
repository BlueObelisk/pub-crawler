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

package wwmm.pubcrawler.crawlers.springer.parsers;

import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Node;
import org.apache.log4j.Logger;
import wwmm.pubcrawler.CrawlerRuntimeException;
import wwmm.pubcrawler.parsers.AbstractIssueParser;
import wwmm.pubcrawler.parsers.IssueTocParser;
import wwmm.pubcrawler.model.FullTextResource;
import wwmm.pubcrawler.model.Issue;
import wwmm.pubcrawler.model.SupplementaryResource;
import wwmm.pubcrawler.model.id.ArticleId;
import wwmm.pubcrawler.model.id.IssueId;
import wwmm.pubcrawler.model.id.JournalId;
import wwmm.pubcrawler.types.Doi;
import wwmm.pubcrawler.utils.XPathUtils;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>The <code>AcsIssueCrawler</code> class provides a method for obtaining
 * information about all articles from a particular issue of a journal
 * published by the American Chemical Society.</p>
 *
 * @author Nick Day
 * @author Sam Adams
 * @version 2
 */
public class SpringerIssueTocParser extends AbstractIssueParser implements IssueTocParser {

    private static final Logger LOG = Logger.getLogger(SpringerIssueTocParser.class);
    private static final Pattern P_VOLUME_NUMBER = Pattern.compile("Volume (\\S+), (?:Numbers?|Supplement) (\\S+(?:\\s+-\\s*\\S)?) / .*? (\\d{4})");
    private static final Pattern P_ISSUE_LINK = Pattern.compile("/content/[^/]+/([^/]+)/([^/]+)/");

    public SpringerIssueTocParser(final Document html, final URI url, final JournalId journalId) {
        super(html, url, journalId);
    }

    @Override
    protected Logger log() {
        return LOG;
    }

    public String getIssn() {
        final String url = getUrl().toString();
        final Pattern p = Pattern.compile("springerlink\\.com/content/([^/]+)/");
        final Matcher m = p.matcher(url);
        m.find();
        return m.group(1);
    }

    @Override
    protected String findJournalTitle() {
        return XPathUtils.getString(getHtml(), "//x:h1[@class='title']").trim();
    }

    @Override
    protected List<Node> getArticleNodes() {
        return XPathUtils.queryHTML(getHtml(), "//x:li[contains(@class, 'journalArticle')]");
    }

    @Override
    protected ArticleId getArticleId(final Node articleNode) {
        final URI url = getArticleUrl(articleNode);
        final String s = url.toString();
        final int i0 = s.lastIndexOf('/');
        final int i1 = s.lastIndexOf('/', i0-1);
        return new ArticleId(getJournalId(), s.substring(i1+1, i0));
    }

    @Override
    protected Doi getArticleDoi(final Node articleNode) {
        return null;
    }

    @Override
    protected URI getArticleUrl(final Node context) {
        final String u = XPathUtils.getString(context, "x:p[@class='title']/x:a/@href");
        return getUrl().resolve(u);
    }

    @Override
    protected URI getArticleSupportingInfoUrl(final Node articleNode) {
        return null;
    }

    @Override
    protected String getArticleTitle(final Node context) {
        return XPathUtils.getString(context, "x:p[@class='title']").trim().replaceAll("\\s+", " ");
    }

    @Override
    protected String getArticleTitleHtml(final Node articleNode) {
        return null;
    }

    @Override
    protected List<String> getArticleAuthors(final Node context) {
        final List<Node> nodes = XPathUtils.queryHTML(context, "x:p[@class='authors']/x:a");
        final List<String> authors = new ArrayList<String>();
        for (final Node node : nodes) {
            authors.add(node.getValue().trim());
        }
        return authors;
    }

    @Override
    protected String findArticlePages(final Node context) {
        return XPathUtils.getString(context, "x:p[@class='contextTag']");
    }

    @Override
    protected List<SupplementaryResource> getArticleSupplementaryResources(final ArticleId articleId, final Node articleNode) {
        return null;
    }

    @Override
    protected List<FullTextResource> getArticleFullTextResources(final Node articleNode) {
        return null;
    }

    @Override
    public Issue getPreviousIssue() {
        return null;
    }

    @Override
    protected String findYear() {
        return getBib()[2];
    }

    @Override
    protected String findVolume() {
        return getBib()[0];
    }

    @Override
    protected String findNumber() {
        return getBib()[1];
    }

    protected String[] getBib() {
        // TODO Volume 48, Supplement 1 / January 2005
        final String s = XPathUtils.getString(getHtml(), "//x:h2[@class='filters']");
        final Matcher m = P_VOLUME_NUMBER.matcher(s);
        if (!m.find()) {
            throw new CrawlerRuntimeException("No match: "+s);
        }
        return new String[] {
                m.group(1), m.group(2), m.group(3)
        };
    }

    @Override
    public List<Issue> getIssueLinks() {
        final List<Issue> issues = new ArrayList<Issue>();
        final List<Node> nodes = XPathUtils.queryHTML(getHtml(), "//x:div[@class='accordion']/x:div[1]/x:ul//x:ul/x:li/x:a");
        for (final Node node : nodes) {
            final Element address = (Element) node;
            final String href = address.getAttributeValue("href");
            final Matcher m = P_ISSUE_LINK.matcher(href);
            if (m.matches()) {
                final String volume = m.group(1);
                final String number = m.group(2);
                issues.add(new Issue(new IssueId(getJournalId(), volume, number), getJournalTitle(), volume, number, null, getUrl().resolve(href)));
            } else {
                LOG.warn("Error matching issue link: " + href);
            }
        }
        return issues;
    }

    // TODO get Archive Issue Links

}
