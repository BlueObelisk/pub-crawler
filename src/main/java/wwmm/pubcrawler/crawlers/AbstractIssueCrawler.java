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
package wwmm.pubcrawler.crawlers;

import nu.xom.Document;
import nu.xom.Node;
import org.joda.time.Duration;
import wwmm.pubcrawler.CrawlerContext;
import wwmm.pubcrawler.model.Article;
import wwmm.pubcrawler.model.Issue;
import wwmm.pubcrawler.model.Journal;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>The <code>IssueCrawler</code> class provides an outline for the methods
 * that a crawler of a journal issue should implement.</p>
 * 
 * @author Nick Day
 * @author Sam Adams
 * @version 2.0
 */
public abstract class AbstractIssueCrawler extends AbstractCrawler {

    private final Issue issueRef;
    private final Document html;
    private final URI url;

    private final Journal journal;

    protected AbstractIssueCrawler(Issue issue, CrawlerContext context) throws IOException {
        this(issue, null, context);
    }

    protected AbstractIssueCrawler(Issue issue, Journal journal, CrawlerContext context) throws IOException {
        super(context);
        this.issueRef = issue;
        this.journal = journal;
        this.html = fetchHtml(issue);
        this.url = URI.create(html.getBaseURI());
    }

    public Issue getIssueRef() {
        return issueRef;
    }

    protected Document fetchHtml(Issue issue) throws IOException {
        Duration maxAge;
        if (issue.isCurrent()) {
            maxAge = AGE_1DAY;
        } else {
            maxAge = AGE_MAX;
        }
        return readHtml(issue.getUrl(), issue.getId()+".html", maxAge);
    }


    protected Document getHtml() {
        return html;
    }

    protected URI getUrl() {
        return url;
    }

    protected Journal getJournal() {
        return journal;
    }

    /**
     * <p>Gets a unique identifier for the journal issue being crawled.</p>
     *
     * @return unique identifier of the issue.
     */
    protected abstract String getIssueId();

    /**
     * <p>Gets descriptions of all of the articles in the journal issue being
     * crawled.</p>
     *
	 * @return a list of descriptions of the articles for the issue.
     */
    public final List<Article> getArticles() {
        String issueId = getIssueId();
        List<Article> articles = new ArrayList<Article>();
        List<Node> articleNodes = getArticleNodes();
        for (Node articleNode : articleNodes) {
            try {
                Article article = getArticleDetails(articleNode, issueId);
                articles.add(article);
            } catch (Exception e) {
                log().warn("Error reading article details from " + issueId, e);
            }
        }
        return articles;
    }

    protected abstract List<Node> getArticleNodes();

    protected abstract Article getArticleDetails(Node context, String issueId);

    /**
     * <p>Gets a description of the previous issue of the journal being
     * crawled.</p>
     *
     * @return a description of the previous issue.
     */
    protected abstract Issue getPreviousIssue();

    protected abstract String getYear();

    protected abstract String getVolume();

    protected abstract String getNumber();


    public Issue toIssue() {
        Issue issue = new Issue();
        issue.setId(getIssueId());
        issue.setUrl(getUrl());
        issue.setYear(getYear());
        issue.setVolume(getVolume());
        issue.setNumber(getNumber());
        issue.setPreviousIssue(getPreviousIssue());
        try {
            List<Article> articles = getArticles();
//            if (articles.isEmpty()) {
//                throw new CrawlerRuntimeException("No articles found!");
//            }
            issue.setArticles(articles);
        } catch (Exception e) {
            log().warn("Error reading articles for issue: "+issue.getId()+" ["+getUrl()+"]");
        }
        return issue;
    }

}
