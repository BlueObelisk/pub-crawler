package wwmm.pubcrawler.http;

/**
 * @author Sam Adams
 */
public interface Fetcher<Request,Resource> {
    
    Resource fetch(Request request) throws Exception;
    
}
