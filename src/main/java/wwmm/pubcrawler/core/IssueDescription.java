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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * Simple container class to describe an issue by
 * the year and its identifier (usually just the
 * issue number).  
 * </p>
 * 
 * @author Nick Day
 * @version 1.1
 */
public class IssueDescription {

	String year;
	String issueId;

	public IssueDescription(String year, String issueId) {
		this.year = year;
		validateYear(year);
		this.issueId = issueId;
	}

	/**
	 * <p>
	 * Gets the year as a four-digit String.
	 * </p>
	 * 
	 * @return the year as a four-digit String.
	 */
	public String getYear() {
		return year;
	}

	/**
	 * <p>
	 * Gets the issue's ID.
	 * </p>
	 * 
	 * @return the issue ID.
	 */
	public String getIssueId() {
		return issueId;
	}

	/**
	 * <p>
	 * Make sure that the year is a four-digit String.
	 * </p>
	 * 
	 * @param String of the year to be validated.
	 */
	private void validateYear(String year) {
		Pattern p = Pattern.compile("^\\d{4}$");
		Matcher m = p.matcher(year);
		if (!m.find()) {
			throw new IllegalStateException("Provided year string is invalid ("+year+"), should be of the form YYYY.");
		}
	}

}
