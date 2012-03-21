package wwmm.pubcrawler.controller;

import wwmm.pubcrawler.model.Article;
import wwmm.pubcrawler.model.Issue;
import wwmm.pubcrawler.model.Journal;
import wwmm.pubcrawler.v2.crawler.CrawlTask;
import wwmm.pubcrawler.v2.crawler.CrawlTaskBuilder;
import wwmm.pubcrawler.v2.crawler.TaskQueue;

/**
 * @author Sam Adams
 */
public class BibliographyCrawlStrategy {

    private TaskQueue taskQueue;

    public void notify(Journal journal) {
        final CrawlTask task = new CrawlTaskBuilder()

            .build();
        taskQueue.queueTask(task);
    }

    public void notify(Issue issue) {

    }

    public void notify(Article article) {

    }
    
}
