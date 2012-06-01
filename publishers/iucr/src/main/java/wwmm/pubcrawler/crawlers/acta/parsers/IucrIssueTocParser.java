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
import wwmm.pubcrawler.HtmlUtil;
import wwmm.pubcrawler.parsers.AbstractIssueTocParser;
import wwmm.pubcrawler.crawlers.acta.ActaUtil;
import wwmm.pubcrawler.crawlers.acta.Iucr;
import wwmm.pubcrawler.model.FullTextResource;
import wwmm.pubcrawler.model.Issue;
import wwmm.pubcrawler.model.SupplementaryResource;
import wwmm.pubcrawler.model.id.ArticleId;
import wwmm.pubcrawler.model.id.IssueId;
import wwmm.pubcrawler.model.id.JournalId;
import wwmm.pubcrawler.types.Doi;
import wwmm.pubcrawler.utils.XPathUtils;

import java.net.URI;
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
 * @version 2.0
 */
public class IucrIssueTocParser extends AbstractIssueTocParser {

    private static final Logger LOG = Logger.getLogger(IucrIssueTocParser.class);

    private static final Pattern P_PAGES = Pattern.compile("\\d+(-\\d+)?");

    private final Document headHtml;
    private final URI headerUrl;

    public IucrIssueTocParser(final Document bodyHtml, final Document headHtml, final JournalId journalId) {
        super(bodyHtml, URI.create(bodyHtml.getBaseURI()), journalId);
        this.headHtml = headHtml;
        this.headerUrl = URI.create(headHtml.getBaseURI());
    }

    @Override
    protected Logger log() {
        return LOG;
    }

    @Override
    protected String findJournalTitle() {
        Node sectionTitle = XPathUtils.getNode(getHtml(), "//x:h1");
        Node actaTitle = XPathUtils.getNode(sectionTitle, "preceding-sibling::x:h3[1]");
        if (actaTitle == null) {
            return sectionTitle.getValue().trim();
        }
        return actaTitle.getValue().trim() + ": " + sectionTitle.getValue().trim();
    }

    @Override
    public Issue getPreviousIssue() {
        final String href = XPathUtils.getString(headHtml, ".//x:a[./x:img/@alt='Previous']/@href");
        if (href == null) {
            return null;
        }
        URI url = headerUrl.resolve(href);
        url = url.resolve("./isscontsbdy.html");
        final Issue issue = new Issue();
        issue.setId(getIssueId(url));
        issue.setUrl(url);
        return issue;
    }

    @Override
    protected List<Node> getArticleNodes() {
        final List<Node> nodes = XPathUtils.queryHTML(getHtml(), ".//x:div[contains(@class, 'toc') and contains(@class, 'entry')]");
        if (nodes.isEmpty()) {
            List<Node> xx = XPathUtils.queryHTML(getHtml(), ".//x:a[@id]");
            for (final Node n : xx) {
                final Element node = new Element("foo");
                final String id = ((Element)n).getAttributeValue("id");
                if (id.length() == 6) {
                    for (final Node x : XPathUtils.queryHTML(n, "./following-sibling::*")) {
                        final Element e = (Element) x;
                        if ("hr".equals(e.getLocalName())) {
                            break;
                        }
                        node.appendChild(x.copy());
                    }
                    nodes.add(node);
                }
            }
            if (nodes.isEmpty()) {
                // Handle e.g. http://journals.iucr.org/c/issues/1997/03/00/issconts.html
                xx = XPathUtils.queryHTML(getHtml(), ".//x:div[@class='buttonlinks']");
                for (final Node n : xx) {
                    final Element node = new Element("foo");
                    node.appendChild(n.copy());
                    for (final Node x : XPathUtils.queryHTML(n, "./following-sibling::*")) {
                        final Element e = (Element) x;
                        if ("div".equals(e.getLocalName()) && "buttonlinks".equals(e.getAttributeValue("class"))) {
                            break;
                        }
                        node.appendChild(x.copy());
                    }
                    nodes.add(node);
                }
            }
        }
        return nodes;
    }

    @Override
    protected ArticleId getArticleId(final Node node) {
        final String idString = XPathUtils.getString(node, ".//x:a[./x:img[contains(@alt, 'pdf version') or contains(@alt, 'PDF version')]]/@href");
        if (idString == null) {
            throw new CrawlerRuntimeException("Unable to locate PDF file:\n"+node.toXML(), getIssueId(), getUrl());
        }
        final Pattern p = Pattern.compile("/([^/]+)/[^/]+\\.pdf");
        final Matcher m = p.matcher(idString);
        if (!m.find()) {
            throw new CrawlerRuntimeException("No match: "+idString, getIssueId(), getUrl());
        }
        return new ArticleId(getJournalId(), m.group(1));
    }

    @Override
    protected Doi getArticleDoi(final Node node) {
        String doi = XPathUtils.getString(node, ".//x:font[@size='2' and contains(.,'doi:10.1107/')]");
        if (doi == null) {
            doi = XPathUtils.getString(node, ".//x:a[contains(@href,'dx.doi.org/10.1107/')]");
        }
        if (doi == null) {
            // e.g. http://journals.iucr.org/b/issues/2006/02/00/issconts.html
            doi = XPathUtils.getString(node, ".//x:span[starts-with(text(), 'doi:')]");
        }
        if (doi == null) {
            throw new CrawlerRuntimeException("Unable to locate DOI in issue: "+getIssueId(), getIssueId(), getUrl());
        }
        return new Doi(doi);
    }

    @Override
    protected URI getArticleUrl(final Node articleNode) {
        // TODO
        return null;
    }

    @Override
    protected URI getArticleSupportingInfoUrl(final Node articleNode) {
        return null;
    }

    @Override
    protected String getArticleTitle(final Node node) {
        final Node heading = XPathUtils.getNode(node, ".//x:h3[normalize-space(.) != \"\"][1]");
        final Element copy = (Element) heading.copy();
        ActaUtil.normaliseHtml(copy);
        return copy.getValue();
    }

    @Override
    protected String getArticleTitleHtml(final Node node) {
        final Node heading = XPathUtils.getNode(node, "./x:h3[1]");
        final Element copy = (Element) heading.copy();
        copy.setLocalName("h1");
        ActaUtil.normaliseHtml(copy);
        final Document doc = new Document(copy);
        final String s = HtmlUtil.writeAscii(doc);
        return s;
    }

    @Override
    protected List<String> getArticleAuthors(final Node articleNode) {
        final List<String> authors = XPathUtils.getStrings(articleNode, "./x:h3/x:a[contains(@href, \"http://scripts.iucr.org/cgi-bin/citedin\")]");
        return authors;
    }

    @Override
    protected String findArticlePages(final Node articleNode) {
        final List<Node> nodes = XPathUtils.queryHTML(articleNode, ".//x:p[x:i]/x:b/following-sibling::text()[1]");
        if (!nodes.isEmpty()) {
            final String s = nodes.get(0).getValue();
            final Matcher m = P_PAGES.matcher(s);
            if (m.find()) {
                return m.group(0);
            }
        }
        return null;
    }

    @Override
    protected List<SupplementaryResource> getArticleSupplementaryResources(final ArticleId articleId, final Node context) {
        IucrSuppInfoParser suppInfoParser = new IucrSuppInfoParser(articleId, getUrl(), context);
        return suppInfoParser.getSupplementaryResources();
    }

    @Override
    protected List<FullTextResource> getArticleFullTextResources(final Node articleNode) {
        // TODO
        return null;
    }

    private IssueId getIssueId(final URI url) {
        // http://journals.iucr.org/e/issues/2011/01/00/isscontsbdy.html
        final Pattern p = Pattern.compile("journals.iucr.org/([^/]+)/issues/(\\d+)/(\\w+)/(\\w+)");
        final Matcher m = p.matcher(url.toString());
        if (!m.find()) {
            throw new CrawlerRuntimeException("Unable to parse URL: "+url, getIssueId(), getUrl());
        }
        final JournalId journalId = new JournalId(Iucr.PUBLISHER_ID, m.group(1));
        return new IssueId(journalId, m.group(2), m.group(3) + '-' + m.group(4));
    }


    private String getBib(final int i) {
        // Volume 66, Part 1 (February 2010)
        final String s = XPathUtils.getString(getHtml(), ".//x:h3[contains(., 'Volume') and contains(., 'Part')]");
        if (s == null) {
            throw new CrawlerRuntimeException("Volume info not found", getIssueId(), getUrl());
        }
        final Pattern p = Pattern.compile("Volume (\\d+), Part (\\d+) .*\\(\\S+ (\\d+)\\)");
        final Matcher m = p.matcher(s);
        if (!m.find()) {
            throw new CrawlerRuntimeException("No match: "+s, getIssueId(), getUrl());
        }
        return m.group(i);
    }

    @Override
    protected String findVolume() {
        return getBib(1);
    }

    @Override
    protected String findNumber() {
        return getBib(2);
    }

    @Override
    protected String findYear() {
        return getBib(3);
    }
}
