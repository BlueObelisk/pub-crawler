package wwmm.pubcrawler.crawlers;

import wwmm.pubcrawler.crawler.CrawlRunner;
import wwmm.pubcrawler.crawler.TaskData;
import wwmm.pubcrawler.http.Fetcher;
import wwmm.pubcrawler.http.RequestFactory;
import wwmm.pubcrawler.model.id.JournalId;
import wwmm.pubcrawler.model.id.PublisherId;
import wwmm.pubcrawler.processors.IssueListProcessor;

import javax.inject.Inject;

/**
 * @author Sam Adams
 */
public class IssueListCrawlRunner<Request, Resource> implements CrawlRunner {

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
    public void run(final String id, final TaskData data) throws Exception {
        final Request request = requestFactory.createFetchTask(id, data);

        final PublisherId publisherId = new PublisherId(data.getString("publisher"));
        final JournalId journalId = new JournalId(publisherId, data.getString("journal"));

        final Resource resource = fetcher.fetch(request);
        processor.processIssueList(journalId, resource);
    }
}