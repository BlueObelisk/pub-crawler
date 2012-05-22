package wwmm.pubcrawler.crawlers.springer.tasks;

import nu.xom.Document;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import wwmm.pubcrawler.archivers.JournalArchiver;
import wwmm.pubcrawler.crawler.Task;
import wwmm.pubcrawler.crawler.TaskQueue;
import wwmm.pubcrawler.crawlers.BasicHttpCrawlTaskRunner;
import wwmm.pubcrawler.crawlers.JournalHandler;
import wwmm.pubcrawler.crawlers.springer.SpringerPublicationListCrawlTaskData;
import wwmm.pubcrawler.crawlers.springer.SpringerPublicationListCrawlTaskDataMarshaller;
import wwmm.pubcrawler.crawlers.springer.SpringerPublicationListCrawlTaskFactory;
import wwmm.pubcrawler.crawlers.springer.SpringerPublicationListParserFactory;
import wwmm.pubcrawler.crawlers.springer.parsers.SpringerPublicationListParser;
import wwmm.pubcrawler.http.DocumentResource;
import wwmm.pubcrawler.http.Fetcher;
import wwmm.pubcrawler.http.UriRequest;
import wwmm.pubcrawler.model.Journal;
import wwmm.pubcrawler.tasks.Marshaller;
import wwmm.pubcrawler.tasks.TaskSpecification;
import wwmm.pubcrawler.utils.HtmlUtils;

import javax.inject.Inject;
import java.net.URI;

/**
 * @author Sam Adams
 */
public class SpringerPublicationListCrawlTask implements TaskSpecification<SpringerPublicationListCrawlTaskData> {

    public static final SpringerPublicationListCrawlTask INSTANCE = new SpringerPublicationListCrawlTask();

    @Override
    public Class<Runner> getRunnerClass() {
        return Runner.class;
    }

    @Override
    public Marshaller<SpringerPublicationListCrawlTaskData> getDataMarshaller() {
        return new SpringerPublicationListCrawlTaskDataMarshaller();
    }

    public static class Runner extends BasicHttpCrawlTaskRunner<SpringerPublicationListCrawlTaskData> {

        private final SpringerPublicationListParserFactory publicationListParserFactory;
        private final SpringerPublicationListCrawlTaskFactory issueIndexCrawlTaskFactory;
        private final JournalArchiver journalArchiver;
        private final JournalHandler journalHandler;
        private final TaskQueue taskQueue;

        @Inject
        public Runner(final Fetcher<UriRequest, CrawlerResponse> fetcher, final SpringerPublicationListParserFactory publicationListParserFactory, final JournalHandler journalHandler, final JournalArchiver journalArchiver, final SpringerPublicationListCrawlTaskFactory issueIndexCrawlTaskFactory, final TaskQueue taskQueue) {
            super(fetcher);
            this.publicationListParserFactory = publicationListParserFactory;
            this.journalHandler = journalHandler;
            this.journalArchiver = journalArchiver;
            this.issueIndexCrawlTaskFactory = issueIndexCrawlTaskFactory;
            this.taskQueue = taskQueue;
        }

        @Override
        protected void handleResponse(final String id, final SpringerPublicationListCrawlTaskData data, final CrawlerResponse response) throws Exception {
            final Document html = HtmlUtils.readHtmlDocument(response);
            final URI url = URI.create(html.getBaseURI());

            final SpringerPublicationListParser parser = publicationListParserFactory.createPublicationListParser(new DocumentResource(url, html));
            for (final Journal journal : parser.findJournals()) {
                archiveJournal(journal);
                journalHandler.handleJournal(journal);
            }

            final URI nextPage = parser.getNextPage();
            if (nextPage != null) {
                final String key = data.getKey();
                final int page = data.getPage();
                final Task crawlTask = issueIndexCrawlTaskFactory.createIssueListCrawlTask(nextPage, key, page + 1);
                taskQueue.queueTask(crawlTask);
            }
        }

        private void archiveJournal(final Journal journal) {
            journalArchiver.archive(journal);
        }

    }

}
