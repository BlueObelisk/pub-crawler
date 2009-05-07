package wwmm.crawler;

import nu.xom.XPathContext;

public interface CrawlerConstants {
	
	// general namespaces and prefixes
	public static final String XHTML_NS = "http://www.w3.org/1999/xhtml";
	
	// contexts for use in XPath queries
	public static final XPathContext X_XHTML = new XPathContext("x", XHTML_NS);
	
	public static final String CIF_CONTENT_TYPE = "chemical/x-cif";
	
}
