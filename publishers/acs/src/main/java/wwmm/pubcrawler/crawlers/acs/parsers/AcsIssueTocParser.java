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
package wwmm.pubcrawler.crawlers.acs.parsers;

import nu.xom.*;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import wwmm.pubcrawler.crawlers.AbstractIssueParser;
import wwmm.pubcrawler.crawlers.IssueTocParser;
import wwmm.pubcrawler.crawlers.acs.AcsTools;
import wwmm.pubcrawler.model.FullTextResource;
import wwmm.pubcrawler.model.Issue;
import wwmm.pubcrawler.model.IssueBuilder;
import wwmm.pubcrawler.model.SupplementaryResource;
import wwmm.pubcrawler.model.id.ArticleId;
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
 * @version 2.0
 */
public class AcsIssueTocParser extends AbstractIssueParser implements IssueTocParser {

    private static final Logger LOG = Logger.getLogger(AcsIssueTocParser.class);
    
    private static final Pattern PREV_URI_PATTERN = Pattern.compile("http://pubs.acs.org/toc/\\w+/(\\w+)/(\\w+)");

    private static final Pattern P_VOLUME = Pattern.compile("Volume (\\d+)");
    private static final Pattern P_NUMBER = Pattern.compile("Issue (\\d+)");
    private static final Pattern P_YEAR = Pattern.compile("\\b(\\d{4})\\b");

    public AcsIssueTocParser(final Document html, final URI url, final JournalId journalId) {
        super(html, url, journalId);
    }

    @Override
    protected Logger log() {
        return LOG;
    }

    @Override
    protected String findJournalTitle() {
        final String s = XPathUtils.getString(getHtml(), "/x:html/x:head/x:title");
        return s.substring(0, s.indexOf(':')).trim();
    }

    @Override
    protected String findVolume() {
        final String text = XPathUtils.getString(getHtml(), ".//x:div[@id='tocMeta']/x:div[2]");
        final Matcher m = P_VOLUME.matcher(text);
        if (m.find()) {
            return m.group(1);
        }
        throw new RuntimeException("Unable to find volume: " + text);
    }

    @Override
    protected String findNumber() {
        final String text = XPathUtils.getString(getHtml(), ".//x:div[@id='tocMeta']/x:div[2]");
        final Matcher m = P_NUMBER.matcher(text);
        if (m.find()) {
            return m.group(1);
        }
        throw new RuntimeException("Unable to find number: " + text);
    }

    @Override
    protected String findYear() {
        final String text = getDateBlock();
        final Matcher m = P_YEAR.matcher(text);
        if (m.find()) {
            return m.group(1);
        }
        log().warn("Unable to locate year: "+text);
        return null;
    }

    @Override
    public Issue getPreviousIssue() {
        final Attribute prev = (Attribute) XPathUtils.getNode(getHtml(), ".//x:div[@id='issueNav']/x:a[@class='previous']/@href");
        if (prev != null) {
            final String href = prev.getValue();
            if (href.startsWith("/toc/"+getJournalAbbreviation())) {
                return parsePreviousIssue(href);
            }
        }
        return null;
    }

    private Issue parsePreviousIssue(final String href) {
        final URI url = getUrl().resolve(href);
        final Matcher matcher = PREV_URI_PATTERN.matcher(url.toString());
        if (matcher.find()) {
            final String volume = matcher.group(1);
            final String number = matcher.group(2);
            return new IssueBuilder()
                .withJournalId(getJournalId())
                .withVolume(volume)
                .withNumber(number)
                .withUrl(url)
                .build();
        }
        LOG.warn("Error matching previous issue: " + href);
        return null;
    }

    @Override
    protected List<Node> getArticleNodes() {
        return XPathUtils.queryHTML(getHtml(), ".//x:div[@class='articleBox']");
    }

    @Override
    protected ArticleId getArticleId(final Node articleNode) {
        final Doi doi = getArticleDoi(articleNode);
        return new ArticleId(getJournalId(), doi.getSuffix());
    }

    @Override
    protected Doi getArticleDoi(final Node node) {
        final String s = XPathUtils.getString(node, ".//x:input[@name='doi']/@value");
        return new Doi(s);
    }

    @Override
    protected URI getArticleUrl(final Node articleNode) {
        final String s = XPathUtils.getString(articleNode, "./x:div[@class='articleLinksIcons']//x:a[@class = 'articleLink'][1]/@href");
        return getUrl().resolve(s);
    }

    @Override
    protected URI getArticleSupportingInfoUrl(final Node articleNode) {
        final String s = XPathUtils.getString(articleNode, "./x:div[@class='articleLinksIcons']//x:a[text() = 'Supporting Info']/@href");
        if (s != null) {
            return getUrl().resolve(s);
        }
        return null;
    }

    @Override
    protected String getArticleTitle(final Node articleNode) {
        // Additions and Corrections have no title
        // e.g. http://pubs.acs.org/toc/inocaj/39/19
        final ParentNode source = (ParentNode) XPathUtils.getNode(articleNode, ".//x:h2/x:a");
        if (source != null) {
            return source.getValue();
        }
        return null;

    }

    @Override
    protected String getArticleTitleHtml(final Node articleNode) {
        // Additions and Corrections have no title
        // e.g. http://pubs.acs.org/toc/inocaj/39/19
        final ParentNode source = (ParentNode) XPathUtils.getNode(articleNode, ".//x:h2/x:a");
        if (source != null) {
            final Element title = new Element("h1", "http://www.w3.org/1999/xhtml");
            AcsTools.copyChildren(source, title);
            return AcsTools.toHtml(title);
        }
        return null;
    }

    @Override
    protected List<String> getArticleAuthors(final Node node) {
        final List<String> authors = new ArrayList<String>();
        final Element names = (Element) XPathUtils.getNode(node, ".//x:div[@class='articleAuthors']");
        if (names != null) {
            final Element copy = (Element) names.copy();
            AcsTools.normaliseImages(copy);
            for (final String name : copy.getValue().split(" and |, (and )?")) {
                authors.add(name.trim());
            }
        }
        return authors;

    }

    @Override
    protected List<SupplementaryResource> getArticleSupplementaryResources(final ArticleId articleId, final Node articleNode) {
        return null;
    }

    @Override
    protected List<FullTextResource> getArticleFullTextResources(final Node articleNode) {
        // TODO
        return null;
    }

    @Override
    protected String findArticlePages(final Node node) {
        String pages = XPathUtils.getString(node, ".//x:div[starts-with(text(), 'pp ')]");
        if (pages != null) {
            return pages.substring(3);
        }
        pages = XPathUtils.getString(node, ".//x:div[starts-with(text(), 'p ')]");
        if (pages != null) {
            return pages.substring(2);
        }
        // Some articles have no page numbers in TOC:
        // http://pubs.acs.org/toc/chreay/110/6
        log().warn("Unable to find article pages: "+getIssueId());
        return null;
    }


    public LocalDate getDate() {
        final String text = getDateBlock();
        final DateTime dt;
        if (text.contains(", ")) {
            dt = DateTimeFormat.forPattern("MMMM d, yyyy").parseDateTime(text);
        } else {
            dt = DateTimeFormat.forPattern("MMMM yyyy").parseDateTime(text);
        }
        return new LocalDate(dt);
    }

    private String getJournalAbbreviation() {
        final String s = getUrl().toString();
        final Pattern p = Pattern.compile("pubs.acs.org/toc/([^/]+)/");
        final Matcher m = p.matcher(s);
        m.find();
        return m.group(1);
    }

    private String getDateBlock() {
        return XPathUtils.getString(getHtml(), ".//x:div[@id='tocMeta']/x:div[@id='date']");
    }

}
