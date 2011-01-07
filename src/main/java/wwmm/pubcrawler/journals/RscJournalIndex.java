/*
 * Copyright 2010-2011 Nick Day, Sam Adams
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
 */
package wwmm.pubcrawler.journals;

import wwmm.pubcrawler.model.Journal;
import wwmm.pubcrawler.model.JournalIndex;

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
public class RscJournalIndex {

    private static final JournalIndex INDEX = new JournalIndex();

    public static final Journal CHEMICAL_COMMUNICATIONS = INDEX.register("CC", "Chemical Communications", 1964);
    public static final Journal CRYST_ENG_COMM = INDEX.register("CE", "CrystEngComm", 1998);
    public static final Journal DALTON_TRANSACTIONS = INDEX.register("DT", "Dalton Transactions", 1971);
    public static final Journal GREEN_CHEMISTRY = INDEX.register("GC", "Green Chemistry", 1998);
    public static final Journal JOURNAL_OF_MATERIALS_CHEMISTRY = INDEX.register("JM", "Journal of Materials Chemistry", 1990);
    public static final Journal JOURNAL_OF_ENVIRONMENTAL_MONITORING = INDEX.register("EM", "Journal of Environmental Monitoring", 1998);
    public static final Journal NATURAL_PRODUCT_REPORTS = INDEX.register("NP", "Natural Product Reports", 1983);
    public static final Journal NEW_JOURNAL_OF_CHEMISTRY = INDEX.register("NJ", "New Journal of Chemistry", 1977);
    public static final Journal ORGANIC_AND_BIOMOLECULAR_CHEMISTRY = INDEX.register("OB", "Organic and Biomolecular Chemistry", 2002);
    public static final Journal PHYSICAL_CHEMISTRY_CHEMICAL_PHYSICS = INDEX.register("CP", "Physical Chemistry Chemical Physics", 1998);

    public static JournalIndex getIndex() {
        return INDEX;
    }
    
}
