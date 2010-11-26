package wwmm.pubcrawler.core;

import java.util.HashMap;
import java.util.Map;

/** list of journals from publisher 
 * 
 * @author pm286
 *
 */
public class JournalMap {
	private Map<String, Journal> map;
	public JournalMap() {
		
	}
	
	public void add(Journal journal) {
		if (map == null) {
			map = new HashMap<String, Journal>();
		}
		map.put(journal.abbreviation, journal);
	}

	public Map<String, Journal> getMap() {
		return map;
	}

	public Journal get(String key) {
		return map.get(key);
	}
}
