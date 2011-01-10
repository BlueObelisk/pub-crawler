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
import wwmm.pubcrawler.CrawlerRuntimeException;
import wwmm.pubcrawler.crawlers.AbstractIssueCrawler;
import wwmm.pubcrawler.model.Article;
import wwmm.pubcrawler.model.Issue;
import wwmm.pubcrawler.model.Reference;
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
            return readHtml(issue.getUrl(), issue.getId()+".html", AGE_1DAY);
        } else {
            return readHtml(issue.getUrl(), issue.getId()+".html", AGE_MAX);
        }
    }


    @Override
    public String getIssueId() {
        return "acs/" + getJournalAbbreviation() + '/' + getVolume() + '/' + getNumber();
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
                issue.setId(id);
                issue.setUrl(url);
                return issue;
            }
        }
        return null;
    }

    @Override
    public List<Article> getArticles() {
        String issueId = getIssueId();
        List<Article> idList = new ArrayList<Article>();
        List<Node> articleNodes = XPathUtils.queryHTML(getHtml(), ".//x:div[@class='articleBox']");
        for (Node articleNode : articleNodes) {
            Article article = getArticle(articleNode, issueId);
            idList.add(article);
        }
        return idList;
    }

    private Article getArticle(Node node, String issueId) {
        Article article = new Article();
        Doi doi = getDoi(node);
        article.setDoi(doi);
        String articleId = issueId + '/' + doi.getSuffix();
        article.setId(articleId);
        article.setTitleHtml(getTitle(node));
        article.setAuthors(getAuthors(node));
        Reference ref = getReference(node);
        article.setReference(ref);
        return article;
    }

    private Reference getReference(Node node) {
        Reference ref = new Reference();
        ref.setJournalTitle(journalTitle);
        ref.setVolume(volume);
        ref.setNumber(number);
        ref.setPages(getPages(node));
        return ref;
    }

    private String getPages(Node node) {
        String pages = XPathUtils.getString(node, ".//x:div[starts-with(., 'pp ')]");
        if (pages != null) {
            return pages.substring(3);
        }
        pages = XPathUtils.getString(node, ".//x:div[starts-with(., 'p ')]");
        if (pages != null) {
            return pages.substring(2);
        }
        throw new CrawlerRuntimeException("Unable to find pages");
    }

    private List<String> getAuthors(Node node) {
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

    private String getTitle(Node node) {
        Element title = new Element("h1", "http://www.w3.org/1999/xhtml");
        ParentNode source = (ParentNode) XPathUtils.getNode(node, ".//x:h2/x:a");
        AcsTools.copyChildren(source, title);
        return AcsTools.toHtml(title);
    }

    private Doi getDoi(Node node) {
        String s = XPathUtils.getString(node, ".//x:input[@name='doi']/@value");
        return new Doi(s);
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

    public String getYear() {
        return Integer.toString(getDate().getYear());
    }

    public LocalDate getDate() {
        String text = XPathUtils.getString(getHtml(), ".//x:div[@id='tocMeta']/x:div[@id='date']");
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
