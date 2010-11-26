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

import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;

/**
 * <p>
 * The <code>DOI</code> class provides a representation of a Digital Object 
 * Identifier (DOI).  It is a simple wrapper class based around a URI.
 * </p>
 * 
 * @author Nick Day
 * @version 1.1
 * 
 */
public class DOI {

	private String doiUrl;
	public static final String DOI_SITE_URL = "http://dx.doi.org";

	/**
	 * Blanked default constructor to ensure valid construction.
	 */
	private DOI() {
		;
	}
	
	public DOI(String doiUrl) {
		this();
		this.doiUrl = doiUrl;
		validate();
	}

	/**
	 * <p>
	 * Make sure that the provided <code>URI</code> is a 
	 * valid DOI.
	 * </p>
	 * 
	 * @throws RuntimeException if the provided URI is not
	 * a valid DOI.
	 * 
	 */
	private void validate() {
		if (!doiUrl.startsWith(DOI_SITE_URL) ||
				doiUrl.length() <= DOI_SITE_URL.length()+1) {
			throw new DOIRuntimeException("URI "+doiUrl+" is not a valid DOI.");
		}
	}
	
	/**
	 * <p>
	 * Trims the DOI providers website URL off the start of
	 * the provided DOI (e.g. it will return 10.1021/b789765f
	 * from a DOI of http://dx.doi.org/10.1021/b789765f).
	 * </p>
	 * 
	 * @param doi the DOI for which you want the postfix.
	 * 
	 * @return the postfix of the provided DOI.
	 */
	public String getPostfix() {
		String doiStr = this.toString();
		return doiStr.replaceAll(DOI.DOI_SITE_URL+"/", "");
	}

	/**
	 * <p>
	 * Convenience method for create URIs and handling any
	 * exceptions.
	 * </p>
	 * 
	 * @param url of the resource you want to create a URI for.
	 * 
	 * @return URI for the provided URL.
	 */
	private URI createURI(String url) {
		if (url == null) {
			throw new IllegalArgumentException(
					"Cannot create a URI from a null String.");
		}
		try {
			return new URI(url, false);
		} catch (URIException e) {
			throw new RuntimeException("Problem creating URI from: "+url, e);
		}
	}

	/**
	 * <p>
	 * Get the DOIs URI.
	 * </p>
	 * 
	 * @return the URI for the DOI.
	 */
	public String getURL() {
		return doiUrl;
	}
	
	/**
	 * <p>
	 * Simple method to get the DOIs URI string.
	 * </p>
	 * 
	 */
	@Override
	public String toString() {
		return doiUrl;
	}

	/**
	 * <p>
	 * Method to override equals in <code>Object</code> class.
	 * </p>
	 * 
	 * @return true if the supplied Object is a DOI with the same
	 * URI as this, false if not. 
	 */
	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}
		if((obj == null) || (obj.getClass() != this.getClass())) {
			return false;
		}
		DOI doi = (DOI)obj;
		if (this.doiUrl.equals(doi.toString())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * <p>
	 * Overrides <code>hashCode</code> method in <code>Object</code> class.  
	 * Implemented because I have overriden <code>equals(Object)</code>.
	 * </p>
	 */
	@Override
	public int hashCode() {
		int hash = 7;
		hash = 31 * hash + (null == doiUrl ? 0 : doiUrl.hashCode());
		return hash;
	}

}
