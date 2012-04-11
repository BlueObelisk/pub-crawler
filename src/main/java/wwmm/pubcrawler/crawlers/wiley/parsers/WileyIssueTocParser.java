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

package wwmm.pubcrawler.crawlers.wiley.parsers;

import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Node;
import org.apache.log4j.Logger;
import wwmm.pubcrawler.CrawlerRuntimeException;
import wwmm.pubcrawler.crawlers.AbstractIssueParser;
import wwmm.pubcrawler.crawlers.IssueTocParser;
import wwmm.pubcrawler.model.Article;
import wwmm.pubcrawler.model.FullTextResource;
import wwmm.pubcrawler.model.Issue;
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
 * @author Sam Adams
 */
public class WileyIssueTocParser extends AbstractIssueParser implements IssueTocParser {

    private static final Logger LOG = Logger.getLogger(WileyIssueTocParser.class);
    private static final Pattern P_PAGES = Pattern.compile("\\(pages? ([^)]+)\\)");

    public WileyIssueTocParser(final Document html, final URI url, final JournalId journalId) {
        super(html, url, journalId);
    }

    @Override
    protected Logger log() {
        return LOG;
    }

    @Override
    protected String findJournalTitle() {
        return XPathUtils.getString(getHtml(), "//x:h1[@id='productTitle']").trim();
    }

    @Override
    protected String findVolume() {
        return XPathUtils.getString(getHtml(), "//x:span[@class='issueTocVolume']").substring(7);
    }

    @Override
    protected String findNumber() {
        return XPathUtils.getString(getHtml(), "//x:span[@class='issueTocIssue']").substring(6);
    }

    @Override
    protected String findYear() {
        final String s = XPathUtils.getString(getHtml(), "//x:div[@id='metaData']/x:h2").trim();
        return s.substring(s.length()-4);
    }

    @Override
    protected List<Node> getArticleNodes() {
        return XPathUtils.queryHTML(getHtml(), "//x:ol[@id='issueTocGroups']//x:ol[@class='articles']/x:li");
    }

    @Override
    protected ArticleId getArticleId(final Node articleNode) {
        final Doi doi = getArticleDoi(articleNode);
        return new ArticleId(getJournalId(), doi.getSuffix());
    }

    @Override
    protected Doi getArticleDoi(final Node articleNode) {
        final String s = XPathUtils.getString(articleNode, ".//x:input[@name='doi']/@value");
        if (s == null) {
            throw new CrawlerRuntimeException("Unable to find DOI in issue: "+getIssueId());
        }
        return new Doi(s);
    }

    @Override
    protected URI getArticleUrl(final Node articleNode) {
        final Element address = (Element) XPathUtils.getNode(articleNode, "x:div[contains(@class, 'tocArticle')]/x:a[1]");
        if (address != null) {
            return getUrl().resolve(address.getAttributeValue("href"));
        }
        return null;
    }

    @Override
    protected URI getArticleSupportingInfoUrl(final Node articleNode) {
        // TODO
        return null;
    }

    @Override
    protected String getArticleTitle(final Node articleNode) {
        final String s = XPathUtils.getString(articleNode, "x:div[contains(@class, 'tocArticle')]/x:a");
        if (s != null) {
            if (s.contains("(page")) {
                return s.substring(0, s.indexOf("(page")).trim();
            }
        }
        return s == null ? null : s.trim();
    }

    @Override
    protected String getArticleTitleHtml(final Node articleNode) {
        // TODO
        return null;
    }

    @Override
    protected List<String> getArticleAuthors(final Node articleNode) {
        final String s = XPathUtils.getString(articleNode, "x:div[contains(@class, 'tocArticle')]/x:p[1]");
        if (s != null && ! s.contains("DOI:")) {
            final List<String> authors = new ArrayList<String>();
            for (final String author : s.split(", | and ")) {
                authors.add(author.trim());
            }
            return authors;
        }
        return null;
    }

    @Override
    protected String findArticlePages(final Node articleNode) {
        final String s = XPathUtils.getString(articleNode, "x:div[contains(@class, 'tocArticle')]/x:a");
        if (s != null) {
            final Matcher m = P_PAGES.matcher(s);
            if (m.find()) {
                return m.group(1).trim().replace('\u2013', '-');
            }
        }
        return null;
    }

    @Override
    protected List<SupplementaryResource> getArticleSupplementaryResources(final Article article, final Node articleNode) {
        // TODO
        return null;
    }

    @Override
    protected List<FullTextResource> getArticleFullTextResources(final Node articleNode) {
        // TODO
        return null;
    }

    @Override
    public Issue getPreviousIssue() {
        final String href = XPathUtils.getString(getHtml(), "//x:a[@id='previousLink']/@href");
        if (href != null) {
            return new WileyPreviousIssueLinkHandler(getJournalId(), getJournalTitle(), getUrl()).parse(href);
        }
        return null;
    }

}
