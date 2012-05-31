package wwmm.pubcrawler.crawlers;

import wwmm.pubcrawler.http.Fetcher;
import wwmm.pubcrawler.http.RequestFactory;
import wwmm.pubcrawler.tasks.HttpCrawlTaskData;
import wwmm.pubcrawler.tasks.TaskRunner;

/**
 * @author Sam Adams
 */
public class CrawlTaskRunner<TaskData extends HttpCrawlTaskData, Request, Resource> implements TaskRunner<TaskData> {

    private final Fetcher<Request, Resource> fetcher;
    private final RequestFactory<Request> requestFactory;
    private final ResourceProcessor<Resource, TaskData> processor;

    public CrawlTaskRunner(final Fetcher<Request, Resource> fetcher, final RequestFactory<Request> requestFactory, final ResourceProcessor<Resource, TaskData> processor) {
        this.fetcher = fetcher;
        this.requestFactory = requestFactory;
        this.processor = processor;
    }

    @Override
    public void run(final String taskId, final TaskData data) throws Exception {
        final Request request = requestFactory.createFetchTask(taskId, data);
        final Resource resource = fetcher.fetch(request);
        processor.process(taskId, data, resource);
    }
}
