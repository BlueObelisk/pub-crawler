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
    private JournalId journalId;

    public NatureIssueTocParser(final Document html, final URI url, final Journal journal) throws IOException {
        super(html, url);
        this.journalId = journal.getId();
    }

    @Override
    protected Logger log() {
        return LOG;
    }

    @Override
    public IssueId getIssueId() {
        return new IssueId(journalId, getVolume(), getNumber());
    }

    @Override
    public Issue getPreviousIssue() {
        String href = XPathUtils.getString(getHtml(), ".//x:a[text()='Previous']/@href");
        if (href == null) {
            return null;
        }
        Pattern p = Pattern.compile("/journal/v(\\d+)/n(\\d+)/");
        Matcher m = p.matcher(href);
        m.find();
        String volume = m.group(1);
        String number = m.group(2);
        Issue prev = new Issue();
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
    protected ArticleId getArticleId(Node articleNode, IssueId issueId) {
        Doi doi = getArticleDoi(null, articleNode);
        return new ArticleId(issueId, doi.getSuffix());
    }

    @Override
    protected Doi getArticleDoi(Article article, Node node) {
        String s = XPathUtils.getString(node, ".//x:span[@class='doi']");
        return new Doi(s);
    }

    @Override
    protected URI getArticleUrl(Article article, Node articleNode) {
        // TODO
        return null;
    }

    @Override
    protected URI getArticleSupportingInfoUrl(Article article, Node node) {
        String href = XPathUtils.getString(node, ".//x:a[text() = 'Supplementary information']/@href");
        return href == null ? null : getUrl().resolve(href);
    }

    @Override
    protected String getArticleTitle(Article article, Node node) {
        StringBuilder s = new StringBuilder();
        for (Node n : XPathUtils.queryHTML(node, "./x:h4/node()[./following-sibling::x:span[@class='hidden']]")) {
            s.append(n.getValue());
        }
        return s.toString().trim();
    }

    @Override
    protected String getArticleTitleHtml(Article article, Node node) {
        Element h = new Element("h1", XHtml.NAMESPACE);
        for (Node n : XPathUtils.queryHTML(node, "./x:h4/node()[./following-sibling::x:span[@class='hidden']]")) {
            h.appendChild(n.copy());
        }
        return HtmlUtil.writeAscii(new Document(h));
    }

    @Override
    protected List<String> getArticleAuthors(Article article, Node articleNode) {
        // TODO
        return null;
    }

    @Override
    protected Reference getArticleReference(Article article, Node articleNode) {
        // TODO
        return null;
    }

    @Override
    protected List<SupplementaryResource> getArticleSupplementaryResources(Article article, Node articleNode) {
        return null;
    }

    @Override
    protected List<FullTextResource> getArticleFullTextResources(Article article, Node articleNode) {
        // TODO
        return null;
    }

    private String getBiblio(int i) {
        String s = XPathUtils.getString(getHtml(), ".//x:h2[@class='issue']");
        if (s == null) {
            throw new CrawlerRuntimeException("not found");
        }
        // January 2011, Volume 3 No 1
        Pattern p = Pattern.compile("(\\d+), Volume (\\d+) No (\\d+)");
        Matcher m = p.matcher(s);
        if (!m.find()) {
            throw new CrawlerRuntimeException("No match: "+s);
        }
        return m.group(i);
    }

    public String getVolume() {
        return getBiblio(2);
    }

    public String getNumber() {
        return getBiblio(3);
    }

    public String getYear() {
        return getBiblio(1);
    }

    public String getJournalAbbreviation() {
        String s = getUrl().toString();
        Pattern p = Pattern.compile("nature.com/([^/]+)/journal/");
        Matcher m = p.matcher(s);
        m.find();
        return m.group(1);
    }


    @Override
    public Issue toIssue() {
        Issue issue = super.toIssue();
        issue.setUrl(getUrl());
        issue.setYear(getYear());
        issue.setVolume(getVolume());
        issue.setNumber(getNumber());
        return issue;
    }
    
}