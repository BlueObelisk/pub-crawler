package wwmm.pubcrawler.crawlers;

import wwmm.pubcrawler.crawler.CrawlRunner;
import wwmm.pubcrawler.http.Fetcher;
import wwmm.pubcrawler.http.RequestFactory;
import wwmm.pubcrawler.model.id.JournalId;
import wwmm.pubcrawler.model.id.PublisherId;
import wwmm.pubcrawler.processors.IssueListProcessor;
import wwmm.pubcrawler.tasks.IssueListCrawlTaskData;

import javax.inject.Inject;

/**
 * @author Sam Adams
 */
public class IssueListCrawlRunner<Request, Resource> implements CrawlRunner<IssueListCrawlTaskData> {

    private final Fetcher<Request, Resource> fetcher;
    private final RequestFactory<Request> requestFactory;
    private final IssueListProcessor<Resource> processor;

    @Inject
    public IssueListCrawlRunner(final RequestFactory<Request> requestFactory, final Fetcher<Request, Resource> fetcher, final IssueListProcessor<Resource> processor) {
        this.requestFactory = requestFactory;
        this.fetcher = fetcher;
        this.processor = processor;
    }

    @Override
    public void run(final String id, final IssueListCrawlTaskData data) throws Exception {
        final Request request = requestFactory.createFetchTask(id, data);

        final PublisherId publisherId = new PublisherId(data.getPublisher());
        final JournalId journalId = new JournalId(publisherId, data.getJournal());

        final Resource resource = fetcher.fetch(request);
        processor.processIssueList(journalId, resource);
    }
}
