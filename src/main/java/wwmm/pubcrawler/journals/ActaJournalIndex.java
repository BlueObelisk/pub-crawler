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
 * The <code>ActaJournal</code> enum is meant to enumerate useful 
 * details about journals of interest from Acta Crystallographica.
 * </p>
 *
 * @author Nick Day
 * @version 1.1
 *
 */
public class ActaJournalIndex {

    private static final JournalIndex INDEX = new JournalIndex();

    public static final String SECTION_B = "b";
    public static final String SECTION_C = "c";

    public static final Journal ACTA_SECTION_A = INDEX.register("a", "Section A: Foundations of Crystallography");
    public static final Journal ACTA_SECTION_B = INDEX.register("b", "Section B: Structural Science");
    public static final Journal ACTA_SECTION_C = INDEX.register("c", "Section C: Crystal Structure Communications");
    public static final Journal ACTA_SECTION_D = INDEX.register("d", "Section D: Biological Crystallography");
    public static final Journal ACTA_SECTION_E = INDEX.register("e", "Section E: Structure Reports");
    public static final Journal ACTA_SECTION_F = INDEX.register("f", "Section F: Structural Biology and Crystallization Communications");
    public static final Journal ACTA_SECTION_J = INDEX.register("j", "Section J: Applied Crystallography");
    public static final Journal ACTA_SECTION_S = INDEX.register("s", "Section S: Synchrotron Radiation");

    public static JournalIndex getIndex() {
        return INDEX;
    }
    
}
