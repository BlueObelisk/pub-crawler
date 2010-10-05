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
 * The <code>ActaJournal</code> enum is meant to enumerate useful 
 * details about journals of interest from Acta Crystallographica.
 * </p>
 * 
 * @author Nick Day
 * @version 1.1
 * 
 */
public class ActaJournal extends Journal {
	
/**
	SECTION_A("a", "Section A: Foundations of Crystallography"),
	SECTION_B("b", "Section B: Structural Science"),
	SECTION_C("c", "Section C: Crystal Structure Communications"),
	SECTION_D("d", "Section D: Biological Crystallography"),
	SECTION_E("e", "Section E: Structure Reports"),
	SECTION_F("f", "Section F: Structural Biology and Crystallization Communications"),
	SECTION_J("j", "Section J: Applied Crystallography"),
	SECTION_S("s", "Section S: Synchrotron Radiation");
*/
	
	static List<Journal> journals;
	public static ActaJournal SECTION_B ;
	public static ActaJournal SECTION_C ;

	static {
		journals = new ArrayList<Journal>();
		SECTION_B = new ActaJournal("b", "Section B: Structural Science") ;
		journals.add(SECTION_B);
		SECTION_C = new ActaJournal("c", "Section C: Crystal Structure Communications") ;
		journals.add(SECTION_C);
	};
	
	public static List<Journal> values() {
		return journals;
	}


	ActaJournal(String abbreviation, String fullTitle) {
		super(abbreviation, fullTitle);
	}

}
