package wwmm.pubcrawler.controller;

/**
 * @author Sam Adams
 */
public abstract class Crawler<Task,Resource> {

    private final Fetcher<Task,Resource> fetcher;
    private final Processor<Resource> processor;

    public Crawler(final Fetcher<Task,Resource> fetcher, final Processor<Resource> processor) {
        this.fetcher = fetcher;
        this.processor = processor;
    }

    public void run(final Task task) throws Exception {
        final Resource resource = fetcher.fetch(task);
        processor.process(resource);
    }

}
