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

import nu.xom.*;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import wwmm.pubcrawler.CrawlerContext;
import wwmm.pubcrawler.crawlers.AbstractIssueCrawler;
import wwmm.pubcrawler.model.*;
import wwmm.pubcrawler.model.id.ArticleId;
import wwmm.pubcrawler.model.id.IssueId;
import wwmm.pubcrawler.types.Doi;
import wwmm.pubcrawler.utils.XPathUtils;

import java.io.IOException;
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
public class AcsIssueCrawler extends AbstractIssueCrawler {

    private static final Logger LOG = Logger.getLogger(AcsIssueCrawler.class);

    private String journalTitle;
    private String volume;
    private String number;

    /**
	 * <p>Creates an instance of the AcsIssueCrawler class and
	 * specifies the issue to be crawled.</p>
	 *
	 * @param issue the issue to be crawled.
	 */
    public AcsIssueCrawler(Issue issue, CrawlerContext context) throws IOException {
        super(issue, context);
        this.journalTitle = getJournalTitle();
        this.volume = getVolume();
        this.number = getNumber();
    }

    @Override
    protected Logger log() {
        return LOG;
    }

    @Override
    protected Document fetchHtml(Issue issue) throws IOException {
        if (issue.isCurrent()) {
            return readHtml(issue.getUrl(), issue.getId(), AGE_1DAY);
        } else {
            return readHtml(issue.getUrl(), issue.getId(), AGE_MAX);
        }
    }


    @Override
    public IssueId getIssueId() {
        return new IssueId("acs/" + getJournalAbbreviation() + '/' + getVolume() + '/' + getNumber());
    }

    @Override
    public Issue getPreviousIssue() {
        Attribute prev = (Attribute) XPathUtils.getNode(getHtml(), ".//x:div[@id='issueNav']/x:a[@class='previous']/@href");
        if (prev != null) {
            String href = prev.getValue();
            if (href.startsWith("/toc/"+getJournalAbbreviation())) {
                String id = "acs/"+href.substring(5);
                URI url = getUrl().resolve(href);

                Issue issue = new Issue();
                issue.setId(new IssueId(id));
                issue.setUrl(url);
                return issue;
            }
        }
        return null;
    }

    @Override
    protected List<Node> getArticleNodes() {
        return XPathUtils.queryHTML(getHtml(), ".//x:div[@class='articleBox']");
    }



    @Override
    protected ArticleId getArticleId(Node articleNode, IssueId issueId) {
        Doi doi = getArticleDoi(null, articleNode);
        return new ArticleId(issueId, doi.getSuffix());
    }

    @Override
    protected Doi getArticleDoi(Article article, Node node) {
        String s = XPathUtils.getString(node, ".//x:input[@name='doi']/@value");
        return new Doi(s);
    }

    @Override
    protected URI getArticleUrl(Article article, Node articleNode) {
//        String s = XPathUtils.getString(articleNode, "./x:div[@class='articleLinksIcons']//x:a[text() = 'Abstract' | text() = 'First Page']/@href");
        String s = XPathUtils.getString(articleNode, "./x:div[@class='articleLinksIcons']//x:a[@class = 'articleLink'][1]/@href");
        URI url = getUrl().resolve(s);
        return url;
    }

    @Override
    protected URI getArticleSupportingInfoUrl(Article article, Node articleNode) {
        String s = XPathUtils.getString(articleNode, "./x:div[@class='articleLinksIcons']//x:a[text() = 'Supporting Info']/@href");
        if (s != null) {
            URI url = getUrl().resolve(s);
            return url;
        }
        return null;
    }

    @Override
    protected String getArticleTitle(Article article, Node articleNode) {
        // Additions and Corrections have no title
        // e.g. http://pubs.acs.org/toc/inocaj/39/19
        ParentNode source = (ParentNode) XPathUtils.getNode(articleNode, ".//x:h2/x:a");
        if (source != null) {
            return source.getValue();
        }
        return null;

    }

    @Override
    protected String getArticleTitleHtml(Article article, Node articleNode) {
        // Additions and Corrections have no title
        // e.g. http://pubs.acs.org/toc/inocaj/39/19
        ParentNode source = (ParentNode) XPathUtils.getNode(articleNode, ".//x:h2/x:a");
        if (source != null) {
            Element title = new Element("h1", "http://www.w3.org/1999/xhtml");
            AcsTools.copyChildren(source, title);
            return AcsTools.toHtml(title);
        }
        return null;
    }

    @Override
    protected List<String> getArticleAuthors(Article article, Node node) {
        List<String> authors = new ArrayList<String>();
        Element names = (Element) XPathUtils.getNode(node, ".//x:div[@class='articleAuthors']");
        if (names != null) {
            Element copy = (Element) names.copy();
            AcsTools.normaliseImages(copy);
            for (String name : copy.getValue().split(" and |, (and )?")) {
                authors.add(name.trim());
            }
        }
        return authors;

    }

    @Override
    protected Reference getArticleReference(Article article, Node articleNode) {
        Reference ref = new Reference();
        ref.setJournalTitle(journalTitle);
        ref.setVolume(volume);
        ref.setNumber(number);
        ref.setPages(getPages(articleNode));
        return ref;

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

    private String getPages(Node node) {
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




    private String getJournalTitle() {
        String s = XPathUtils.getString(getHtml(), "/x:html/x:head/x:title");
        return s.substring(s.indexOf(':'));
    }

    public String getVolume() {
        String text = XPathUtils.getString(getHtml(), ".//x:div[@id='tocMeta']/x:div[2]");
        Pattern p = Pattern.compile("Volume (\\d+)");
        Matcher m = p.matcher(text);
        m.find();
        return m.group(1);
    }

    public String getNumber() {
        String text = XPathUtils.getString(getHtml(), ".//x:div[@id='tocMeta']/x:div[2]");
        Pattern p = Pattern.compile("Issue (\\d+)");
        Matcher m = p.matcher(text);
        m.find();
        return m.group(1);
    }

    private static final Pattern P_YEAR = Pattern.compile("\\b(\\d{4})\\b");

    public String getYear() {
        String text = getDateBlock();
        Matcher m = P_YEAR.matcher(text);
        if (m.find()) {
            return m.group(1);
        }
        log().warn("Unable to locate year: "+text);
        return null;
    }

    private String getDateBlock() {
        return XPathUtils.getString(getHtml(), ".//x:div[@id='tocMeta']/x:div[@id='date']");
    }

    public LocalDate getDate() {
        String text = getDateBlock();
        DateTime dt;
        if (text.contains(", ")) {
            dt = DateTimeFormat.forPattern("MMMM d, yyyy").parseDateTime(text);
        } else {
            dt = DateTimeFormat.forPattern("MMMM yyyy").parseDateTime(text);
        }
        return new LocalDate(dt);
    }

    public String getJournalAbbreviation() {
        String s = getUrl().toString();
        Pattern p = Pattern.compile("pubs.acs.org/toc/([^/]+)/");
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
//        issue.setDate(getDate());
        return issue;
    }
    
}
