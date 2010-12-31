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
package wwmm.crawler.journal.acs;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import wwmm.pubcrawler.core.model.Journal;
import wwmm.pubcrawler.journal.acs.AcsJournalIndex;

public class AcsJournalTest {
	
	@Test
	public void checkJournalsHaveAllParamatersSet() {
		for (Journal journal : AcsJournalIndex.getIndex().values()) {
			String abbreviation = journal.getAbbreviation();
			assertNotNull("Journal "+journal.toString()+" has a NULL abbreviation, must be set to a string.", abbreviation);
			int volumeOffset = journal.getVolumeOffset();
			assertNotNull("Journal "+journal.toString()+" has a NULL volumeOffset, must be set to a string.", volumeOffset);
			String fullTitle = journal.getFullTitle();
			assertNotNull("Journal "+journal.toString()+" has a NULL title, must be set to a string.", fullTitle);
		}
	}

}