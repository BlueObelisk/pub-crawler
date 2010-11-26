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
package wwmm.pubcrawler.journal.rsc;

import java.util.ArrayList;
import java.util.List;

import wwmm.pubcrawler.core.Journal;
import wwmm.pubcrawler.core.JournalMap;

/**
 * <p>
 * The <code>RscJournal</code> enum is meant to enumerate useful 
 * details about journals of interest from the Royal Society of 
 * Chemistry.
 * </p>
 * 
 * @author Nick Day
 * @version 1.1
 * 
 */
public class RscJournal extends Journal {
	
	public static final String CHEMCOMM = "cc";
	public static final String DALTON_TRANSACTIONS = "dt";

	static {
    	journalMap = new JournalMap();
	    journalMap.add(new RscJournal(CHEMCOMM, "Chemical Communications", 1964));
	    journalMap.add(new RscJournal("ce", "CrystEngComm", 1998));
	    journalMap.add(new RscJournal(DALTON_TRANSACTIONS, "Dalton Transactions", 1971));
	    journalMap.add(new RscJournal("gc", "Green Chemistry", 1998));
	    journalMap.add(new RscJournal("jm", "Journal of Materials Chemistry", 1990));
	    journalMap.add(new RscJournal("em", "Journal of Environmental Monitoring", 1998));
	    journalMap.add(new RscJournal("np", "Natural Product Reports", 1983));
	    journalMap.add(new RscJournal("nj", "New Journal of Chemistry", 1977));
	    journalMap.add(new RscJournal("ob", "Organic and Biomolecular Chemistry", 2002));
	    journalMap.add(new RscJournal("cp", "Physical Chemistry Chemical Physics", 1998));
	}
	
	RscJournal(String abbreviation, String fullTitle, int volumeOffset) {
		super(abbreviation, fullTitle, volumeOffset);
	}
}
