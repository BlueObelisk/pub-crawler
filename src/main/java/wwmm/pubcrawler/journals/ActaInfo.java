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
import wwmm.pubcrawler.model.id.PublisherId;

import java.util.ArrayList;
import java.util.List;

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
public class ActaInfo {

    private static final PublisherId ACTA_ID = new PublisherId("acta");

    public static List<Journal> getJournals() {
        List<Journal> journals = new ArrayList<Journal>();

        journals.add(new Journal(ACTA_ID, "a", "Section A: Foundations of Crystallography"));
        journals.add(new Journal(ACTA_ID, "b", "Section B: Structural Science"));
        journals.add(new Journal(ACTA_ID, "c", "Section C: Crystal Structure Communications"));
        journals.add(new Journal(ACTA_ID, "d", "Section D: Biological Crystallography"));
        journals.add(new Journal(ACTA_ID, "e", "Section E: Structure Reports"));
        journals.add(new Journal(ACTA_ID, "f", "Section F: Structural Biology and Crystallization Communications"));
        journals.add(new Journal(ACTA_ID, "j", "Section J: Applied Crystallography"));
        journals.add(new Journal(ACTA_ID, "s", "Section S: Synchrotron Radiation"));

        return journals;
    }

}
