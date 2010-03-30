package wwmm.pubcrawler.core;

import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;

/**
 * <p>
 * Class used to represent details about a resource that is 
 * provided as supplementary information to a published 
 * journal article.
 * </p>
 * 
 * @author Nick Day
 * @version 0.1
 * 
 */
public class SupplementaryResourceDescription {

	private URI uri;
	private String fileId;
	private String linkText;
	private String contentType;
	
	// private default constructor so that the other is used
	// to set all instance vars on construction.
	private SupplementaryResourceDescription() {
		;
	}
	
	/**
	 * <p>
	 * Creates an instance of the SupplementaryResourceDetails class. 
	 * NOTE that the provided fileId MUST be part of the provided URI.
	 * The reason this must be provided is that there is no way of
	 * automatically asserting the files ID from the URI alone.
	 * </p>
	 * 
	 * @param uri - the URI that the resource resides at.
	 * @param fileId - the site-specific identifier for the resource.
	 * @param linkText - the text from the HTML link that points to the resource.
	 * @param contentType - the Content-type of the resource from its HTTP headers.
	 */
	public SupplementaryResourceDescription(URI uri, String fileId, String linkText, String contentType) {
		this.uri = uri;
		this.fileId = fileId;
		this.linkText = linkText;
		this.contentType = contentType;
		validate();
	}
	
	/**
	 * Make sure that the provided file ID is part of the supplementary
	 * files URL. 
	 */
	private void validate() {
		if (!getUriString().contains(fileId)) {
			throw new RuntimeException("The provided filename must be " +
					"part of the provided URI.");
		}
	}

	/**
	 * <p>
	 * Gets the text from HTML link that points to the supplementary resource.
	 * </p>
	 * 
	 * @return text from the HTML link that points to the
	 * supplementary resource.
	 */
	public String getLinkText() {
		return linkText;
	}

	/**
	 * <p>
	 * Gets the URI that points to the supplementary resource.
	 * </p>
	 * 
	 * @return the URI that points to the supplementary resource.
	 */
	public URI getURI() {
		return uri;
	}
	
	/**
	 * <p>
	 * Gets the URI that points to the supplementary resource in
	 * String form.
	 * </p>
	 * 
	 * @return String form of the URI that points to the
	 * supplementary resource.
	 */
	public String getUriString() {
		String uriStr = null;
		try {
			uriStr = uri.getURI();
		} catch (URIException e) {
			throw new RuntimeException("Exception getting string for URI: "+uri);
		}
		return uriStr;
	}
	
	/**
	 * <p>
	 * Gets the site-specific ID of the supplementary resource.
	 * </p>
	 * 
	 * @return the site-specific ID of the supplementary resource.
	 */
	public String getFileId() {
		return fileId;
	}

	/**
	 * <p>
	 * Gets the Content-type of the supplementary resource, as described
	 * in its HTTP header.
	 * </p>
	 * 
	 * @return the Content-type of the supplementary resource, as described
	 * in its HTTP header.
	 */
	public String getContentType() {
		return contentType;
	}
	
	/**
	 * <p>
	 * Sets the Content-type of the supplementary resource, as would be 
	 * described by its HTTP header. 
	 * </p>
	 * 
	 * @param contentType - a String to append to the resource's
	 * content-type.
	 */
	public void appendToContentType(String contentType) {
		String newContentType = contentType+"; "+this.contentType;
		this.contentType = newContentType;
	}
	
}
