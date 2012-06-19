package wwmm.pubcrawler.archivers;

import wwmm.pubcrawler.model.Journal;

/**
 * @author Sam Adams
 */
public interface JournalArchiver {
    
    void archive(Journal journal);
    
}
