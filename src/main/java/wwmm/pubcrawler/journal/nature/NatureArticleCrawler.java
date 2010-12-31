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
package wwmm.pubcrawler.journal.nature;

import static wwmm.pubcrawler.core.utils.CrawlerConstants.NATURE_HOMEPAGE_URL;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Node;
import nu.xom.Nodes;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import wwmm.pubcrawler.core.crawler.ArticleCrawler;
import wwmm.pubcrawler.core.model.*;
import wwmm.pubcrawler.core.model.ArticleReference;
import wwmm.pubcrawler.core.model.DOI;
import wwmm.pubcrawler.core.model.FullTextResourceDescription;
import wwmm.pubcrawler.core.model.SupplementaryResourceDescription;
import wwmm.pubcrawler.core.utils.XHtml;
import wwmm.pubcrawler.core.utils.XPathUtils;

/**
 * <p>
 * The <code>NatureArticleCrawler</code> class uses a provided DOI to get
 * information about an article that is published in a journal of Nature 
 * Publishing Group.
 * </p>
 * 
 * @author Nick Day
 * @version 0.1
 * 
 */
public class NatureArticleCrawler extends ArticleCrawler {

	private static final Logger LOG = Logger.getLogger(NatureArticleCrawler.class);

	public NatureArticleCrawler() {
		;
	}

	public NatureArticleCrawler(DOI doi) {
		super(doi);
	}
	
	protected void readProperties() {
		articleInfo.fullTextPdfXpath = ".//x:a[@class='download-pdf']";
		articleInfo.fullTextPdfLinkUrl = NATURE_HOMEPAGE_URL;
		articleInfo.fullTextHtmlXpath = ".//x:a[@class='fulltext']";
		articleInfo.fullTextHtmlLinkUrl = NATURE_HOMEPAGE_URL;
		
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
		LOG.info("Finished finding article details: "+doi.toString());
		return articleDetails;
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
		ar.setVolume(getVolume());
		ar.setNumber(getNumber());
		ar.setPages(getPages());
		return ar;
	}

	/**
	 * <p>
	 * Gets the name of the journal that the article has been
	 * published in from the abstract webpage.
	 * </p>
	 * 
	 * @return name of the journal that the article has been
	 * published in.
	 * 
	 */
	private String getJournal() {
		return getMetaElementContent("prism.publicationName");
	}

	/**
	 * <p>
	 * Gets the volume of the issue that the article is part of
	 * from the abstract webpage.
	 * </p>
	 * 
	 * @return the volume of the issue that the article is part of.
	 * 
	 */
	private String getVolume() {
		return getMetaElementContent("prism.volume");
	}

	/**
	 * <p>
	 * Gets the number of the issue that the article is part of
	 * from the abstract webpage.
	 * </p>
	 * 
	 * @return the number of the issue that the article is part of.
	 * 
	 */
	private String getNumber() {
		return getMetaElementContent("prism.number");
	}

	/**
	 * <p>
	 * Gets the article page numbers from the abstract webpage.
	 * </p>
	 * 
	 * @return the article page numbers.
	 * 
	 */
	private String getPages() {
		String start = getMetaElementContent("prism.startingPage");
		String end = getMetaElementContent("prism.endingPage");
		if (start != null && end != null) {
			return start+" - "+end;
		} else {
			return null;
		}
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
		Document suppPageDoc = getSupplementaryDataWebpage();
		if (suppPageDoc == null) {
			return Collections.EMPTY_LIST;
		}
		List<Node> suppLinks = XPathUtils.queryHTML(suppPageDoc, ".//x:a[contains(@href,'/extref/')]");
		List<SupplementaryResourceDescription> sfList = new ArrayList<SupplementaryResourceDescription>(suppLinks.size());
		for (Node suppLink : suppLinks) {
			Element link = (Element)suppLink;
			String urlPostfix = link.getAttributeValue("href");
			String url = NATURE_HOMEPAGE_URL+urlPostfix;
			String filename = FilenameUtils.getName(url);
			String linkText = link.getValue();
			linkText = linkText.replaceAll("\\s+", " ").trim();
			String contentType = httpClient.getContentType(url);
			SupplementaryResourceDescription sf = new SupplementaryResourceDescription(url, filename, linkText, contentType);
			sfList.add(sf);
		}
		return sfList;
	}

	/**
	 * <p>
	 * Crawls the abstract webpage to find a link to a webpage listing the
	 * article supplementary files.  This is then retrieved and returned.
	 * </p>
	 * 
	 * @return HTML of the webpage listing the article supplementary files.
	 * 
	 */
	private Document getSupplementaryDataWebpage() {
		Nodes suppPageLinks = articleAbstractHtml.query(".//x:a[contains(@href,'/suppinfo/')]", XHtml.XPATH_CONTEXT);
		if (suppPageLinks.size() == 0) {
			return null;
		} else if (suppPageLinks.size() > 1) {
			LOG.warn("Expected either 0 or 1 links to supporting info page, found "+suppPageLinks.size());
		}
		String urlPostfix = ((Element)suppPageLinks.get(0)).getAttributeValue("href");
		String suppPageUrl = NATURE_HOMEPAGE_URL+urlPostfix;
		return httpClient.getResourceHTML(suppPageUrl);
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
		return getMetaElementContent("dc.title");
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
		return getMetaElementContent("citation_authors");
	}

	/**
	 * <p>
	 * Much of the article metadata for Nature articles is
	 * provided in the meta elements in the HTML head, 
	 * so this method provides a generic method to get the
	 * value of a meta element with the provided name
	 * parameter.
	 * </p>
	 * 
	 * @param name - the value of the name attribute on the
	 * <meta> element you want to find. 
	 * 
	 * @return the value of the content attribute on the meta
	 * element.
	 */
	private String getMetaElementContent(String name) {
		Nodes authorNds = articleAbstractHtml.query(".//x:meta[@name='"+name+"']", XHtml.XPATH_CONTEXT);
		if (authorNds.size() == 0) {
			LOG.info("No meta element found with name='"+name+"' at: "+doi);
			return null;
		}
		Element metaEl = (Element)authorNds.get(0);
		return metaEl.getAttributeValue("content");
	}

	/**
	 * <p>
	 * Main method meant for demonstration purposes only, requires
	 * no arguments.
	 * </p>
	 * 
	 */
	public static void main(String[] args) {
		DOI doi = new DOI("http://dx.doi.org/10.1038/nchem.213");
		NatureArticleCrawler nac = new NatureArticleCrawler(doi);
		ArticleDescription details = nac.getDetails();
		System.out.println(details.toString());
	}

}
