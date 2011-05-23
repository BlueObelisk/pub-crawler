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
import wwmm.pubcrawler.model.Article;
import wwmm.pubcrawler.model.Issue;
import wwmm.pubcrawler.model.Journal;

import java.io.IOException;
import java.util.*;

/**
 * @author Sam Adams
 */
public abstract class AbstractJournalCrawler extends AbstractCrawler {

    private final Journal journal;

    private int maxArticlesPerIssue = -1;
    private int maxIssues = -1;
    private int minYear = -1;

    private Set<String> visitedIssues = new LinkedHashSet<String>();

    protected AbstractJournalCrawler(Journal journal, CrawlerContext context) {
        super(context);
        this.journal = journal;
    }

    protected Journal getJournal() {
        return journal;
    }

    public void crawlJournal() throws IOException {
        log().info("Crawling journal: "+getJournal().getFullTitle());

        List<Issue> issueIndex = fetchIssueList();
        Iterator<Issue> issueIterator = issueIndex.iterator();

        List<Issue> issues = new ArrayList<Issue>();

        Issue issue;
        try {
            issue = fetchCurrentIssue();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching current issue: "+getJournal().getFullTitle(), e);
        }
        if (issue != null) {
            if (!getDataStore().containsIssue(issue.getId())) {
                log().debug("new issue: "+issue.getId());
                getDataStore().saveIssue(issue);
            }
        }

        while (issue != null || issueIterator.hasNext()) {
            if (getMaxIssues() >= 0 && issues.size() >= getMaxIssues()) {
                break;
            }

            if (issue != null) {
                int year = Integer.valueOf(issue.getYear());
                if (year < getMinYear()) {
                    break;
                }

                if (visitedIssues.add(issue.getId())) {
                    issues.add(issue);
                    log().info("found issue: "+issue.getId());
                }

                try {
                    issue = getPreviousIssue(issue);
                    if (issue != null && visitedIssues.contains(issue.getId())) {
                        issue = null;
                    }
                } catch (Exception e) {
                    log().warn("error fetching issue", e);
                    issue = null;
                }
            } else {
                issue = issueIterator.next();
                if (!getDataStore().containsIssue(issue.getId())) {
                    try {
                        issue = fetchIssue(issue);
                    } catch (Exception e) {
                        log().warn("error crawling issue: "+issue.getId(), e);
                        issue = null;
                        continue;
                    }
                    log().debug("new issue: "+issue.getId());
                    getDataStore().saveIssue(issue);
                }
            }
            Thread.yield();
        }

        crawlArticles(issues);

        log().info("Crawl complete: "+getJournal().getFullTitle());
    }

    private Issue getPreviousIssue(Issue issue) throws IOException {
        Issue prev = issue.getPreviousIssue();
        if (prev == null) {
            return null;
        }

        Issue prevIssue = getDataStore().findIssue(prev.getId());
        if (prevIssue == null) {
            prev = fetchIssue(prev);
            getDataStore().saveIssue(prev);
            return prev;
        } else {
            return prevIssue;
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
        log().info("crawling "+articles.size()+" articles from "+issue.getId());
        for (Article article : articles) {
            if (getMaxArticlesPerIssue() >= 0 && i >= getMaxArticlesPerIssue()) {
                break;
            }
            log().debug("found article ("+i+"/"+articles.size()+"): "+article.getId() + " ["+article.getDoi()+"]");
            try {
                crawlArticle(article);
            } catch (Exception e) {
                log().warn("Error crawling article: "+article.getId() + " ["+article.getDoi()+"]", e);
            }
            Thread.yield();
            i++;
        }
    }

    protected void crawlArticle(Article article) throws IOException {
        if (!getDataStore().containsArticle(article.getId())) {
            try {
                article = fetchArticle(article);
            } catch (Exception e) {
                log().warn("error fetching article: "+article.getId() + " [" + article.getDoi() + "]", e);
                return;
            }
            log().debug("new article: "+article.getId());
            getDataStore().saveArticle(article);
//        } else {
//            article = getDataStore().findArticle(article.getId());
        }
//        return article;
    }

    public abstract Issue fetchCurrentIssue() throws IOException;

    public Issue fetchIssue(Issue issue) throws IOException {
        AbstractIssueCrawler crawler = getFactory().createIssueCrawler(issue, getJournal(), getContext());
        return crawler.toIssue();
    }

    public Article fetchArticle(Article article) throws IOException {
        AbstractArticleCrawler crawler = getFactory().createArticleCrawler(article, getContext());
        if (crawler != null) {
            return crawler.toArticle();
        }
        return article;
    }


    public boolean hasIndex() {
        return false;
    }

    public List<Issue> fetchIssueList() throws IOException {
        return Collections.emptyList();
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
