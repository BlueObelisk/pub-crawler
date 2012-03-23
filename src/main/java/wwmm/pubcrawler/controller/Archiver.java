package wwmm.pubcrawler.controller;

/**
 * @author Sam Adams
 */
public interface Archiver<T> {
    
    void archive(T object);
    
}
