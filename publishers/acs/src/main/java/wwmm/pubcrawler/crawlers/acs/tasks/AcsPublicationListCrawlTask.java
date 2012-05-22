package wwmm.pubcrawler.crawlers.acs.tasks;

import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import wwmm.pubcrawler.archivers.JournalArchiver;
import wwmm.pubcrawler.crawlers.BasicPublicationListCrawlTaskRunner;
import wwmm.pubcrawler.http.Fetcher;
import wwmm.pubcrawler.http.UriRequest;
import wwmm.pubcrawler.crawlers.JournalHandler;
import wwmm.pubcrawler.crawlers.acs.AcsPublicationListParserFactory;
import wwmm.pubcrawler.http.DocumentResource;
import wwmm.pubcrawler.processors.PublicationListProcessor;
import wwmm.pubcrawler.tasks.*;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author Sam Adams
 */
@Singleton
public class AcsPublicationListCrawlTask implements TaskSpecification<HttpCrawlTaskData> {

    public static final AcsPublicationListCrawlTask INSTANCE = new AcsPublicationListCrawlTask();

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
        public Runner(final Fetcher<UriRequest, CrawlerResponse> fetcher, final AcsPublicationListParserFactory parserFactory, final JournalArchiver journalArchiver, final JournalHandler journalHandler) {
            super(fetcher, new PublicationListProcessor<DocumentResource>(parserFactory, journalArchiver, journalHandler));
        }

    }
}
