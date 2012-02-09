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
import wwmm.pubcrawler.CrawlerRuntimeException;
import wwmm.pubcrawler.crawlers.wiley.WileyIssueCrawler;
import wwmm.pubcrawler.model.*;
import wwmm.pubcrawler.model.id.ArticleId;
import wwmm.pubcrawler.model.id.IssueId;
import wwmm.pubcrawler.types.Doi;

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
public abstract class AbstractIssueCrawler extends AbstractCrawler implements IssueCrawler {

    private final Issue issueRef;
    private final Document html;
    private final URI url;

    private final Journal journal;

//    protected AbstractIssueCrawler(Issue issue, CrawlerContext context) throws IOException {
//        this(issue, null, context);
//    }

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
        return readHtml(issue.getUrl(), issue.getId(), "toc.html", maxAge);
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
    protected abstract IssueId getIssueId();

    /**
     * <p>Gets descriptions of all of the articles in the journal issue being
     * crawled.</p>
     *
	 * @return a list of descriptions of the articles for the issue.
     */
    public final List<Article> getArticles() {
        IssueId issueId = getIssueId();
        List<Article> articles = new ArrayList<Article>();
        List<Node> articleNodes = getArticleNodes();
        for (Node articleNode : articleNodes) {
            try {
                Article article = getArticle(articleNode, issueId);
                if (article != null) {
                    articles.add(article);
                }
            } catch (Exception e) {
                log().warn("Error reading article details from " + issueId, e);
            }
        }
        return articles;
    }

    protected abstract List<Node> getArticleNodes();


    protected final Article getArticle(Node articleNode, IssueId issueId) {

        Article article = new Article();

        ArticleId articleId;
        try {
            articleId = getArticleId(articleNode, issueId);
            if (articleId == null) {
                throw new CrawlerRuntimeException("Unable to locate article ID [issue: "+issueId+"]");
            }
            article.setId(articleId);
        } catch (Exception e) {
            throw new CrawlerRuntimeException("Error locating article ID [issue: "+issueId+"]", e);
        }

        try {
            Doi doi = getArticleDoi(article, articleNode);
            article.setDoi(doi);
        } catch (Exception e) {
            throw new CrawlerRuntimeException("Error locating DOI [issue: "+issueId+" | article: "+articleId+"]", e);
        }

        try {
            URI url = getArticleUrl(article, articleNode);
            article.setUrl(url);
        } catch (Exception e) {
            throw new CrawlerRuntimeException("Error locating URL [issue: "+issueId+" | article: "+articleId+"]", e);
        }

        try {
            URI suppUrl = getArticleSupportingInfoUrl(article, articleNode);
            article.setSupplementaryResourceUrl(suppUrl);
        } catch (Exception e) {
            throw new CrawlerRuntimeException("Error locating supp info URL [issue: "+issueId+" | article: "+articleId+"]", e);
        }

        try {
            String title = getArticleTitle(article, articleNode);
            if (title != null) {
                article.setTitle(title);
            }
        } catch (Exception e) {
            throw new CrawlerRuntimeException("Error locating title [issue: "+issueId+" | article: "+articleId+"]", e);
        }

        try {
            String titleHtml = getArticleTitleHtml(article, articleNode);
            if (titleHtml != null) {
                article.setTitleHtml(titleHtml);
            }
        } catch (Exception e) {
            throw new CrawlerRuntimeException("Error locating HTML title [issue: "+issueId+" | article: "+articleId+"]", e);
        }

        try {
            List<String> authors = getArticleAuthors(article, articleNode);
            if (authors != null) {
                article.setAuthors(authors);
            }
        } catch (Exception e) {
            throw new CrawlerRuntimeException("Error locating authors [issue: "+issueId+" | article: "+articleId+"]", e);
        }

        try {
            Reference reference = getArticleReference(article, articleNode);
            if (reference != null) {
                article.setReference(reference);
            }
        } catch (Exception e) {
            throw new CrawlerRuntimeException("Error locating reference [issue: "+issueId+" | article: "+articleId+"]", e);
        }

        try {
            List<SupplementaryResource> supplementaryResources = getArticleSupplementaryResources(article, articleNode);
            if (supplementaryResources != null) {
                article.setSupplementaryResources(supplementaryResources);
            }
        } catch (Exception e) {
            throw new CrawlerRuntimeException("Error locating supp info [issue: "+issueId+" | article: "+articleId+"]", e);
        }

        try {
            List<FullTextResource> fullTextResources = getArticleFullTextResources(article, articleNode);
            if (fullTextResources != null) {
                article.setFullTextResources(fullTextResources);
            }
        } catch (Exception e) {
            throw new CrawlerRuntimeException("Error locating full text [issue: "+issueId+" | article: "+articleId+"]", e);
        }

        return article;
    }


    protected abstract ArticleId getArticleId(Node articleNode, IssueId issueId);

    protected abstract Doi getArticleDoi(Article article, Node articleNode);

    protected abstract URI getArticleUrl(Article article, Node articleNode);

    protected abstract URI getArticleSupportingInfoUrl(Article article, Node articleNode);

    protected abstract String getArticleTitle(Article article, Node articleNode);

    protected abstract String getArticleTitleHtml(Article article, Node articleNode);

    protected abstract List<String> getArticleAuthors(Article article, Node articleNode);

    protected abstract Reference getArticleReference(Article article, Node articleNode);

    protected abstract List<SupplementaryResource> getArticleSupplementaryResources(Article article, Node articleNode);

    protected abstract List<FullTextResource> getArticleFullTextResources(Article article, Node articleNode);



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


    @Override
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
            if (articles.isEmpty() && !(this instanceof WileyIssueCrawler)) {
                log().warn("No articles found in issue: "+issue.getId());
            }
            issue.setArticles(articles);
        } catch (Exception e) {
            log().warn("Error reading articles for issue: "+issue.getId()+" ["+getUrl()+"]");
        }
        return issue;
    }

}
