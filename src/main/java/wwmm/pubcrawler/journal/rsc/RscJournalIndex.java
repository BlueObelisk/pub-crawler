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

import wwmm.pubcrawler.core.model.Journal;
import wwmm.pubcrawler.core.model.JournalIndex;

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

    public static final Journal CHEMICAL_COMMUNICATIONS = INDEX.register("cc", "Chemical Communications", 1964);
    public static final Journal CRYST_ENG_COMM = INDEX.register("ce", "CrystEngComm", 1998);
    public static final Journal DALTON_TRANSACTIONS = INDEX.register("dt", "Dalton Transactions", 1971);
    public static final Journal GREEN_CHEMISTRY = INDEX.register("gc", "Green Chemistry", 1998);
    public static final Journal JOURNAL_OF_MATERIALS_CHEMISTRY = INDEX.register("jm", "Journal of Materials Chemistry", 1990);
    public static final Journal JOURNAL_OF_ENVIRONMENTAL_MONITORING = INDEX.register("em", "Journal of Environmental Monitoring", 1998);
    public static final Journal NATURAL_PRODUCT_REPORTS = INDEX.register("np", "Natural Product Reports", 1983);
    public static final Journal NEW_JOURNAL_OF_CHEMISTRY = INDEX.register("nj", "New Journal of Chemistry", 1977);
    public static final Journal ORGANIC_AND_BIOMOLECULAR_CHEMISTRY = INDEX.register("ob", "Organic and Biomolecular Chemistry", 2002);
    public static final Journal PHYSICAL_CHEMISTRY_CHEMICAL_PHYSICS = INDEX.register("cp", "Physical Chemistry Chemical Physics", 1998);

    public static JournalIndex getIndex() {
        return INDEX;
    }
    
}
