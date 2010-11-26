/*******************************************************************************
 * Copyright 2010 Nick Day
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
 ******************************************************************************/
package wwmm.pubcrawler.journal.nature;

import java.util.ArrayList;
import java.util.List;

import wwmm.pubcrawler.core.Journal;
import wwmm.pubcrawler.core.JournalMap;

/**
 * <p>
 * The <code>NatureJournal</code> enum is meant to enumerate useful 
 * details about journals of interest from the Nature Publishing
 * Group.
 * </p>
 * 
 * @author Nick Day
 * @version 0.1
 * 
 */
public class NatureJournal extends Journal {
	
	public static final String CHEMISTRY = "nchem";

	static {
    	journalMap = new JournalMap();
	    journalMap.add(new NatureJournal(CHEMISTRY, "Nature Chemistry", 2008));
	}
	
	NatureJournal(String abbreviation, String fullTitle, int volumeOffset) {
		super(abbreviation, fullTitle, volumeOffset);
	}

}
