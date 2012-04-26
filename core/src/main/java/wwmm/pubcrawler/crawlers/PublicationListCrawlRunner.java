package wwmm.pubcrawler.crawlers;

import wwmm.pubcrawler.crawler.CrawlRunner;
import wwmm.pubcrawler.crawler.TaskData;
import wwmm.pubcrawler.http.Fetcher;
import wwmm.pubcrawler.http.RequestFactory;
import wwmm.pubcrawler.processors.PublicationListProcessor;

import javax.inject.Inject;

/**
 * @author Sam Adams
 */
public class PublicationListCrawlRunner<Request, Resource> implements CrawlRunner {

    private final Fetcher<Request, Resource> fetcher;
    private final RequestFactory<Request> requestFactory;
    private final PublicationListProcessor<Resource> processor;

    @Inject
    public PublicationListCrawlRunner(final RequestFactory<Request> requestFactory, final Fetcher<Request, Resource> fetcher, final PublicationListProcessor<Resource> processor) {
        this.requestFactory = requestFactory;
        this.fetcher = fetcher;
        this.processor = processor;
    }

    @Override
    public void run(final String id, final TaskData data) throws Exception {
        final Request request = requestFactory.createFetchTask(id, data);
        final Resource resource = fetcher.fetch(request);
        processor.processPublicationList(resource);
    }
}
