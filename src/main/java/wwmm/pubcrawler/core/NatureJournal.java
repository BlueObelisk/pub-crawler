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
	
//	CHEMISTRY("nchem", "Nature Chemistry", 2008);
	
	static List<Journal> journals;
	public static NatureJournal CHEMISTRY;
	static {
		journals = new ArrayList<Journal>();
		CHEMISTRY = new NatureJournal("nchem", "Nature Chemistry", 2008);
		journals.add(CHEMISTRY);
	};
	
	public static List<Journal> values() {
		return journals;
	}


	NatureJournal(String abbreviation, String fullTitle, int volumeOffset) {
		super(abbreviation, fullTitle, volumeOffset);
	}

}
