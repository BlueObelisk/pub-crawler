package wwmm.pubcrawler.controller;

import com.google.inject.Injector;

import javax.inject.Inject;

/**
 * @author Sam Adams
 */
public class ProcessorFactory {

    private final Injector injector;

    @Inject
    public ProcessorFactory(final Injector injector) {
        this.injector = injector;
    }

    public <Resource> Processor<Resource> createFetcher(final Class<Processor<Resource>> type) {
        return injector.getInstance(type);
    }
    
}
