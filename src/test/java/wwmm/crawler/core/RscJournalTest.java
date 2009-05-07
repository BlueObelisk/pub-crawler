package wwmm.crawler.core;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import wwmm.crawler.core.RscJournal;

public class RscJournalTest {
	
	@Test
	public void checkJournalsHaveAllParamatersSet() {
		for (RscJournal journal : RscJournal.values()) {
			String abbreviation = journal.getAbbreviation();
			assertNotNull("Journal "+journal.toString()+" has a NULL abbreviation, must be set to a string.", abbreviation);
			String fullTitle = journal.getFullTitle();
			assertNotNull("Journal "+journal.toString()+" has a NULL title, must be set to a string.", fullTitle);
		}
	}

}
