package wwmm.pubcrawler.crawlers.acs.tasks;

import wwmm.pubcrawler.v2.crawler.TaskQueue;
import wwmm.pubcrawler.v2.crawler.CrawlRunner;
import wwmm.pubcrawler.v2.crawler.CrawlRunnerFactory;
import wwmm.pubcrawler.v2.crawler.CrawlTask;

import javax.inject.Inject;

import static wwmm.pubcrawler.v2.crawler.CrawlTaskBuilder.newJob;

/**
 * @author Sam Adams
 */
public class AcsCrawlerApplication implements Runnable {

    private final CrawlRunnerFactory crawlRunnerFactory;
    private final TaskQueue crawlQueue;

    private volatile boolean stop;

    @Inject
    public AcsCrawlerApplication(final CrawlRunnerFactory crawlRunnerFactory, final TaskQueue crawlQueue) {
        this.crawlRunnerFactory = crawlRunnerFactory;
        this.crawlQueue = crawlQueue;
    }

    public void run() {

        loadSeedData();

        while (!stop) {
            final CrawlTask task = crawlQueue.nextTask();
            if (task != null) {
                final CrawlRunner runner = crawlRunnerFactory.createCrawlRunner(task);
                try {
                    runner.run(task.getId(), task.getData());
                } catch (Exception e) {
                    System.err.println("Error running task " + task.getId());
                    e.printStackTrace();
                }
            } else {
                // no more tasks
                stop = true;
            }
        }
    }

    private void loadSeedData() {
        final CrawlTask task = newJob(AcsJournalListCrawler.class)
            .withId("acs:pub-list")
            .build();
        
        crawlQueue.queueTask(task);
    }

}
