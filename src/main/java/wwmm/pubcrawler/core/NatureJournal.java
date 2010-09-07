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
package wwmm.pubcrawler.core;

/**
 * <p>
 * The <code>NatureJournal</code> enum is meant to enumerate useful 
 * details about journals of interest from the Nature Publishing
 * Group.
 * </p>
 * 
 * @author Nick Day
 * @version 0.1
 * 
 */
public enum NatureJournal {
	CHEMISTRY("nchem", "Nature Chemistry", 2008);

	private final String abbreviation;
	private final String fullTitle;
	private final int volumeOffset;

	NatureJournal(String abbreviation, String fullTitle, int volumeOffset) {
		this.abbreviation = abbreviation;
		this.fullTitle = fullTitle;
		this.volumeOffset = volumeOffset;
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
	 * </p>
	 *
	 * @return int
	 * 
	 */
	public int getVolumeOffset() {
		return this.volumeOffset;
	}
	
	public int getVolumeFromYear(int year) {
		return year - this.volumeOffset;
	}
	
	public int getYearFromVolume(int volume) {
		return this.volumeOffset + volume;
	}

}
