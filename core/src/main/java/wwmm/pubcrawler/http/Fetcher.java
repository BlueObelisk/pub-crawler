package wwmm.pubcrawler.http;

import java.io.IOException;

/**
 * @author Sam Adams
 */
public interface Fetcher<Task,Resource> {
    
    Resource fetch(Task task) throws Exception;
    
}
