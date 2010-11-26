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
package wwmm.pubcrawler.journal.acta;

import java.util.ArrayList;
import java.util.List;

import wwmm.pubcrawler.core.Journal;
import wwmm.pubcrawler.core.JournalMap;

/**
 * <p>
 * The <code>ActaJournal</code> enum is meant to enumerate useful 
 * details about journals of interest from Acta Crystallographica.
 * </p>
 * 
 * @author Nick Day
 * @version 1.1
 * 
 */
public class ActaJournal extends Journal {
	
	public static final String SECTION_B = "b";
	public static final String SECTION_C = "c";

	static {
    	journalMap = new JournalMap();
	    journalMap.add(new ActaJournal("a", "Section A: Foundations of Crystallography"));
	    journalMap.add(new ActaJournal("b", "Section B: Structural Science"));
	    journalMap.add(new ActaJournal("c", "Section C: Crystal Structure Communications"));
	    journalMap.add(new ActaJournal("d", "Section D: Biological Crystallography"));
	    journalMap.add(new ActaJournal("e", "Section E: Structure Reports"));
	    journalMap.add(new ActaJournal("f", "Section F: Structural Biology and Crystallization Communications"));
	    journalMap.add(new ActaJournal("j", "Section J: Applied Crystallography"));
	    journalMap.add(new ActaJournal("s", "Section S: Synchrotron Radiation"));
	}
	
	ActaJournal(String abbreviation, String fullTitle) {
		super(abbreviation, fullTitle);
	}

}
