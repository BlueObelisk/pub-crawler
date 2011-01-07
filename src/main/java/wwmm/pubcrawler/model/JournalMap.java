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
