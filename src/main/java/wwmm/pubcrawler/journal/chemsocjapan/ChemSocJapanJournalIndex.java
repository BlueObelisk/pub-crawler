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
package wwmm.pubcrawler.journal.chemsocjapan;

import wwmm.pubcrawler.core.model.Journal;
import wwmm.pubcrawler.core.model.JournalIndex;


/**
 * <p>
 * The <code>ChemSocJapanJournal</code> enum is meant to 
 * enumerate useful details about journals of interest from 
 * the Chemical Society of Japan.
 * </p>
 * 
 * @author Nick Day
 * @version 1.1
 * 
 */
public class ChemSocJapanJournalIndex {

    private static final JournalIndex INDEX = new JournalIndex();

    public static final Journal CHEMISTRY_LETTERS = INDEX.register("chem-lett", "Chemistry Letters");

    public static JournalIndex getIndex() {
        return INDEX;
    }
    
}
