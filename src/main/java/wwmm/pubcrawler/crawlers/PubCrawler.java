package wwmm.pubcrawler.crawlers;

import org.apache.log4j.Logger;
import wwmm.pubcrawler.CrawlerContext;
import wwmm.pubcrawler.data.mongo.MongoStore;
import wwmm.pubcrawler.model.Article;
import wwmm.pubcrawler.model.Issue;
import wwmm.pubcrawler.model.Journal;
import wwmm.pubcrawler.model.id.IssueId;

import java.util.*;

public class PubCrawler {

    private static final Logger LOG = Logger.getLogger(PubCrawler.class);

    private Journal journal;
    private JournalHandler journalHandler;
    private CrawlerContext context;

    private int maxArticlesPerIssue = -1;
    private int maxIssues = -1;
    private int minYear = -1;

    public PubCrawler(Journal journal, JournalHandler journalHandler, CrawlerContext context) {
        this.journal = journal;
        this.journalHandler = journalHandler;
        this.context = context;
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


    public Journal getJournal() {
        return journal;
    }

    public MongoStore getDataStore() {
        return context.getDataStore();
    }


    protected CrawlerContext getContext() {
        return context;
    }


    public synchronized void run() {

        LOG.info("Crawling journal: "+getJournal().getTitle()+" "+getJournal().getId());

        if (!getDataStore().containsJournal(journal.getId())) {
            getDataStore().saveJournal(journal);
        }

        List<Issue> queue = initialiseQueue();

        Set<IssueId> visited = new HashSet<IssueId>();
        Set<IssueId> issueList = new LinkedHashSet<IssueId>();

        while (!queue.isEmpty()) {
            // Check issue count
            if (getMaxIssues() >= 0 && visited.size() >= getMaxIssues()) {
                LOG.info("Stopping crawl - max issues reached: " + getMaxIssues());
                break;
            }

            Issue issue = queue.remove(queue.size()-1);
            if (visited.contains(issue.getId())) {
                continue;
            }
            if (isSkip(issue)) {
                continue;
            }

            if (issue.getYear() != null) {
                try {
                    int year = Integer.valueOf(issue.getYear());
                    if (year < getMinYear()) {
                        continue;
                    }
                } catch (NumberFormatException e) {
                    LOG.warn("Error parsing year: "+issue.getYear()+" ["+issue.getId()+"]");
                }
            }

            issue = fetchIssue(issue);
            if (issue == null) {
                continue;
            }

            visited.add(issue.getId());
            if (isSkip(issue)) {
                continue;
            }

            // Check year
            try {
                int year = Integer.valueOf(issue.getYear());
                if (year < getMinYear()) {
                    continue;
                }
            } catch (NumberFormatException e) {
                LOG.warn("Error parsing year: "+issue.getYear()+" ["+issue.getId()+"]");
            }

            if (!getDataStore().containsIssue(issue.getId())) {
                LOG.debug("new issue: "+issue.getId());
                getDataStore().saveIssue(issue);
                getDataStore().addIssueToJournal(journal, issue);
            } else {
                LOG.debug("found issue: "+issue.getId());
            }

            issueList.add(issue.getId());

            // Queue previous issue
            Issue prev = issue.getPreviousIssue();
            if (prev != null) {
                if (!visited.contains(prev.getId())) {
                    queue.add(prev);
                }
            }
            Thread.yield();
        }

        crawlArticles(issueList);

        LOG.info("Crawl complete: "+getJournal().getTitle());
    }

    protected boolean isSkip(Issue issue) {
        return false;
    }

    protected List<Issue> initialiseQueue() {
        List<Issue> queue = new ArrayList<Issue>();

        List<Issue> issueIndex = fetchIssueList();
        if (issueIndex != null) {
            queue.addAll(issueIndex);
        }

        System.err.println("fetch current issue...");
        Issue currentIssue = fetchCurrentIssue();
        if (currentIssue != null) {
            queue.add(currentIssue);
        }

//        sortQueue(queue);

        return queue;
    }

    private void sortQueue(List<Issue> queue) {
        Collections.sort(queue, new Comparator<Issue>() {

            private IntuitiveComparator c = new IntuitiveComparator();

            public int compare(Issue o1, Issue o2) {

                String v1 = o1.getVolume();
                String v2 = o2.getVolume();
                if (v1 != null || v2 != null) {
                    if (v1 == null) {
                        return -1;
                    }
                    if (v2 == null) {
                        return 1;
                    }

                    int i = c.compare(v1, v2);
                    if (i != 0) {
                        return i;
                    }
                }

                String n1 = o1.getNumber();
                String n2 = o2.getNumber();
                if (n1 != null || n2 != null) {
                    if (n1 == null) {
                        return -1;
                    }
                    if (n2 == null) {
                        return 1;
                    }
                    int i = c.compare(n1, n2);
                    if (i != 0) {
                        return i;
                    }
                }

                return 0;
            }

        });
    }




    private void crawlArticles(Collection<IssueId> issues) {
        for (IssueId issueId : issues) {
            Issue issue = getDataStore().findIssue(issueId);
            if (issue != null) {
                crawlArticles(issue);
            }
        }
    }

    protected final void crawlArticles(Issue issue) {
        if (getMaxArticlesPerIssue() != 0) {
            List<Article> articles = issue.getArticles();
            if (issue.getArticles().isEmpty()) {
                LOG.warn("No articles found [issue: "+issue.getId()+"]");
            } else {
                LOG.info("crawling "+articles.size()+" articles from "+issue.getId());
                crawlArticles(articles, issue);
            }
        }
    }

    private void crawlArticles(List<Article> articles, Issue issue) {
        int i = 0;
        for (Article article : articles) {
            if (getMaxArticlesPerIssue() >= 0 && i >= getMaxArticlesPerIssue()) {
                break;
            }
            LOG.debug("found article ("+i+"/"+articles.size()+"): "+article.getId() + " ["+article.getDoi()+"]");
            crawlArticle(article);
            Thread.yield();
            i++;
        }
    }

    protected Article crawlArticle(Article article) {
        if (getDataStore().containsArticle(article.getId())) {
            article = getDataStore().findArticle(article.getId());
        } else {
            article = fetchArticle(article);
            if (article != null) {
                LOG.debug("new article: "+article.getId());
                getDataStore().saveArticle(article);
            }
        }
        return article;
    }


    protected Issue fetchIssue(Issue issue) {
        Issue tmp = getDataStore().findIssue(issue.getId());
        if (tmp == null) {
            try {
                Issue issue_ = journalHandler.fetchIssue(issue);
                return issue_;
            } catch (Exception e) {
                LOG.error("Error fetching issue " + issue.getId(), e);
            }
        }
        return tmp;
    }

    public Article fetchArticle(Article article) {

        Article tmp = null;
        try {
            AbstractArticleCrawler crawler = context.getCrawlerFactory().createArticleCrawler(article, context);
            tmp = crawler.toArticle();
        } catch (Exception e) {
            LOG.error("Error fetching article "+article.getId(), e);
        }
        return tmp;

    }

    public Issue fetchCurrentIssue() {
        try {
            Issue currentIssue = journalHandler.fetchCurrentIssue();
            return currentIssue;
        } catch (Exception e) {
            LOG.error("Error fetching current issue "+journal.getId(), e);
            return null;
        }
    }

    public List<Issue> fetchIssueList() {
        try {
            List<Issue> issueList = journalHandler.fetchIssueList();
            return issueList;
        } catch (Exception e) {
            LOG.error("Error fetching issue list "+journal.getId(), e);
            return new ArrayList<Issue>();
        }
    }


}
