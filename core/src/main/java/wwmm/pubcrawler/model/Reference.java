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

/**
 * <p>
 * The <code>ArticleReference</code> class provides 
 * data items that would usually be used in a 
 * bibliographic reference to a published journal
 * article.  Is a simple container class where the only
 * methods are to allow getting and setting of instance
 * variables.
 * </p>
 * 
 * @author Nick Day
 * @version 1.1
 * 
 */
public class Reference extends MongoDBObject {

    public Reference() {
    }

    public Reference(final String journalTitle, final String volume, final String number, final String year, final String pages) {
        setJournalTitle(journalTitle);
        setVolume(volume);
        setNumber(number);
        setYear(year);
        setPages(pages);
    }

    /**
	 * Get the title of the journal the article was 
	 * published in.
	 * 
	 * @return title of the journal the article was 
	 * published in.
	 */
	public String getJournalTitle() {
		return getString("journal");
	}

	/**
	 * Set the title of the journal the article was 
	 * published in.
	 * 
	 * @param journalTitle - the title of the journal the
	 * article was published in.
	 */
	public void setJournalTitle(final String journalTitle) {
		put("journal", journalTitle);
	}


	/**
	 * Get the year the article was published.
	 * 
	 * @return year the article was published.
	 */
	public String getYear() {
		return getString("year");
	}

	/**
	 * Set the year the article was published.
	 * 
	 * @param year the article was published.
	 */
	public void setYear(final String year) {
		put("year", year);
	}


	/**
	 * Get the volume of the journal the article was
	 * published in.
	 * 
	 * @return String of the volume of the journal the 
	 * article was published in.
	 */
	public String getVolume() {
		return getString("volume");
	}

	/**
	 * Set the volume of the journal the article was 
	 * published in.
	 * 
	 * @param volume of the journal the article was 
	 * published in.
	 */
	public void setVolume(final String volume) {
		put("volume", volume);
	}

	/**
	 * Get the identifier of the issue the article
	 * was published in.
	 * 
	 * @return the identifier of the issue the article
	 * was published in.
	 */
	public String getNumber() {
		return getString("number");
	}

	/**
	 * Set the identifier of the issue the article
	 * was published in.
	 * 
	 * @param number - the identifier of the issue the 
	 * article was published in.
	 */
	public void setNumber(final String number) {
		put("number", number);
	}

	/**
	 * Get the pages of the issue that the article is
	 * found on.
	 * 
	 * @return the pages of the issue that the article is
	 * found on.
	 */
	public String getPages() {
		return getString("pages");
	}

	/**
	 * Set the pages of the issue that the article is
	 * found on.
	 * 
	 * @param pages of the issue that the article is 
	 * found on.
	 */
	public void setPages(final String pages) {
		put("pages", pages);
	}

	/**
	 * <p>
	 * Gets a String representation of this object
	 * which is a typical article reference you may 
	 * see in a journal.
	 * </p>
	 * 
	 * @return a String representation of this object
	 * which is a typical article reference you may 
	 * see in a journal.
	 */
	public String getRefString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(getJournalTitle());
		sb.append(", ");
		if (getYear() != null) {
			sb.append(getYear());
			sb.append(", ");
		}
		if (getVolume() != null) {
			sb.append(getVolume());
			if (getNumber() == null) {
				sb.append(", ");
			}
		}
		if (getNumber() != null) {
			if (getVolume() != null) {
				sb.append(" (");
				sb.append(getNumber());
				sb.append(")");
			} else {
				sb.append(getNumber());
			}
			sb.append(", ");
		}
		if (getPages() != null) {
			sb.append(getPages());
		}
		return sb.toString();
	}

}
