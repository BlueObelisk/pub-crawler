package wwmm.pubcrawler.archivers;

/**
 * @author Sam Adams
 */
public interface Archiver<T> {
    
    void archive(T object);
    
}
