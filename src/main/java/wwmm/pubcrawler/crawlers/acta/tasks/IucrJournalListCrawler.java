package wwmm.pubcrawler.crawlers.acta.tasks;

import nu.xom.Document;
import org.joda.time.Duration;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerGetRequest;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import uk.ac.cam.ch.wwmm.httpcrawler.HttpFetcher;
import wwmm.pubcrawler.crawlers.acta.parsers.IucrPublicationListParser;
import wwmm.pubcrawler.model.Journal;
import wwmm.pubcrawler.model.id.PublisherId;
import wwmm.pubcrawler.v2.crawler.CrawlRunner;
import wwmm.pubcrawler.v2.crawler.CrawlTask;
import wwmm.pubcrawler.v2.crawler.TaskData;
import wwmm.pubcrawler.v2.crawler.TaskQueue;
import wwmm.pubcrawler.v2.crawler.common.HtmlCrawler;
import wwmm.pubcrawler.v2.repositories.JournalRepository;

import javax.inject.Inject;
import java.net.URI;

import static java.lang.String.format;
import static wwmm.pubcrawler.v2.crawler.CrawlTaskBuilder.newJob;

/**
 * @author Sam Adams
 */
public class IucrJournalListCrawler extends HtmlCrawler implements CrawlRunner {
    
    private static final PublisherId IUCR = new PublisherId("iucr");
    private static final URI JOURNAL_LIST = URI.create("http://journals.iucr.org/indexbdy.html");

    private final HttpFetcher crawler;
    private final JournalRepository journalRepo;
    private final TaskQueue crawlQueue;

    @Inject
    public IucrJournalListCrawler(final HttpFetcher crawler, final JournalRepository journalRepo, final TaskQueue crawlQueue) {
        this.crawler = crawler;
        this.journalRepo = journalRepo;
        this.crawlQueue = crawlQueue;
    }

    @Override
    public void run(final String id, final TaskData data) throws Exception {
        final CrawlerResponse response = crawler.execute(new CrawlerGetRequest(JOURNAL_LIST, "iucr::pub-list.html", Duration.standardDays(1)));
        final Document html = readDocument(response);
        final IucrPublicationListParser parser = new IucrPublicationListParser(IUCR, html, JOURNAL_LIST);
        for (final Journal journal : parser.findJournals()) {
            ensureJournalInRepository(journal);
            enqueueJournalCrawl(journal);
        }
    }

    private void ensureJournalInRepository(final Journal journal) {
        Journal repoJournal = journalRepo.getJournal(IUCR, journal.getAbbreviation());
        
    }

    private void enqueueJournalCrawl(final Journal journal) {
        final CrawlTask task = newJob()
            .ofType(IucrIssueIndexCrawler.class)
            .withId(format("iucr::%s:issue-index.html", journal.getAbbreviation()))
            .build();
        crawlQueue.queueTask(task);
    }

}
