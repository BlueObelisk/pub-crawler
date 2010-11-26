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


    public static final Journal ACCOUNTS_OF_CHEMICAL_RESEARCH = INDEX.register("achre4", "Accounts of Chemical Research", 1967);
    public static final Journal ANALYTICAL_CHEMISTRY = INDEX.register("ancham", "Analytical Chemistry", 1928);
    public static final Journal BIOCONJUGATE_CHEMISTRY = INDEX.register("bcches", "Bioconjugate Chemistry", 1989);
    public static final Journal BIOCHEMISTRY = INDEX.register("bichaw", "Biochemistry", 1961);
    public static final Journal BIOMACROMOLECULES = INDEX.register("bomaf6", "Biomacromolecules", 1999);
    public static final Journal CHEMICAL_REVIEWS = INDEX.register("chreay", "Chemical Reviews", 1900);
    public static final Journal CHEMISTRY_OF_MATERIALS = INDEX.register("cmatex", "Chemistry of Materials", 1988);
    public static final Journal CRYSTAL_GROWTH_AND_DESIGN = INDEX.register("cgdefu", "Crystal Growth and Design", 2000);
    public static final Journal ENERGY_AND_FUELS = INDEX.register("enfuem", "Energy and Fuels", 1986);
    public static final Journal INDUSTRIAL_AND_ENGINEERING_CHEMISTRY_RESEARCH = INDEX.register("iecred", "Industrial and Engineering Chemistry Research", 1961);
    public static final Journal INORGANIC_CHEMISTRY = INDEX.register("inocaj", "Inorganic Chemistry", 1961);
    public static final Journal JOURNAL_OF_AGRICULTURAL_AND_FOOD_CHEMISTRY = INDEX.register("jafcau", "Journal of Agricultural and Food Chemistry", 1952);
    public static final Journal JOURNAL_OF_CHEMICAL_AND_ENGINEERING_DATA = INDEX.register("jceaax", "Journal of Chemical & Engineering Data", 1955);
    public static final Journal JOURNAL_OF_THE_AMERICAN_CHEMICAL_SOCIETY = INDEX.register("jacsat", "Journal of the American Chemical Society", 1878);
    public static final Journal JOURNAL_OF_COMBINATORIAL_CHEMISTRY = INDEX.register("jcchff", "Journal of Combinatorial Chemistry", 1998);
    public static final Journal JOURNAL_OF_CHEMICAL_INFORMATION_AND_MODELLING = INDEX.register("jcisd8", "Journal of Chemical Information and Modelling", 1960);
    public static final Journal JOURNAL_OF_MEDICINAL_CHEMISTRY = INDEX.register("jmcmar", "Journal of Medicinal Chemistry", 1957);
    public static final Journal JOURNAL_OF_NATURAL_PRODUCTS = INDEX.register("jnprdf", "Journal of Natural Products", 1937);
    public static final Journal THE_JOURNAL_OF_ORGANIC_CHEMISTRY = INDEX.register("joceah", "The Journal of Organic Chemistry", 1935);
    public static final Journal LANGMUIR = INDEX.register("langd5", "Langmuir", 1984);
    public static final Journal MACROMOLECULES = INDEX.register("mamobx", "Macromolecules", 1967);
    public static final Journal MOLECULAR_PHARMACEUTICS = INDEX.register("mpohbp", "Molecular Pharmaceutics", 2003);
    public static final Journal ORGANIC_LETTERS = INDEX.register("orlef7", "Organic Letters", 1998);
    public static final Journal ORGANIC_PROCESS_AND_RESEARCH_AND_DEVELOPMENT = INDEX.register("oprdfk", "Organic Process and Research and Development", 1996);
    public static final Journal ORGANOMETALLICS = INDEX.register("orgnd7", "Organometallics", 1981);

    public static JournalIndex getIndex() {
        return INDEX;
    }
    
}
