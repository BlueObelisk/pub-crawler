package wwmm.pubcrawler.crawlers.acta.tasks;

import org.joda.time.Duration;
import wwmm.pubcrawler.crawler.CrawlRunner;
import wwmm.pubcrawler.crawler.TaskData;
import wwmm.pubcrawler.crawlers.acta.IucrFrameRequestFactory;
import wwmm.pubcrawler.crawlers.acta.IucrFrameResource;
import wwmm.pubcrawler.crawlers.acta.IucrFrameRequest;
import wwmm.pubcrawler.http.Fetcher;
import wwmm.pubcrawler.model.id.JournalId;
import wwmm.pubcrawler.model.id.PublisherId;
import wwmm.pubcrawler.processors.IssueTocProcessor;

import javax.inject.Inject;
import java.net.URI;

/**
 * @author Sam Adams
 */
public class IucrIssueTocCrawlTask implements CrawlRunner {

    private final Fetcher<IucrFrameRequest, IucrFrameResource> fetcher;
    private final IssueTocProcessor<IucrFrameResource> processor;
    private final IucrFrameRequestFactory requestFactory;

    @Inject
    public IucrIssueTocCrawlTask(final Fetcher<IucrFrameRequest, IucrFrameResource> fetcher, final IssueTocProcessor<IucrFrameResource> processor, final IucrFrameRequestFactory requestFactory) {
        this.fetcher = fetcher;
        this.processor = processor;
        this.requestFactory = requestFactory;
    }

    @Override
    public void run(final String id, final TaskData data) throws Exception {
        final IucrFrameRequest request = requestFactory.createFetchTask(id, data);

        final PublisherId publisherId = new PublisherId(data.getString("publisher"));
        final JournalId journalId = new JournalId(publisherId, data.getString("journal"));

        final IucrFrameResource resource = fetcher.fetch(request);
        processor.process(id, journalId, resource);
    }
}
