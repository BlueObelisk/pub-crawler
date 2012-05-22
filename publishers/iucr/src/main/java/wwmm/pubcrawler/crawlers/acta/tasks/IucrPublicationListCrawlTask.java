package wwmm.pubcrawler.crawlers.acta.tasks;

import nu.xom.Document;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import wwmm.pubcrawler.archivers.JournalArchiver;
import wwmm.pubcrawler.crawlers.BasicPublicationListCrawlTaskRunner;
import wwmm.pubcrawler.crawlers.acta.IucrJournalHandler;
import wwmm.pubcrawler.crawlers.acta.IucrPublicationListParserFactory;
import wwmm.pubcrawler.http.DocumentResource;
import wwmm.pubcrawler.http.Fetcher;
import wwmm.pubcrawler.http.UriRequest;
import wwmm.pubcrawler.processors.PublicationListProcessor;
import wwmm.pubcrawler.tasks.HttpCrawlTaskData;
import wwmm.pubcrawler.tasks.HttpCrawlTaskDataMarshaller;
import wwmm.pubcrawler.tasks.Marshaller;
import wwmm.pubcrawler.tasks.TaskSpecification;
import wwmm.pubcrawler.utils.HtmlUtils;

import javax.inject.Inject;
import java.io.IOException;

/**
 * @author Sam Adams
 */
public class IucrPublicationListCrawlTask implements TaskSpecification<HttpCrawlTaskData> {

    public static final IucrPublicationListCrawlTask INSTANCE = new IucrPublicationListCrawlTask();

    @Override
    public Class<Runner> getRunnerClass() {
        return Runner.class;
    }

    @Override
    public Marshaller<HttpCrawlTaskData> getDataMarshaller() {
        return new HttpCrawlTaskDataMarshaller();
    }

    public static class Runner extends BasicPublicationListCrawlTaskRunner {

        @Inject
        public Runner(final Fetcher<UriRequest, CrawlerResponse> fetcher, final IucrPublicationListParserFactory parserFactory, final JournalArchiver journalArchiver, final IucrJournalHandler journalHandler) {
            super(fetcher, new PublicationListProcessor<DocumentResource>(parserFactory, journalArchiver, journalHandler));
        }

        @Override
        protected Document readResponse(final CrawlerResponse response) throws IOException {
            return HtmlUtils.readHtmlDocument(response);
        }

    }
}
