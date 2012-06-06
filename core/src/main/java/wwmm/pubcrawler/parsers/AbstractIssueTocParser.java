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
package wwmm.pubcrawler.parsers;

import nu.xom.Document;
import nu.xom.Node;
import org.apache.log4j.Logger;
import wwmm.pubcrawler.CrawlerRuntimeException;
import wwmm.pubcrawler.model.*;
import wwmm.pubcrawler.model.id.ArticleId;
import wwmm.pubcrawler.model.id.IssueId;
import wwmm.pubcrawler.model.id.JournalId;
import wwmm.pubcrawler.types.Doi;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Sam Adams
 */
public abstract class AbstractIssueTocParser implements IssueTocParser {

    private final JournalId journalId;
    
    private final Document html;
    private final URI url;
    
    private String journalTitle;
    private String volume;
    private String number;
    private String year;

    protected AbstractIssueTocParser(final Document html, final URI url, final JournalId journalId) {
        this.html = html;
        this.url = url;
        this.journalId = journalId;
    }

    protected abstract Logger log();
    
    protected final Document getHtml() {
        return html;
    }

    protected final URI getUrl() {
        return url;
    }

    protected final JournalId getJournalId() {
        return journalId;
    }

    public final String getJournalTitle() {
        String journalTitle = this.journalTitle;
        if (journalTitle == null) {
            journalTitle = findJournalTitle();
            this.journalTitle = journalTitle;
        }
        return journalTitle;
    }

    public final String getVolume() {
        String volume = this.volume;
        if (volume == null) {
            volume = findVolume();
            this.volume = volume;
        }
        return volume;
    }

    public final String getNumber() {
        String number = this.number;
        if (number == null) {
            number = findNumber();
            this.number = number;
        }
        return number;
    }

    public final String getYear() {
        String year = this.year;
        if (year == null) {
            year = findYear();
            this.year = year;
        }
        return year;
    }

    /**
     * <p>Gets descriptions of all of the articles in the journal issue being
     * crawled.</p>
     *
	 * @return a list of descriptions of the articles for the issue.
     */
    @Override
    public final List<Article> getArticles() {
        final IssueId issueId = getIssueId();
        final List<Article> articles = new ArrayList<Article>();
        final List<Node> articleNodes = getArticleNodes();
        for (final Node articleNode : articleNodes) {
            try {
                final Article article = getArticle(articleNode, issueId);
                if (article != null) {
                    article.setIssueRef(issueId);
                    articles.add(article);
                }
            } catch (Exception e) {
                log().warn("Error reading article details from " + issueId, e);
            }
        }
        return articles;
    }

    public final IssueId getIssueId() {
        return new IssueId(journalId, getVolume(), getNumber());
    }

    protected abstract List<Node> getArticleNodes();


    public final Article getArticle(final Node articleNode, final IssueId issueId) {

        final Article article = new Article();

        final ArticleId articleId;
        try {
            articleId = getArticleId(articleNode);
            if (articleId == null) {
                throw new CrawlerRuntimeException("Unable to locate article ID", issueId, url);
            }
            article.setId(articleId);
        } catch (Exception e) {
            throw new CrawlerRuntimeException("Error locating article ID", e, issueId, url);
        }

        try {
            final Doi doi = getArticleDoi(articleNode);
            article.setDoi(doi);
        } catch (Exception e) {
            throw new CrawlerRuntimeException("Error locating DOI for article '"+articleId+"'", e, issueId, url);
        }

        try {
            final URI url = getArticleUrl(articleNode);
            article.setUrl(url);
        } catch (Exception e) {
            throw new CrawlerRuntimeException("Error locating URL for article '"+articleId+"'", e, issueId, url);
        }

        try {
            final URI suppUrl = getArticleSupportingInfoUrl(articleNode);
            article.setSupplementaryResourceUrl(suppUrl);
        } catch (Exception e) {
            throw new CrawlerRuntimeException("Error locating supp info URL for article '"+articleId+"'", e, issueId, url);
        }

        try {
            final String title = getArticleTitle(articleNode);
            if (title != null) {
                article.setTitle(title);
            }
        } catch (Exception e) {
            throw new CrawlerRuntimeException("Error locating title for article '"+articleId+"'", e, issueId, url);
        }

        try {
            final String titleHtml = getArticleTitleHtml(articleNode);
            if (titleHtml != null) {
                article.setTitleHtml(titleHtml);
            }
        } catch (Exception e) {
            throw new CrawlerRuntimeException("Error locating HTML title for article '"+articleId+"'", e, issueId, url);
        }

        try {
            final List<String> authors = getArticleAuthors(articleNode);
            if (authors != null) {
                article.setAuthors(authors);
            }
        } catch (Exception e) {
            throw new CrawlerRuntimeException("Error locating authors for article '"+articleId+"'", e, issueId, url);
        }

        try {
            final Reference reference = getArticleReference(articleNode);
            if (reference != null) {
                article.setReference(reference);
            }
        } catch (Exception e) {
            throw new CrawlerRuntimeException("Error locating reference for article '"+articleId+"'", e, issueId, url);
        }

        try {
            final List<SupplementaryResource> supplementaryResources = getArticleSupplementaryResources(articleId, articleNode);
            if (supplementaryResources != null) {
                article.setSupplementaryResources(supplementaryResources);
            }
        } catch (Exception e) {
            throw new CrawlerRuntimeException("Error locating supp info for article '"+articleId+"'", e, issueId, url);
        }

        try {
            final List<FullTextResource> fullTextResources = getArticleFullTextResources(articleNode);
            if (fullTextResources != null) {
                article.setFullTextResources(fullTextResources);
            }
        } catch (Exception e) {
            throw new CrawlerRuntimeException("Error locating full text for article '"+articleId+"'", e, issueId, url);
        }

        return article;
    }


    protected abstract ArticleId getArticleId(Node articleNode);

    protected abstract Doi getArticleDoi(Node articleNode);

    protected abstract URI getArticleUrl(Node articleNode);

    protected abstract URI getArticleSupportingInfoUrl(Node articleNode);

    protected abstract String getArticleTitle(Node articleNode);

    protected abstract String getArticleTitleHtml(Node articleNode);

    protected abstract List<String> getArticleAuthors(Node articleNode);

    protected abstract String findArticlePages(Node articleNode);

    protected abstract List<SupplementaryResource> getArticleSupplementaryResources(ArticleId articleId, Node articleNode);

    protected abstract List<FullTextResource> getArticleFullTextResources(Node articleNode);


    protected final Reference getArticleReference(Node articleNode) {
        return new Reference(getJournalTitle(), getVolume(), getNumber(), getYear(), findArticlePages(articleNode));
    }


    /**
     * <p>Gets a description of the previous issue of the journal being
     * crawled.</p>
     *
     * @return a description of the previous issue.
     */
    protected abstract IssueLink getPreviousIssue();

    protected abstract String findJournalTitle();

    protected abstract String findYear();

    protected abstract String findVolume();

    protected abstract String findNumber();

    @Override
    public final Issue getIssueDetails() {
        
        final Issue issue = new IssueBuilder()
            .withJournalId(journalId)
            .withJournalTitle(getJournalTitle())
            .withVolume(getVolume())
            .withNumber(getNumber())
            .withYear(getYear())
            .withUrl(getUrl())
            .build();
        
        issue.setPreviousIssue(getPreviousIssue());
        return issue;
    }

    @Override
    public List<IssueLink> getIssueLinks() {
        final IssueLink issue = getPreviousIssue();
        return issue == null ? Collections.<IssueLink>emptyList() : Collections.singletonList(issue);
    }

    @Override
    public List<URI> getIssueListLinks() {
        return null;
    }

}
