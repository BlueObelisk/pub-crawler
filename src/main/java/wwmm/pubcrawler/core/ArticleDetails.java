package wwmm.pubcrawler.core;

import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * The <code>ArticleDetails</code> class provides 
 * a description of a published journal article.  Is a
 * simple container class where the only methods are 
 * getters and setters for each instance variable.
 * </p>
 * 
 * @author Nick Day
 * @version 1.1
 * 
 */
public class ArticleDetails {

	private DOI doi;
	private boolean doiResolved;

	private String title;
	private String authors;
	private ArticleReference reference;
	private List<FullTextResourceDetails> fullTexts;
	private List<SupplementaryResourceDetails> suppFiles;
	
	// assume initially that the article has been published,
	// it is more likely than not.
	private boolean hasBeenPublished = true;
	
	public ArticleDetails() {
		;
	}

	/**
	 * <p>
	 * States whether or not the article has been 
	 * published, i.e. made available at the publisher's
	 * website with a proper bibliographic reference.
	 * </p>
	 * 
	 * @return boolean of whether the article has been published
	 */
	public boolean hasBeenPublished() {
		return hasBeenPublished;
	}

	/**
	 * <p>
	 * Set whether or not the article has been 
	 * published, i.e. made available at the publisher's
	 * website with a proper bibliographic reference.
	 * </p>
	 * 
	 * @param hasBeenPublished - boolean of whether the article
	 * has been published.
	 */
	public void setHasBeenPublished(boolean hasBeenPublished) {
		this.hasBeenPublished = hasBeenPublished;
	}

	/**
	 * <p>
	 * Get whether or not the DOI for the article resolves
	 * at the DOI providing service (as of 24/02/2009 it is
	 * http://dx.doi.org).
	 * </p>
	 * 
	 * @return boolean of whether the DOI for the article 
	 * resolves at the providing service.
	 */
	public boolean isDoiResolved() {
		return doiResolved;
	}

	/**
	 * <p>
	 * State whether or not the DOI for the article resolves
	 * at the DOI providing service (as of 24/02/2009 it is
	 * http://dx.doi.org).
	 * </p>
	 * 
	 * @param doiResolved - boolean of whether the DOI for the
	 * article resolves at the providing service.
	 */
	public void setDoiResolved(boolean doiResolved) {
		this.doiResolved = doiResolved;
	}
	
	/**
	 * <p>
	 * Gets details for any full-text resources for this article.
	 * </p>
	 * 
	 * @return the details for any full-text resources provided
	 * for this article.
	 */
	public List<FullTextResourceDetails> getFullTextResources() {
		return fullTexts;
	}

	/**
	 * <p>
	 * Get details for any files that are provided as 
	 * supplementary information to the article. 
	 * </p>
	 * 
	 * @return details for each supplementary file to the
	 * article.
	 */
	public List<SupplementaryResourceDetails> getSupplementaryResources() {
		return suppFiles;
	}
	
	/**
	 * <p>
	 * Sets details for any full-text resources that are provided
	 * for this article.
	 * </p>
	 * 
	 * @param fullTexts - details for each full-text resources
	 * provided for this article.
	 */
	public void setFullTextResources(List<FullTextResourceDetails> fullTexts) {
		this.fullTexts = fullTexts;
	}

	/**
	 * <p>
	 * Set details for any files that are provided as 
	 * supplementary information to the article. 
	 * </p>
	 * 
	 * @param suppFiles - details for each supplementary
	 * file to the article.
	 */
	public void setSupplementaryResources(List<SupplementaryResourceDetails> suppFiles) {
		this.suppFiles = suppFiles;
	}

	/**
	 * <p>
	 * Set the article Digital Object Identifier.
	 * </p>
	 * 
	 * @param doi - the Digital Object Identifier of the
	 * article.
	 */
	public void setDoi(DOI doi) {
		this.doi = doi;
	}

	/**
	 * <p>
	 * Get the article Digital Object Identifier.
	 * </p>
	 * 
	 * @return Digital Object Identifier for the article.
	 */
	public DOI getDoi() {
		return doi;
	}

	/**
	 * <p>
	 * Get the title of the article.
	 * </p>
	 * 
	 * @return String title of the article.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * <p>
	 * Get the data-items that constitute a bibliographic 
	 * reference for the article.
	 * </p>
	 * 
	 * @return bibliographic reference for the article.
	 */
	public ArticleReference getReference() {
		return reference;
	}

	/**
	 * <p>
	 * Get the names of the authors of the article.
	 * </p>
	 * 
	 * @return String containing the authors of the article.
	 */
	public String getAuthors() {
		return authors;
	}

	/**
	 * <p>
	 * Set the title of the article.
	 * </p>
	 * 
	 * @param title of the article.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * <p>
	 * Provide data items that constitute a bibliographic 
	 * reference to the article.
	 * </p>
	 * 
	 * @param reference - contains data-items that make up
	 * a bibliographic reference to the article.
	 */
	public void setReference(ArticleReference reference) {
		this.reference = reference;
	}

	/**
	 * <p>
	 * Set a <code>String</code> containing the names
	 * of the authors of the article.
	 * </p>
	 * 
	 * @param authors of the article.
	 */
	public void setAuthors(String authors) {
		this.authors = authors;
	}

	/**
	 * <p>
	 * Intended only for debugging.
	 *
	 * Here, if they are not null, the contents of every field 
	 * are placed into the result, with one field per line.
	 * </p>
	 * 
	 */
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		String NEW_LINE = System.getProperty("line.separator");

		result.append(this.getClass().getName()+" Object {"+NEW_LINE);
		if (doi != null) {
			result.append("  DOI: "+doi+NEW_LINE);
		}

		if (!isDoiResolved()) {
			result.append("  ### DOI DID NOT RESOLVE - NO ARTICLE DETAILS OBTAINED ###"+NEW_LINE);
		} else {
			if (StringUtils.isNotEmpty(title)) {
				result.append("  Title: "+title+NEW_LINE);
			}
			if (StringUtils.isNotEmpty(authors)) {
				result.append("  Authors: "+authors+NEW_LINE);
			}
			result.append("  Bib data: "+NEW_LINE);
			if (StringUtils.isNotEmpty(reference.getJournalTitle())) {
				result.append("    Journal: "+reference.getJournalTitle()+NEW_LINE);
			}
			if (StringUtils.isNotEmpty(reference.getYear())) {
				result.append("    Year: "+reference.getYear()+NEW_LINE);
			}
			if (StringUtils.isNotEmpty(reference.getVolume())) {
				result.append("    Volume: "+reference.getVolume()+NEW_LINE);
			}
			if (StringUtils.isNotEmpty(reference.getNumber())) {
				result.append("    Number: "+reference.getNumber()+NEW_LINE);
			}
			if (StringUtils.isNotEmpty(reference.getPages())) {
				result.append("    Pages: "+reference.getPages()+NEW_LINE);
			}
			result.append("  Full-text file details:"+NEW_LINE);
			int fcount = 1;
			for (FullTextResourceDetails ftrd : fullTexts) {
				if (ftrd.getURI() != null) {
					result.append("    URI: "+ftrd.getURI()+NEW_LINE);
				}
				if (!StringUtils.isEmpty(ftrd.getLinkText())) {
					result.append("    Link text: "+ftrd.getLinkText()+NEW_LINE);
				}
				if (!StringUtils.isEmpty(ftrd.getContentType())) {
					result.append("    Content-type: "+ftrd.getContentType()+NEW_LINE);
				}
				if (fullTexts.size() > 1 && fcount < fullTexts.size()) {
					result.append("    -----"+NEW_LINE);
				}
				fcount++;
			}
			result.append("  Supplementary file details:"+NEW_LINE);
			int scount = 1;
			for (SupplementaryResourceDetails sf : suppFiles) {
				if (sf.getURI() != null) {
					result.append("    URI: "+sf.getURI()+NEW_LINE);
				}
				if (!StringUtils.isEmpty(sf.getLinkText())) {
					result.append("    Link text: "+sf.getLinkText()+NEW_LINE);
				}
				if (!StringUtils.isEmpty(sf.getContentType())) {
					result.append("    Content-type: "+sf.getContentType()+NEW_LINE);
				}
				if (suppFiles.size() > 1 && scount < suppFiles.size()) {
					result.append("    -----"+NEW_LINE);
				}
				scount++;
			}
			if (suppFiles.size() == 0) {
				result.append("    --no supplementary files--"+NEW_LINE);
			}
			if (!hasBeenPublished) {
				result.append("    ### THIS ARTICLE HAS YET TO BE PUBLISHED - REFERENCE DETAILS WILL BE INCOMPLETE ###\n");
			}
		}
		result.append("}"+NEW_LINE);

		return result.toString();
	}

}
