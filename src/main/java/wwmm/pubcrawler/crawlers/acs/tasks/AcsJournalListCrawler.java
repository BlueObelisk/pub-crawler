package wwmm.pubcrawler.crawlers.acs.tasks;

import nu.xom.Document;
import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerGetRequest;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import uk.ac.cam.ch.wwmm.httpcrawler.HttpFetcher;
import wwmm.pubcrawler.crawlers.acs.parsers.AcsPublicationListParser;
import wwmm.pubcrawler.model.Journal;
import wwmm.pubcrawler.model.id.PublisherId;
import wwmm.pubcrawler.v2.crawler.CrawlRunner;
import wwmm.pubcrawler.v2.crawler.CrawlTask;
import wwmm.pubcrawler.v2.crawler.TaskData;
import wwmm.pubcrawler.v2.crawler.TaskQueue;
import wwmm.pubcrawler.v2.crawler.common.HtmlCrawler;
import wwmm.pubcrawler.v2.repositories.JournalRepository;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;
import static wwmm.pubcrawler.v2.crawler.CrawlTaskBuilder.newJob;

/**
 * @author Sam Adams
 */
public class AcsJournalListCrawler extends HtmlCrawler implements CrawlRunner {

    private static final Logger LOG = LoggerFactory.getLogger(AcsJournalListCrawler.class);

    public static final URI URL = URI.create("http://pubs.acs.org/");

    private static final PublisherId ACS = new PublisherId("acs");

    private final HttpFetcher httpCrawler;
    private final TaskQueue crawlQueue;
    private final JournalRepository journalRepository;

    @Inject
    public AcsJournalListCrawler(final TaskQueue crawlQueue, final JournalRepository journalRepository, final HttpFetcher httpCrawler) {
        this.crawlQueue = crawlQueue;
        this.journalRepository = journalRepository;
        this.httpCrawler = httpCrawler;
    }

    @Override
    public void run(final String id, final TaskData data) throws Exception {
        final Document html = fetchResource(id, data);
        final List<Journal> journals = parseResource(html);
        for (final Journal journal : journals) {
            journalRepository.updateJournal(ACS, journal);
            queueTask(journal);
            break;
        }
    }

    private void queueTask(final Journal journal) {
        final Map<String,String> data = new HashMap<String, String>();
        data.put("url", format("http://pubs.acs.org/toc/%s/current", journal.getAbbreviation()));
        data.put("journalId", format("acs:%s", journal.getAbbreviation()));

        final CrawlTask task = newJob()
            .ofType(AcsIssueTocCrawler.class)
            .withId(format("acs~issue~%s~current", journal.getAbbreviation()))
            .withData(data)
            .build();

        crawlQueue.queueTask(task);
    }

    private List<Journal> parseResource(final Document html) {
        final AcsPublicationListParser parser = new AcsPublicationListParser(ACS, html, URI.create(html.getBaseURI()));
        return parser.findJournals();
    }

    private Document fetchResource(final String id, final TaskData data) throws IOException {
        final String key = String.format("%s:journal-list.html", id);
        final CrawlerResponse response = httpCrawler.execute(new CrawlerGetRequest(URL, key, Duration.standardDays(1)));
        return readDocument(response);
    }

}
