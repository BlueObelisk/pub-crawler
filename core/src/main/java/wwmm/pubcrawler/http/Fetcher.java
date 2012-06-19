package wwmm.pubcrawler.http;

/**
 * @author Sam Adams
 */
public interface Fetcher<R, T> {
    
    T fetch(R request) throws Exception;
    
}
