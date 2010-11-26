package wwmm.pubcrawler.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sea36
 */
public class JournalIndex {

    private final Map<String,Journal> journalMap = new HashMap<String, Journal>();

    public Journal register(String abbreviation, String fullTitle) {
		return register(abbreviation, fullTitle, null);
	}

	public synchronized Journal register(String abbreviation, String fullTitle, Integer volumeOffset) {
		if (getJournalMap().containsKey(abbreviation)) {
            throw new IllegalStateException("Journal '"+abbreviation+"' already exists");
        }
        Journal journal = new Journal(abbreviation, fullTitle, volumeOffset);
        getJournalMap().put(abbreviation, journal);
        return journal;
	}

    private Map<String,Journal> getJournalMap() {
        return journalMap;
    }

    public Journal getJournal(String abbreviation) {
        return journalMap.get(abbreviation);
    }

    public List<Journal> values() {
        return new ArrayList<Journal>(journalMap.values());
    }
    
}
