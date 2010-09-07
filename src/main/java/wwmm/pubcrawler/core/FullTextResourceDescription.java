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
 * Class used to represent details about a full-text resource
 * of a published journal article.
 * </p>
 * 
 * @author Nick Day
 * @version 0.1
 * 
 */
public class FullTextResourceDescription {
	
	private String url;
	private String linkText;
	private String contentType;
	
	// private default constructor so that the other is used
	// to set all instance vars on construction.
	private FullTextResourceDescription() {
		;
	}
	
	/**
	 * <p>
	 * Creates an instance of the FullTextResourceDetails class. 
	 * </p>
	 * 
	 * @param url - the URI that the resource resides at.
	 * @param linkText - the text from the HTML link that points to the resource.
	 * @param contentType - the Content-type of the resource from its HTTP headers.
	 */
	public FullTextResourceDescription(String url, String linkText, String contentType) {
		this.url = url;
		this.linkText = linkText;
		this.contentType = contentType;
	}

	/**
	 * <p>
	 * Gets the text from HTML link that points to the full-text resource.
	 * </p>
	 * 
	 * @return text from the HTML link that points to the
	 * full-text resource.
	 */
	public String getLinkText() {
		return linkText;
	}

	/**
	 * <p>
	 * Gets the URI that points to the full-text resource.
	 * </p>
	 * 
	 * @return the URI that points to the full-text resource.
	 */
	public String getURL() {
		return url;
	}

	/**
	 * <p>
	 * Gets the Content-type of the full-text resource, as described
	 * in its HTTP header.
	 * </p>
	 * 
	 * @return the Content-type of the full-text resource, as described
	 * in its HTTP header.
	 */
	public String getContentType() {
		return contentType;
	}
	
	/**
	 * <p>
	 * Sets the Content-type of the full-text resource, as would be 
	 * described by its HTTP header. 
	 * </p>
	 * 
	 * @param contentType - a String to append to the resource's
	 * content type.
	 */
	public void appendToContentType(String contentType) {
		String newContentType = contentType+"; "+this.contentType;
		this.contentType = newContentType;
	}

}
