/*
 * Copyright 2010-2011 Nick Day, Sam Adams
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package wwmm.pubcrawler.model;

import java.util.*;

/**
 * @author sea36
 */
public class JournalIndex {

    private final Map<String,Journal> journalMap = new LinkedHashMap<String, Journal>();

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
