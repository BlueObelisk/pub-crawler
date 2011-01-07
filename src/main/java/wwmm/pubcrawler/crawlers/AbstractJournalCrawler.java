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

import wwmm.pubcrawler.CrawlerContext;
import wwmm.pubcrawler.model.Journal;
import wwmm.pubcrawler.model.Article;
import wwmm.pubcrawler.model.Issue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Sam Adams
 */
public abstract class AbstractJournalCrawler extends AbstractCrawler {

    private final Journal journal;

    private int maxArticlesPerIssue = -1;
    private int maxIssues = -1;
    private int minYear = -1;

    protected AbstractJournalCrawler(Journal journal, CrawlerContext context) {
        super(context);
        this.journal = journal;
    }

    protected Journal getJournal() {
        return journal;
    }

    public void crawlJournal() throws IOException {
        log().info("Crawling journal: "+getJournal().getFullTitle());
        if (hasIndex()) {
            crawlIndexedJournal();
        } else {
            crawlUnindexedJournal();
        }
        log().info("Crawl complete");
    }

    private void crawlUnindexedJournal() throws IOException {
        Issue issue = fetchCurrentIssue();
        if (!getDataStore().hasData(issue.getId())) {
            log().debug("new issue: "+issue.getId());
            getDataStore().save(issue.getId(), issue);
        }
        int i = 0;
        List<Issue> issues = new ArrayList<Issue>();
        while (issue != null) {
            if (getMaxIssues() >= 0 && i >= getMaxIssues()) {
                break;
            }
            if (getMinYear() > 0) {
                int year = Integer.valueOf(issue.getYear());
                if (year < getMinYear()) {
                    break;
                }
            }
            log().info("found issue: "+issue.getId());
            issues.add(issue);
            try {
                issue = getPreviousIssue(issue);
            } catch (Exception e) {
                log().warn("error crawling issue: "+issue.getId(), e);
                issue = null;
            }
            i++;
        }

        crawlArticles(issues);
    }


    private void crawlIndexedJournal() throws IOException {
        List<Issue> issueList = fetchIssueList();
        int i = 0;
        List<Issue> issues = new ArrayList<Issue>();
        for (Issue issue : issueList) {
            if (getMaxIssues() >= 0 && i >= getMaxIssues()) {
                break;
            }
            if (getMinYear() > 0) {
                int year = Integer.valueOf(issue.getYear());
                if (year < getMinYear()) {
                    break;
                }
            }
            log().info("found issue: "+issue.getId());
            if (!getDataStore().hasData(issue.getId())) {
                try {
                    issue = fetchIssue(issue);
                } catch (Exception e) {
                    log().warn("error crawling issue: "+issue.getId(), e);
                    continue;
                }
                log().debug("new issue: "+issue.getId());
                getDataStore().save(issue.getId(), issue);
            }
            issues.add(issue);
            i++;
        }
        
        crawlArticles(issues);
    }


    private Issue getPreviousIssue(Issue issue) throws IOException {
        Issue prev = issue.getPreviousIssue();
        if (prev == null) {
            return null;
        }

        if (getDataStore().hasData(prev.getId())) {
            return getDataStore().load(prev.getId(), Issue.class);
        } else {
            prev = fetchIssue(prev);
            getDataStore().save(prev.getId(), prev);
            return prev;
        }
    }


    private void crawlArticles(List<Issue> issues) {
        for (Issue issue : issues) {
            crawlArticles(issue);
        }
    }

    private void crawlArticles(Issue issue) {
        int i = 0;
        List<Article> articles = issue.getArticles();
        log().debug("crawling "+articles.size()+" articles");
        for (Article article : articles) {
            if (getMaxArticlesPerIssue() >= 0 && i >= getMaxArticlesPerIssue()) {
                break;
            }
            log().info("found article ("+i+"/"+articles.size()+"): "+article.getId() + " ["+article.getDoi()+"]");
            try {
                crawlArticle(article);
            } catch (Exception e) {
                log().warn("Error crawling article: "+article.getId() + " ["+article.getDoi()+"]", e);
            }
            i++;
        }
    }

    protected Article crawlArticle(Article article) throws IOException {
        if (!getDataStore().hasData(article.getId())) {
            try {
                article = fetchArticle(article);
            } catch (Exception e) {
                log().warn("error crawling article: "+article.getId() + " [" + article.getDoi() + "]", e);
                return null;
            }
            log().debug("new article: "+article.getId());
            getDataStore().save(article.getId(), article);
        } else {
            article = getDataStore().load(article.getId(), Article.class);
        }
        return article;
    }

    public abstract Issue fetchCurrentIssue() throws IOException;

    public Issue fetchIssue(Issue issue) throws IOException {
        AbstractIssueCrawler crawler = getFactory().createIssueCrawler(issue, getJournal(), getContext());
        return crawler.toIssue();
    }

    public Article fetchArticle(Article article) throws IOException {
        AbstractArticleCrawler crawler = getFactory().createArticleCrawler(article, getContext());
        return crawler.toArticle();
    }


    public boolean hasIndex() {
        return false;
    }

    public List<Issue> fetchIssueList() throws IOException {
        throw new UnsupportedOperationException();
    }


    public int getMaxArticlesPerIssue() {
        return maxArticlesPerIssue;
    }

    public void setMaxArticlesPerIssue(int maxArticlesPerIssue) {
        this.maxArticlesPerIssue = maxArticlesPerIssue;
    }


    public int getMaxIssues() {
        return maxIssues;
    }

    public void setMaxIssues(int maxIssues) {
        this.maxIssues = maxIssues;
    }


    public int getMinYear() {
        return minYear;
    }

    public void setMinYear(int minYear) {
        this.minYear = minYear;
    }

}
