/*
 * Copyright 2010-2011 Nick Day, Sam Adams
 *
 * Licensed under the Apache License, Version 2.0 (the "License"));
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
 * The <code>RscJournal</code> enum is meant to enumerate useful 
 * details about journals of interest from the Royal Society of 
 * Chemistry.
 * </p>
 *
 * @author Nick Day
 * @version 1.1
 *
 */
public class RscInfo {

    private static final PublisherId RSC_ID = new PublisherId("rsc");

    public static List<Journal> getJournals() {
        final List<Journal> journals = new ArrayList<Journal>();

        journals.add(new Journal(RSC_ID, "cc", "Chemical Communications", 1964));
        journals.add(new Journal(RSC_ID, "ce", "CrystEngComm", 1998));
        journals.add(new Journal(RSC_ID, "dt", "Dalton Transactions", 1971));
        journals.add(new Journal(RSC_ID, "gc", "Green Chemistry", 1998));
        journals.add(new Journal(RSC_ID, "jm", "Journal of Materials Chemistry", 1990));
        journals.add(new Journal(RSC_ID, "em", "Journal of Environmental Monitoring", 1998));
        journals.add(new Journal(RSC_ID, "np", "Natural Product Reports", 1983));
        journals.add(new Journal(RSC_ID, "nj", "New Journal of Chemistry", 1977));
        journals.add(new Journal(RSC_ID, "ob", "Organic and Biomolecular Chemistry", 2002));
        journals.add(new Journal(RSC_ID, "cp", "Physical Chemistry Chemical Physics", 1998));

        return journals;
    }

}
