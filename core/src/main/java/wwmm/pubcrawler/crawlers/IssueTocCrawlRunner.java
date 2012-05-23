package wwmm.pubcrawler.crawlers;

import wwmm.pubcrawler.crawler.CrawlRunner;
import wwmm.pubcrawler.http.Fetcher;
import wwmm.pubcrawler.http.RequestFactory;
import wwmm.pubcrawler.model.id.JournalId;
import wwmm.pubcrawler.model.id.PublisherId;
import wwmm.pubcrawler.processors.IssueTocProcessor;
import wwmm.pubcrawler.tasks.IssueTocCrawlTaskData;

/**
 * @author Sam Adams
 */
public class IssueTocCrawlRunner<Request, Resource> implements CrawlRunner<IssueTocCrawlTaskData> {
    
    private final Fetcher<Request, Resource> fetcher;
    private final RequestFactory<Request> requestFactory;
    private final IssueTocProcessor<Resource> processor;

    public IssueTocCrawlRunner(final RequestFactory<Request> requestFactory, final Fetcher<Request, Resource> fetcher, final IssueTocProcessor<Resource> processor) {
        this.requestFactory = requestFactory;
        this.fetcher = fetcher;
        this.processor = processor;
    }

    @Override
    public void run(final String id, final IssueTocCrawlTaskData data) throws Exception {
        final Request request = requestFactory.createFetchTask(id, data);

        final PublisherId publisherId = new PublisherId(data.getPublisher());
        final JournalId journalId = new JournalId(publisherId, data.getJournal());

        final Resource resource = fetcher.fetch(request);
        processor.process(id, journalId, resource);
    }
}
