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
package wwmm.pubcrawler.utils;

import nu.xom.XPathContext;

/**
 * <p>
 * The <code>CrawlerConstants</code> interface is intended to provide static 
 * access to common constants required by web crawlers (e.g. webpage URLs and
 * namespaces).
 * </p>
 * 
 * @author Nick Day
 * @version 1.1
 * 
 */
public interface CrawlerConstants {

	// webpage URLs
	public static final String ACTA_HOMEPAGE_URL = "http://journals.iucr.org";
	public static final String ACS_HOMEPAGE_URL = "http://pubs.acs.org";
	public static final String CHEMSOCJAPAN_HOMEPAGE_URL = "http://www.jstage.jst.go.jp";
	public static final String ELSEVIER_JOURNAL_URL_PREFIX = "http://www.sciencedirect.com";
	public static final String NATURE_HOMEPAGE_URL = "http://www.nature.com";
	public static final String RSC_PUBS_URL = "http://pubs.rsc.org";
	public static final String RSC_HOMEPAGE_URL = "http://www.rsc.org";
	
	// XML namespaces
	public static final String DC_NS = "http://purl.org/dc/elements/1.1/";
	public static final String RSS_1_NS = "http://purl.org/rss/1.0/";
	public static final String XHTML_NS = "http://www.w3.org/1999/xhtml";
	
	// contexts to be used for XPath queries
	public static final XPathContext X_DC = new XPathContext("dc", DC_NS);
	public static final XPathContext X_RSS1 = new XPathContext("rss1", RSS_1_NS);
	
	public static final String CIF_CONTENT_TYPE = "chemical/x-cif";
	public static final String HTML_CONTENT_TYPE = "text/html";
	public static final String PDF_CONTENT_TYPE = "application/pdf";
	
}
