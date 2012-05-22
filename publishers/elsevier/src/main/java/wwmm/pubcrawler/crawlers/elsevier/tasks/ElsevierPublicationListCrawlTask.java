package wwmm.pubcrawler.crawlers.elsevier.tasks;

import nu.xom.Document;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import wwmm.pubcrawler.archivers.JournalArchiver;
import wwmm.pubcrawler.crawlers.BasicPublicationListCrawlTaskRunner;
import wwmm.pubcrawler.crawlers.JournalHandler;
import wwmm.pubcrawler.crawlers.elsevier.ElsevierPublicationListParserFactory;
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
public class ElsevierPublicationListCrawlTask implements TaskSpecification<HttpCrawlTaskData> {

    public static final ElsevierPublicationListCrawlTask INSTANCE = new ElsevierPublicationListCrawlTask();

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
        public Runner(final Fetcher<UriRequest, CrawlerResponse> fetcher, final ElsevierPublicationListParserFactory parserFactory, final JournalArchiver journalArchiver, final JournalHandler journalHandler) {
            super(fetcher, new PublicationListProcessor<DocumentResource>(parserFactory, journalArchiver, journalHandler));
        }

        @Override
        protected Document readResponse(final CrawlerResponse response) throws IOException {
            return HtmlUtils.readXmlDocument(response);
        }
    }

}
