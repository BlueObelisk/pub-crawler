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

package wwmm.pubcrawler.model;

public class Journal {

	public final String abbreviation;
	protected final String fullTitle;
	private Integer volumeOffset = null;

	public Journal(String abbreviation, String fullTitle) {
		this.abbreviation = abbreviation;
		this.fullTitle = fullTitle;
	}

	public Journal(String abbreviation, String fullTitle, Integer volumeOffset) {
		this(abbreviation, fullTitle);
		this.volumeOffset  = volumeOffset;
	}
	
	/**
	 * <p>
	 * Gets the complete journal title.
	 * </p>
	 * 
	 * @return String of the complete journal title.
	 * 
	 */
	public String getFullTitle() {
		return this.fullTitle;
	}

	/**
	 * <p>
	 * Gets the journal abbreviation (as used by the publisher
	 * on their website).
	 * </p>
	 * 
	 * @return String of the journal abbreviation.
	 * 
	 */
	public String getAbbreviation() {
		return this.abbreviation;
	}

	/**
	 * <p>
	 * Gets an <code></code> which describes the relationship 
	 * between a journals year and volume i.e. if you know 
	 * the journal year is 2009 and the <code>volumeOffset</code> 
	 * is 2000, then the volume of the journal in 2009 is 9. 
	 * Magic.
	 * PMR: except when it isn't - like multi-issue years
	 * </p>
	 *
	 * @return int
	 * 
	 */

	public Integer getVolumeOffset() {
		return volumeOffset;
	}

	public int getVolumeFromYear(int year) {
		return year - this.volumeOffset;
	}

	public int getYearFromVolume(int volume) {
		return this.volumeOffset + volume;
	}

//	public static Collection<Journal> values() {
//		return journalMap.getMap().values();
//	}
//
//	public static Journal getJournal(String abbreviation) {
//		return journalMap.get(abbreviation);
//	}

}
