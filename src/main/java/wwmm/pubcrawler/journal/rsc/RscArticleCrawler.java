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
package wwmm.pubcrawler.journal.rsc;

import static wwmm.pubcrawler.core.utils.CrawlerConstants.X_XHTML;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nu.xom.Element;
import nu.xom.Nodes;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import wwmm.pubcrawler.core.crawler.ArticleCrawler;
import wwmm.pubcrawler.core.model.ArticleDescription;
import wwmm.pubcrawler.core.model.ArticleReference;
import wwmm.pubcrawler.core.model.*;
import wwmm.pubcrawler.core.model.SupplementaryResourceDescription;
import wwmm.pubcrawler.core.model.FullTextResourceDescription;

/**
 * <p>
 * The <code>RscArticleCrawler</code> class uses a provided DOI to get
 * information about an article that is published in a journal of the 
 * Royal Society of Chemistry.
 * </p>
 * 
 * @author Nick Day
 * @version 1.1
 * 
 */
public class RscArticleCrawler extends ArticleCrawler {

	private static final Logger LOG = Logger.getLogger(RscArticleCrawler.class);

	public RscArticleCrawler() {
		;
	}

	public RscArticleCrawler(DOI doi) {
		super(doi);
	}
	
	protected void readProperties() {
		
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
	@Override
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
		LOG.info("Finished finding article details: "+doi);
		return articleDetails;
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
	@Override
	protected FullTextResourceDescription getFullTextHtmlDetails() {
		String url = getMetaElementContent("citation_fulltext_html_url");
		return new FullTextResourceDescription(url, "HTML", "text/html");
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
	@Override
	protected FullTextResourceDescription getFullTextPdfDetails() {
		String url = getMetaElementContent("citation_pdf_url");
		return new FullTextResourceDescription(url, "PDF", "application/pdf");
	}

	/**
	 * <p>
	 * Gets the details of any supplementary files provided alongside
	 * the published article.
	 * </p>
	 * 
	 * @return a list where each item describes a separate supplementary
	 * data file (as a <code>SupplementaryFileDetails</code> object).
	 * 
	 */
	@Override
	protected List<SupplementaryResourceDescription> getSupplementaryFilesDetails() {
		Nodes linkElements = articleAbstractHtml.query(".//x:a[contains(@href,'/suppdata/')]", X_XHTML);
		if (linkElements.size() == 0) {
			return Collections.EMPTY_LIST;
		}
		List<SupplementaryResourceDescription> sfdList = new ArrayList<SupplementaryResourceDescription>(linkElements.size());
		for (int i = 0; i < linkElements.size(); i++) {
			Element linkElement = (Element)linkElements.get(i);
			String suppFileUrl = linkElement.getAttributeValue("href");
			String linkText = linkElement.getValue().trim();
			String suppFilename = getFilenameFromUrl(suppFileUrl);
			String contentType = httpClient.getContentType(suppFileUrl);
			SupplementaryResourceDescription sfd = new SupplementaryResourceDescription(suppFileUrl, suppFilename, linkText, contentType);
			sfdList.add(sfd);
		}
		return sfdList;
	}

	/**
	 * <p>
	 * Gets the ID of the supplementary file at the publisher's site from
	 * the supplementary file URL.
	 * </p>
	 * 
	 * @param cifUrl - the URL from which to obtain the filename.
	 * 
	 * @return the filename of the supplementary file.
	 */
	private String getFilenameFromUrl(String cifUrl) {
		int idx = cifUrl.lastIndexOf("/");
		String namePlusMime = cifUrl.substring(idx+1);
		int dot = namePlusMime.indexOf(".");
		if (dot == -1) {
			return namePlusMime;
		} else {
			return namePlusMime.substring(0,dot);
		}
	}

	/**
	 * <p>
	 * Gets a authors of the article from the abstract webpage.
	 * </p>
	 * 
	 * @return String containing the article authors.
	 * 
	 */
	@Override
	protected String getAuthors() {
		Nodes authorNds = getMetaElements("DC.Creator");
		StringBuilder authors = new StringBuilder();
		for (int i = 0; i < authorNds.size(); i++) {
			Element authorElement = (Element)authorNds.get(i);
			String author = authorElement.getAttributeValue("content");
			if (!StringUtils.isEmpty(author)) {
				if (i > 0) {
					authors.append(", ");
				}
				authors.append(author);
			}
		}
		return authors.toString();
	}

	/**
	 * <p>
	 * Creates the article bibliographic reference from information found 
	 * on the abstract webpage.
	 * </p>
	 * 
	 * @return the article bibliographic reference.
	 * 
	 */
	@Override
	protected ArticleReference getReference() {
		ArticleReference ar = new ArticleReference();
		ar.setJournalTitle(getJournal());
		ar.setYear(getYear());
		ar.setVolume(getVolume());
		ar.setNumber(getIssueNumber());
		ar.setPages(getPages());
		return ar;
	}
	
	private String getJournal() {
		return getMetaElementContent("citation_journal_title");
	}
	
	private String getYear() {
		String date = getMetaElementContent("citation_date");
		return date.substring(0, date.indexOf("/"));
	}
	
	private String getVolume() {
		return getMetaElementContent("citation_volume");
	}
	
	private String getIssueNumber() {
		return getMetaElementContent("citation_issue");
	}
	
	private String getPages() {
		String startPage = getMetaElementContent("citation_firstpage");
		String endPage = getMetaElementContent("citation_lastpage");
		StringBuilder pages = new StringBuilder();
		pages.append(startPage);
		pages.append(" - ");
		pages.append(endPage);
		return pages.toString();
	}
	
	private String getMetaElementContent(String metaElementName) {
		Nodes metaElements = getMetaElements(metaElementName);
		String content = null;
		if (metaElements.size() > 0) {
			Element el = (Element)metaElements.get(0);
			content =  el.getAttributeValue("content");
			if (content != null) {
				content = content.trim();
			}
		}
		return content;
	}
	
	private Nodes getMetaElements(String metaElementName) {
		return articleAbstractHtml.query(".//x:meta[@name='"+metaElementName+"']", X_XHTML);
	}

	/**
	 * <p>
	 * Gets the article title from the abstract webpage.
	 * </p>
	 * 
	 * @return the article title.
	 * 
	 */
	@Override
	protected String getTitle() {
		return getMetaElementContent("citation_title");
	}

	/**
	 * <p>
	 * Main method meant for demonstration purposes only. Requires
	 * no arguments.
	 * </p>
	 * 
	 */
	public static void main(String[] args) {
		DOI doi = new DOI("http://dx.doi.org/10.1039/C0CC01684E");
		ArticleCrawler crawler = new RscArticleCrawler(doi);
		ArticleDescription ad = crawler.getDetails();
		System.out.println(ad.toString());
	}

}
