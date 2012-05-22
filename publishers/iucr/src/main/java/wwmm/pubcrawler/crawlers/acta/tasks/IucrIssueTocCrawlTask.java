package wwmm.pubcrawler.crawlers.acta.tasks;

import org.joda.time.Duration;
import wwmm.pubcrawler.controller.TaskReceiver;
import wwmm.pubcrawler.crawler.CrawlRunner;
import wwmm.pubcrawler.crawler.TaskData;
import wwmm.pubcrawler.crawlers.acta.IucrFrameRequestFactory;
import wwmm.pubcrawler.crawlers.acta.IucrFrameResource;
import wwmm.pubcrawler.crawlers.acta.IucrFrameRequest;
import wwmm.pubcrawler.http.Fetcher;
import wwmm.pubcrawler.model.id.JournalId;
import wwmm.pubcrawler.model.id.PublisherId;
import wwmm.pubcrawler.processors.IssueTocProcessor;
import wwmm.pubcrawler.tasks.*;

import javax.inject.Inject;
import java.net.URI;

/**
 * @author Sam Adams
 */
public class IucrIssueTocCrawlTask implements TaskSpecification<IssueTocCrawlTaskData> {

    public static final IucrIssueTocCrawlTask INSTANCE = new IucrIssueTocCrawlTask();

    @Override
    public Class<Runner> getRunnerClass() {
        return Runner.class;
    }

    @Override
    public Marshaller<IssueTocCrawlTaskData> getDataMarshaller() {
        return new IssueTocCrawlTaskDataMarshaller();
    }

    public static class Runner implements TaskRunner<IssueTocCrawlTaskData> {

        private final Fetcher<IucrFrameRequest, IucrFrameResource> fetcher;
        private final IssueTocProcessor<IucrFrameResource> processor;
        private final IucrFrameRequestFactory requestFactory;

        @Inject
        public Runner(final Fetcher<IucrFrameRequest, IucrFrameResource> fetcher, final IssueTocProcessor<IucrFrameResource> processor, final IucrFrameRequestFactory requestFactory) {
            this.fetcher = fetcher;
            this.processor = processor;
            this.requestFactory = requestFactory;
        }

        @Override
        public void run(final String id, final IssueTocCrawlTaskData data) throws Exception {
            final IucrFrameRequest request = requestFactory.createFetchTask(id, data);

            final PublisherId publisherId = new PublisherId(data.getPublisher());
            final JournalId journalId = new JournalId(publisherId, data.getJournal());

            final IucrFrameResource resource = fetcher.fetch(request);
            processor.process(id, journalId, resource);
        }

    }
}
