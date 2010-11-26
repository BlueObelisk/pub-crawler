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

import wwmm.pubcrawler.core.Journal;
import wwmm.pubcrawler.core.JournalIndex;

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
public class AcsJournalIndex {

    private static final JournalIndex INDEX = new JournalIndex();

	public final static String CRYSTAL_GROWTH_AND_DESIGN = "cgdefu";
	public static final String JOURNAL_OF_THE_AMERICAN_CHEMICAL_SOCIETY = "jacsat";
	public static final String THE_JOURNAL_OF_ORGANIC_CHEMISTRY = "joceah";
	
    public static final Journal J_ACS_achre4 = INDEX.register("achre4", "Accounts of Chemical Research", 1967);
    public static final Journal J_ACS_ancham = INDEX.register("ancham", "Analytical Chemistry", 1928);
    public static final Journal J_ACS_bcches = INDEX.register("bcches", "Bioconjugate Chemistry", 1989);
    public static final Journal J_ACS_bichaw = INDEX.register("bichaw", "Biochemistry", 1961);
    public static final Journal J_ACS_bomaf6 = INDEX.register("bomaf6", "Biomacromolecules", 1999);
    public static final Journal J_ACS_chreay = INDEX.register("chreay", "Chemical Reviews", 1900);
    public static final Journal J_ACS_cmatex = INDEX.register("cmatex", "Chemistry of Materials", 1988);
    public static final Journal J_ACS_cgdefu = INDEX.register("cgdefu", "Crystal Growth and Design", 2000);
    public static final Journal J_ACS_enfuem = INDEX.register("enfuem", "Energy and Fuels", 1986);
    public static final Journal J_ACS_iecred = INDEX.register("iecred", "Industrial and Engineering Chemistry Research", 1961);
    public static final Journal J_ACS_inocaj = INDEX.register("inocaj", "Inorganic Chemistry", 1961);
    public static final Journal J_ACS_jafcau = INDEX.register("jafcau", "Journal of Agricultural and Food Chemistry", 1952);
    public static final Journal J_ACS_jceaax = INDEX.register("jceaax", "Journal of Chemical & Engineering Data", 1955);
    public static final Journal J_ACS_jacsat = INDEX.register("jacsat", "Journal of the American Chemical Society", 1878);
    public static final Journal J_ACS_jcchff = INDEX.register("jcchff", "Journal of Combinatorial Chemistry", 1998);
    public static final Journal J_ACS_jcisd8 = INDEX.register("jcisd8", "Journal of Chemical Information and Modelling", 1960);
    public static final Journal J_ACS_jmcmar = INDEX.register("jmcmar", "Journal of Medicinal Chemistry", 1957);
    public static final Journal J_ACS_jnprdf = INDEX.register("jnprdf", "Journal of Natural Products", 1937);
    public static final Journal J_ACS_joceah = INDEX.register("joceah", "The Journal of Organic Chemistry", 1935);
    public static final Journal J_ACS_langd5 = INDEX.register("langd5", "Langmuir", 1984);
    public static final Journal J_ACS_mamobx = INDEX.register("mamobx", "Macromolecules", 1967);
    public static final Journal J_ACS_mpohbp = INDEX.register("mpohbp", "Molecular Pharmaceutics", 2003);
    public static final Journal J_ACS_orlef7 = INDEX.register("orlef7", "Organic Letters", 1998);
    public static final Journal J_ACS_oprdfk = INDEX.register("oprdfk", "Organic Process and Research and Development", 1996);
    public static final Journal J_ACS_orgnd7 = INDEX.register("orgnd7", "Organometallics", 1981);

    public static JournalIndex getIndex() {
        return INDEX;
    }
    
}
