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
package wwmm.pubcrawler.journal.acs;

import java.util.ArrayList;
import java.util.List;

import wwmm.pubcrawler.core.Journal;
import wwmm.pubcrawler.core.JournalMap;

/**
 * <p>
 * The <code>AcsJournal</code> enum is meant to enumerate useful 
 * details about journals of interest from the American Chemical
 * Society.
 * </p>
 * 
 * @author Nick Day
 * @version 1.1
 * 
 */
public class AcsJournal extends Journal {

	public final static String CRYSTAL_GROWTH_AND_DESIGN = "cgdefu";
	public static final String JOURNAL_OF_THE_AMERICAN_CHEMICAL_SOCIETY = "jacsat";
	public static final String THE_JOURNAL_OF_ORGANIC_CHEMISTRY = "joceah";
	static {
    	journalMap = new JournalMap();
		journalMap.add( new AcsJournal("achre4", "Accounts of Chemical Research", 1967));
		journalMap.add( new AcsJournal("ancham", "Analytical Chemistry", 1928));
		journalMap.add( new AcsJournal("bcches", "Bioconjugate Chemistry", 1989));
		journalMap.add( new AcsJournal("bichaw", "Biochemistry", 1961));
		journalMap.add( new AcsJournal("bomaf6", "Biomacromolecules", 1999));
		journalMap.add( new AcsJournal("chreay", "Chemical Reviews", 1900));
		journalMap.add( new AcsJournal("cmatex", "Chemistry of Materials", 1988));
		journalMap.add( new AcsJournal("cgdefu", "Crystal Growth and Design", 2000));
		journalMap.add( new AcsJournal("enfuem", "Energy and Fuels", 1986));
		journalMap.add( new AcsJournal("iecred", "Industrial and Engineering Chemistry Research", 1961));
		journalMap.add( new AcsJournal("inocaj", "Inorganic Chemistry", 1961));
		journalMap.add( new AcsJournal("jafcau", "Journal of Agricultural and Food Chemistry", 1952));
		journalMap.add( new AcsJournal("jceaax", "Journal of Chemical & Engineering Data", 1955));
		journalMap.add( new AcsJournal("jacsat", "Journal of the American Chemical Society", 1878));
		journalMap.add( new AcsJournal("jcchff", "Journal of Combinatorial Chemistry", 1998));
		journalMap.add( new AcsJournal("jcisd8", "Journal of Chemical Information and Modelling", 1960));
		journalMap.add( new AcsJournal("jmcmar", "Journal of Medicinal Chemistry", 1957));
		journalMap.add( new AcsJournal("jnprdf", "Journal of Natural Products", 1937));
		journalMap.add( new AcsJournal("joceah", "The Journal of Organic Chemistry", 1935));
		journalMap.add( new AcsJournal("langd5", "Langmuir", 1984));
		journalMap.add( new AcsJournal("mamobx", "Macromolecules", 1967));
		journalMap.add( new AcsJournal("mpohbp", "Molecular Pharmaceutics", 2003));
		journalMap.add( new AcsJournal("orlef7", "Organic Letters", 1998));
		journalMap.add( new AcsJournal("oprdfk", "Organic Process and Research and Development", 1996));
		journalMap.add( new AcsJournal("orgnd7", "Organometallics", 1981));
    }

	AcsJournal(String abbreviation, String fullTitle, int volumeOffset) {
		super(abbreviation, fullTitle, volumeOffset);
	}
	
}
