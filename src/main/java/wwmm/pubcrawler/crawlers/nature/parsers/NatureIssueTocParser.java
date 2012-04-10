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
package wwmm.pubcrawler.crawlers.nature.parsers;

import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Node;
import org.apache.log4j.Logger;
import wwmm.pubcrawler.CrawlerRuntimeException;
import wwmm.pubcrawler.HtmlUtil;
import wwmm.pubcrawler.crawlers.AbstractIssueParser;
import wwmm.pubcrawler.model.*;
import wwmm.pubcrawler.model.id.ArticleId;
import wwmm.pubcrawler.model.id.IssueId;
import wwmm.pubcrawler.model.id.JournalId;
import wwmm.pubcrawler.types.Doi;
import wwmm.pubcrawler.utils.XHtml;
import wwmm.pubcrawler.utils.XPathUtils;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>The <code>NatureIssueParser</code> class provides a method for obtaining
 * information about all articles from a particular issue of a journal
 * published by the Nature Publishing Group.</p>
 *
 * @author Nick Day
 * @author Sam Adams
 * @version 2
 */
public class NatureIssueTocParser extends AbstractIssueParser {

    private static final Logger LOG = Logger.getLogger(NatureIssueTocParser.class);

    public NatureIssueTocParser(final Document html, final URI url, final JournalId journalId) throws IOException {
        super(html, url, journalId);
    }

    @Override
    protected Logger log() {
        return LOG;
    }

    @Override
    public Issue getPreviousIssue() {
        final String href = XPathUtils.getString(getHtml(), ".//x:a[text()='Previous']/@href");
        if (href == null) {
            return null;
        }
        final Pattern p = Pattern.compile("/journal/v(\\d+)/n(\\d+)/");
        final Matcher m = p.matcher(href);
        m.find();
        final String volume = m.group(1);
        final String number = m.group(2);
        final Issue prev = new Issue();
        prev.setId(new IssueId("nature/"+getJournalAbbreviation()+"/"+volume+"/"+number));
        prev.setUrl(getUrl().resolve(href));
        return prev;
    }

    @Override
    protected List<Node> getArticleNodes() {
        return XPathUtils.queryHTML(getHtml(), ".//x:div[@class='entry' or @class='compound']");
    }

//    @Override
//    protected Article initArticle(Node context, String issueId) {
//        Doi doi = getArticleDoi(context);
//        String articleId = issueId + '/' + doi.getSuffix();
//
//        Article article = new Article();
//        article.setId(articleId);
//        article.setDoi(doi);
//        article.setTitle(getArticleTitle(context));
//        article.setTitleHtml(getArticleTitleHtml(context));
//        article.setSupplementaryResourceUrl(getArticleSuppUrl(context));
//        return article;
//    }


    @Override
    protected ArticleId getArticleId(final Node articleNode, final IssueId issueId) {
        final Doi doi = getArticleDoi(null, articleNode);
        return new ArticleId(getJournalId(), doi.getSuffix());
    }

    @Override
    protected Doi getArticleDoi(final Article article, final Node node) {
        final String s = XPathUtils.getString(node, ".//x:span[@class='doi']");
        return new Doi(s);
    }

    @Override
    protected URI getArticleUrl(final Article article, final Node articleNode) {
        // TODO
        return null;
    }

    @Override
    protected URI getArticleSupportingInfoUrl(final Article article, final Node node) {
        final String href = XPathUtils.getString(node, ".//x:a[text() = 'Supplementary information']/@href");
        return href == null ? null : getUrl().resolve(href);
    }

    @Override
    protected String getArticleTitle(final Article article, final Node node) {
        final StringBuilder s = new StringBuilder();
        for (final Node n : XPathUtils.queryHTML(node, "./x:h4/node()[./following-sibling::x:span[@class='hidden']]")) {
            s.append(n.getValue());
        }
        return s.toString().trim();
    }

    @Override
    protected String getArticleTitleHtml(final Article article, final Node node) {
        final Element h = new Element("h1", XHtml.NAMESPACE);
        for (final Node n : XPathUtils.queryHTML(node, "./x:h4/node()[./following-sibling::x:span[@class='hidden']]")) {
            h.appendChild(n.copy());
        }
        return HtmlUtil.writeAscii(new Document(h));
    }

    @Override
    protected List<String> getArticleAuthors(final Article article, final Node articleNode) {
        // TODO
        return null;
    }

    @Override
    protected String findArticlePages(final Node articleNode) {
        // TODO
        return null;
    }

    @Override
    protected List<SupplementaryResource> getArticleSupplementaryResources(final Article article, final Node articleNode) {
        return null;
    }

    @Override
    protected List<FullTextResource> getArticleFullTextResources(final Article article, final Node articleNode) {
        // TODO
        return null;
    }

    private String getBiblio(final int i) {
        final String s = XPathUtils.getString(getHtml(), ".//x:h2[@class='issue']");
        if (s == null) {
            throw new CrawlerRuntimeException("not found");
        }
        // January 2011, Volume 3 No 1
        final Pattern p = Pattern.compile("(\\d+), Volume (\\d+) No (\\d+)");
        final Matcher m = p.matcher(s);
        if (!m.find()) {
            throw new CrawlerRuntimeException("No match: "+s);
        }
        return m.group(i);
    }

    @Override
    protected String findJournalTitle() {
        return XPathUtils.getString(getHtml(), "//x:span[@class='journal-name']");
    }

    @Override
    protected String findVolume() {
        return getBiblio(2);
    }

    @Override
    protected String findNumber() {
        return getBiblio(3);
    }

    @Override
    protected String findYear() {
        return getBiblio(1);
    }

    public String getJournalAbbreviation() {
        final String s = getUrl().toString();
        final Pattern p = Pattern.compile("nature.com/([^/]+)/journal/");
        final Matcher m = p.matcher(s);
        m.find();
        return m.group(1);
    }

}
