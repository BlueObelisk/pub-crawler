package wwmm.pubcrawler.crawlers.acta.tasks;

import wwmm.pubcrawler.crawlers.CrawlTaskRunner;
import wwmm.pubcrawler.crawlers.acta.IucrFrameRequest;
import wwmm.pubcrawler.crawlers.acta.IucrFrameRequestFactory;
import wwmm.pubcrawler.crawlers.acta.IucrFrameResource;
import wwmm.pubcrawler.http.Fetcher;
import wwmm.pubcrawler.processors.IssueTocProcessor;
import wwmm.pubcrawler.tasks.IssueTocCrawlTaskData;
import wwmm.pubcrawler.tasks.IssueTocCrawlTaskDataMarshaller;
import wwmm.pubcrawler.tasks.Marshaller;
import wwmm.pubcrawler.tasks.TaskSpecification;

import javax.inject.Inject;

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

    public static class Runner extends CrawlTaskRunner<IssueTocCrawlTaskData, IucrFrameRequest, IucrFrameResource> {

        @Inject
        public Runner(final Fetcher<IucrFrameRequest, IucrFrameResource> fetcher, final IucrFrameRequestFactory requestFactory, final IssueTocProcessor<IucrFrameResource> processor) {
            super(fetcher, requestFactory, processor);
        }

    }
}
