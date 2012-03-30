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
import wwmm.pubcrawler.model.id.PublisherId;

import java.util.ArrayList;
import java.util.List;

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
public class AcsInfo {

    private static final PublisherId ACS_ID = new PublisherId("acs");

    public static List<Journal> getJournals() {
        final List<Journal> journals = new ArrayList<Journal>();

        journals.add(new Journal(ACS_ID, "achre4", "Accounts of Chemical Research", 1967));
        journals.add(new Journal(ACS_ID, "ancham", "Analytical Chemistry", 1928));
        journals.add(new Journal(ACS_ID, "bcches", "Bioconjugate Chemistry", 1989));
        journals.add(new Journal(ACS_ID, "bichaw", "Biochemistry", 1961));
        journals.add(new Journal(ACS_ID, "bomaf6", "Biomacromolecules", 1999));
        journals.add(new Journal(ACS_ID, "chreay", "Chemical Reviews", 1900));
        journals.add(new Journal(ACS_ID, "cmatex", "Chemistry of Materials", 1988));
        journals.add(new Journal(ACS_ID, "cgdefu", "Crystal Growth and Design", 2000));
        journals.add(new Journal(ACS_ID, "enfuem", "Energy and Fuels", 1986));
        journals.add(new Journal(ACS_ID, "iecred", "Industrial and Engineering Chemistry Research", 1961));
        journals.add(new Journal(ACS_ID, "inocaj", "Inorganic Chemistry", 1961));
        journals.add(new Journal(ACS_ID, "jafcau", "Journal of Agricultural and Food Chemistry", 1952));
        journals.add(new Journal(ACS_ID, "jceaax", "Journal of Chemical & Engineering Data", 1955));
        journals.add(new Journal(ACS_ID, "jacsat", "Journal of the American Chemical Society", 1878));
        journals.add(new Journal(ACS_ID, "jcchff", "Journal of Combinatorial Chemistry", 1998));
        journals.add(new Journal(ACS_ID, "jcisd8", "Journal of Chemical Information and Modelling", 1960));
        journals.add(new Journal(ACS_ID, "jmcmar", "Journal of Medicinal Chemistry", 1957));
        journals.add(new Journal(ACS_ID, "jnprdf", "Journal of Natural Products", 1937));
        journals.add(new Journal(ACS_ID, "joceah", "The Journal of Organic Chemistry", 1935));
        journals.add(new Journal(ACS_ID, "langd5", "Langmuir", 1984));
        journals.add(new Journal(ACS_ID, "mamobx", "Macromolecules", 1967));
        journals.add(new Journal(ACS_ID, "mpohbp", "Molecular Pharmaceutics", 2003));
        journals.add(new Journal(ACS_ID, "orlef7", "Organic Letters", 1998));
        journals.add(new Journal(ACS_ID, "oprdfk", "Organic Process and Research and Development", 1996));
        journals.add(new Journal(ACS_ID, "orgnd7", "Organometallics", 1981));

        return journals;
    }

}
