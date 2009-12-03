package wwmm.pubcrawler.core;

import java.net.URI;

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
public class FullTextResourceDetails {
	
	private URI uri;
	private String linkText;
	private String contentType;
	
	// private default constructor so that the other is used
	// to set all instance vars on construction.
	private FullTextResourceDetails() {
		;
	}
	
	/**
	 * <p>
	 * Creates an instance of the FullTextResourceDetails class. 
	 * </p>
	 * 
	 * @param uri - the URI that the resource resides at.
	 * @param linkText - the text from the HTML link that points to the resource.
	 * @param contentType - the Content-type of the resource from its HTTP headers.
	 */
	public FullTextResourceDetails(URI uri, String linkText, String contentType) {
		this.uri = uri;
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
	public URI getURI() {
		return uri;
	}
	
	/**
	 * <p>
	 * Gets the URI that points to the full-text resource in
	 * String form.
	 * </p>
	 * 
	 * @return String form of the URI that points to the
	 * full-text resource.
	 */
	public String getUriString() {
		return uri.toString();
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
