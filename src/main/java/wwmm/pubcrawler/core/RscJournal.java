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
package wwmm.pubcrawler.core;

import java.util.ArrayList;
import java.util.List;

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
	/**
	CHEMCOMM("cc", "Chemical Communications", 1964),
	CRYSTENGCOMM("ce", "CrystEngComm", 1998),
	DALTON_TRANSACTIONS("dt", "Dalton Transactions", 1971),
	GREEN_CHEMISTRY("gc", "Green Chemistry", 1998),
	JOURNAL_OF_MATERIALS_CHEMISTRY("jm", "Journal of Materials Chemistry", 1990),
	JOURNAL_OF_ENVIRONMENTAL_MONITORING("em", "Journal of Environmental Monitoring", 1998),
	NATURAL_PRODUCT_REPORTS("np", "Natural Product Reports", 1983),
	NEW_JOURNAL_OF_CHEMISTRY("nj", "New Journal of Chemistry", 1977),
	ORGANIC_AND_BIOMOLECULAR_CHEMISTRY("ob", "Organic and Biomolecular Chemistry", 2002),
	PCCP("cp", "Physical Chemistry Chemical Physics", 1998);
*/
	static List<Journal> journals;
	public static RscJournal CHEMCOMM;
	public static RscJournal DALTON_TRANSACTIONS;
	static {
		journals = new ArrayList<Journal>();
		CHEMCOMM = new RscJournal("cc", "Chemical Communications", 1964);
		journals.add(CHEMCOMM);
		DALTON_TRANSACTIONS = new RscJournal("dt", "Dalton Transactions", 1971);
		journals.add(DALTON_TRANSACTIONS);
	};
	
	public static List<Journal> values() {
		return journals;
	}


	RscJournal(String abbreviation, String fullTitle, int volumeOffset) {
		super(abbreviation, fullTitle, volumeOffset);
	}
}
