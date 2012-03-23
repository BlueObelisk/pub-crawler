package wwmm.pubcrawler.controller;

/**
 * @author Sam Adams
 */
public interface Task<Resource> {
    
    Class<Fetcher<Task,Resource>> getFetcherType();
    
    Class<Processor<Resource>> getProcessorType();
    
}
