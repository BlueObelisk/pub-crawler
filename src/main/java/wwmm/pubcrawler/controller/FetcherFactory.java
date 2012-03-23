package wwmm.pubcrawler.controller;

import com.google.inject.Injector;

import javax.inject.Inject;

/**
 * @author Sam Adams
 */
public class FetcherFactory {

    private final Injector injector;

    @Inject
    public FetcherFactory(final Injector injector) {
        this.injector = injector;
    }

    public <Task,Resource> Fetcher<Task,Resource> createFetcher(final Class<Fetcher<Task,Resource>> type) {
        return injector.getInstance(type);
    }
    
}
