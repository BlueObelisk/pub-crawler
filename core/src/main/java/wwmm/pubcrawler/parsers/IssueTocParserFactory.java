package wwmm.pubcrawler.parsers;

import wwmm.pubcrawler.model.id.JournalId;

/**
 * @author Sam Adams
 */
public interface IssueTocParserFactory<T> {
    
    IssueTocParser createIssueTocParser(JournalId journalId, T resource);
    
}
