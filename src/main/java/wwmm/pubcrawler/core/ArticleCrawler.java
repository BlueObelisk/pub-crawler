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

import static wwmm.pubcrawler.core.CrawlerConstants.X_XHTML;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import wwmm.pubcrawler.Utils;

import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Node;
import nu.xom.Nodes;

/**
 * <p>
 * The abstract <code>ArticleCrawler</code> class provides a base implementation
 * for crawling the webpages of published articles.  It is assumed that all 
 * articles have a DOI, which can be used to find all the necessary details.  Hence,
 * this class has a single constructor which takes a DOI parameter.
 * </p>
 * 
 * @author Nick Day
 * @version 1.1
 * 
 */
public abstract class ArticleCrawler extends Crawler {
	protected static final String APPLICATION_PDF = "application/pdf";
	protected static final String TEXT_HTML = "text/html";

	private static Logger LOG = Logger.getLogger(ArticleCrawler.class);

	protected DOI doi;
	protected Document articleAbstractHtml;
	protected boolean doiResolved;
	protected ArticleDescription articleDetails;
	protected BibtexTool bibtexTool;

	protected ArticleInfo articleInfo;
	
	public ArticleCrawler() {
		this.articleInfo = new ArticleInfo();
		readProperties();
	}

	public ArticleCrawler(DOI doi) {
		this();
		setDOI(doi);
	}
	
	protected abstract void readProperties();

	/**
	 * <p>
	 * Uses the provided <code>DOI</code> to initialise some instance 
	 * variables, including:
	 *  1. getting the article abstract webpage HTML from the provided DOI
	 *     and setting it as <code>articleAbstractHtml</code>.
	 *  2. checking whether the provided DOI has resolved (NB. if a DOI does not 
	 *     resolve, then http://dx.doi.org still returns a webpage with HTTP 
	 *     status 200 (OK).  So to check if something has gone awry, we need to 
	 *     parse the HTML to check for the error message =0 ). 
	 *  3. adds a boolean flag to an <code>ArticleDetails</code> instance, 
	 *     which should be completed by <code>getDetails()</code> of the 
	 *     implementing subclass of this.
	 * </p>
	 * 
	 */
	public void setDOI(DOI doi) {
		this.doi = null;
		this.articleAbstractHtml = null;
		this.articleDetails = new ArticleDescription();
		this.bibtexTool = null;
		this.doiResolved = false;
		
		this.doi = doi;
		articleAbstractHtml = httpClient.getResourceHTML(doi.getURL());
		setHasDoiResolved();
		articleDetails.setDoiResolved(doiResolved);
		articleDetails.setDoi(doi);
	}
	
	public DOI getDOI() {
		return this.doi;
	}

	/**
	 * <p>
	 * Sets a boolean which specifies whether the provided DOI resolves 
	 * at http://dx.doi.org.
	 * </p>
	 * 
	 */
	private void setHasDoiResolved() {
		Nodes nodes = articleAbstractHtml.query(".//x:body[contains(.,'Error - DOI Not Found')]", X_XHTML);
		if (nodes.size() > 0) {
			doiResolved = false;
		} else {
			doiResolved = true;
		}
	}
	
	protected void applyBibtexTool() {
		setBibtexTool();
		if (bibtexTool != null) {
			String title = bibtexTool.getTitle();
			ArticleReference ref = bibtexTool.getReference();
			articleDetails.setHasBeenPublished(true);
			String authors = bibtexTool.getAuthors();
			articleDetails.setTitle(title);
			articleDetails.setReference(ref);
			articleDetails.setAuthors(authors);
			List<SupplementaryResourceDescription> suppFiles = getSupplementaryFilesDetails();
			articleDetails.setSupplementaryResources(suppFiles);
		}
	}
	
	protected void setBibtexTool() {
	}

	protected List<SupplementaryResourceDescription> getSupplementaryFilesDetails() {
		return null;
	}

	protected void addFullTextPdfDetails(List<FullTextResourceDescription> fullTextResources) {
		FullTextResourceDescription fullTextPdfDetails = getFullTextPdfDetails();
		if (fullTextPdfDetails != null) {
			fullTextResources.add(fullTextPdfDetails);
		}
	}

	protected void addFullTextEnhancedPdfDetails(List<FullTextResourceDescription> fullTextResources) {
		FullTextResourceDescription fullTextEnhancedPdfDetails = getFullTextEnhancedPdfDetails();
		if (fullTextEnhancedPdfDetails != null) {
			fullTextResources.add(fullTextEnhancedPdfDetails);
		}
	}

	protected void addFullTextHtmlDetails(List<FullTextResourceDescription> fullTextResources) {
		FullTextResourceDescription fullTextHtmlDetails = getFullTextHtmlDetails();
		if (fullTextHtmlDetails != null) {
			fullTextResources.add(fullTextHtmlDetails);
		}
	}
	
	/**
	 * <p>
	 * Gets the details of any full-text resources provided for
	 * the article.
	 * </p>
	 * 
	 * @return list containing the details of each full-text
	 * resource provided for the article.
	 */
	protected List<FullTextResourceDescription> getFullTextResources() {
		List<FullTextResourceDescription> fullTextResources = new ArrayList<FullTextResourceDescription>();
		addFullTextHtmlDetails(fullTextResources);
		addFullTextPdfDetails(fullTextResources);
		addFullTextEnhancedPdfDetails(fullTextResources);
		return fullTextResources;
	}

	protected FullTextResourceDescription getFullTextResourceDescription(
			String xpath, String linkUrl, String mimeType) {
		FullTextResourceDescription ftrd = null;
		if (xpath != null) {
			List<Node> fullTextPdfLinks = Utils.queryHTML(articleAbstractHtml, xpath);
			if (fullTextPdfLinks.size() == 0) {
				LOG.warn("Problem getting full text PDF link: "+doi);
				return null;
			}
			Element fullTextLink = (Element)fullTextPdfLinks.get(0);
			String linkText = fullTextLink.getValue().trim();
			String fullTextPdfUrl = linkUrl+fullTextLink.getAttributeValue("href");
			ftrd = new FullTextResourceDescription(fullTextPdfUrl, linkText, mimeType);
		}
		return ftrd;
	}

	/**
	 * <p>
	 * Gets the details about the full-text PDF resource for 
	 * this article.
	 * </p>
	 * 
	 * @return details about the full-text PDF resource for this
	 * article.
	 */
	protected FullTextResourceDescription getFullTextPdfDetails() {
		return getFullTextResourceDescription(
				articleInfo.fullTextPdfXpath, articleInfo.fullTextPdfLinkUrl, APPLICATION_PDF);
	}

	/**
	 * <p>
	 * Gets the details about the full-text enhanced PDF resource for 
	 * this article.
	 * </p>
	 * 
	 * @return details about the full-text enhanced PDF resource for this
	 * article.
	 */
	protected FullTextResourceDescription getFullTextEnhancedPdfDetails() {
		return getFullTextResourceDescription(
				articleInfo.fullTextEnhancedPdfXpath, articleInfo.fullTextPdfLinkUrl, APPLICATION_PDF);
	}

	/**
	 * <p>
	 * Gets the details about the full-text HTML resource for 
	 * this article.
	 * </p>
	 * 
	 * @return details about the full-text HTML resource for this
	 * article.
	 */
	protected FullTextResourceDescription getFullTextHtmlDetails() {
		return getFullTextResourceDescription(
				articleInfo.fullTextHtmlXpath, articleInfo.fullTextHtmlLinkUrl, TEXT_HTML);
	}

	/**
	 * <p>
	 * Crawls the article abstract webpage for information, which is 
	 * returned in an ArticleDetails object.
	 * </p> 
	 * 
	 * @return ArticleDetails object containing important details about
	 * the article (e.g. title, authors, reference, supplementary 
	 * files).
	 * 
	 */
	public ArticleDescription getDetails() {
		if (!doiResolved) {
			LOG.warn("The DOI provided for the article abstract ("+doi.toString()+") has not resolved so we cannot get article details.");
			return articleDetails;
		}
		LOG.info("Starting to find article details: "+doi);
		List<FullTextResourceDescription> fullTextResources = getFullTextResources();
		articleDetails.setFullTextResources(fullTextResources);
		String title = getTitle();
		articleDetails.setTitle(title);
		String authors = getAuthors();
		articleDetails.setAuthors(authors);
		ArticleReference ref = getReference();
		articleDetails.setReference(ref);
		List<SupplementaryResourceDescription> suppFiles = getSupplementaryFilesDetails();
		articleDetails.setSupplementaryResources(suppFiles);
		articleDetails.setHasBeenPublished(true);
		LOG.debug("Finished finding article details: "+doi.toString());
		return articleDetails;
	}

	protected String getTitle() {return null;}
	protected String getAuthors() {return null;}
	protected ArticleReference getReference() {return null;}
	
}
