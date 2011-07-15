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

    public static final Journal A = new Journal(ACTA_ID, "a", "Section A: Foundations of Crystallography");
    public static final Journal B = new Journal(ACTA_ID, "b", "Section B: Structural Science");
    public static final Journal C = new Journal(ACTA_ID, "c", "Section C: Crystal Structure Communications");
    public static final Journal D = new Journal(ACTA_ID, "d", "Section D: Biological Crystallography");
    public static final Journal E = new Journal(ACTA_ID, "e", "Section E: Structure Reports");
    public static final Journal F = new Journal(ACTA_ID, "f", "Section F: Structural Biology and Crystallization Communications");
    public static final Journal J = new Journal(ACTA_ID, "j", "Section J: Applied Crystallography");
    public static final Journal S = new Journal(ACTA_ID, "s", "Section S: Synchrotron Radiation");

    public static List<Journal> getJournals() {
        List<Journal> journals = new ArrayList<Journal>();

        journals.add(A);
        journals.add(B);
        journals.add(C);
        journals.add(D);
        journals.add(E);
        journals.add(F);
        journals.add(J);
        journals.add(S);

        return journals;
    }

}
