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
package wwmm.pubcrawler.impl;

import static wwmm.pubcrawler.core.CrawlerConstants.NATURE_HOMEPAGE_URL;
import static wwmm.pubcrawler.core.CrawlerConstants.X_XHTML;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Nodes;

import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;
import org.apache.log4j.Logger;

import wwmm.pubcrawler.core.ArticleDescription;
import wwmm.pubcrawler.core.CrawlerHttpClient;
import wwmm.pubcrawler.core.IssueCrawler;
import wwmm.pubcrawler.core.IssueDescription;
import wwmm.pubcrawler.journal.nature.NatureIssueCrawler;
import wwmm.pubcrawler.journal.nature.NatureJournal;

/**
 * <p>
 * Provides a method of crawling an issue of a journal published
 * by Nature and extracting any compound data provided.
 * </p>
 * 
 * @author Nick Day
 * @version 0.1
 *
 */
public class NatureCompoundsCrawler {

	private final CrawlerHttpClient httpClient = new CrawlerHttpClient();
	private IssueCrawler crawler;

	private static final Logger LOG = Logger.getLogger(NatureCompoundsCrawler.class);

	// hide the default constructor
	private NatureCompoundsCrawler() {
		;
	}

	public NatureCompoundsCrawler(NatureJournal journal) {
		crawler = new NatureIssueCrawler(journal);
	}

	/**
	 * <p>
	 * Crawls the current issue of the provided <code>NatureJournal</code>
	 * and provides details of each article for that issue, complete with
	 * details on the compounds in each article. 
	 * </p>
	 * 
	 * @return - a list of <code>ArticleData</code> objects, which provide
	 * details about the article, as well as any data for any compounds found.
	 */
	public List<ArticleData> crawlCurrentIssue() {
		List<ArticleDescription> articleDetailsList = crawler.getCurrentArticleDescriptions();
		return getArticleDatasFromArticleDetails(articleDetailsList);
	}

	/**
	 * <p>
	 * Crawls the issue defined in <code>issueDetails</code> of the provided 
	 * <code>NatureJournal</code> and provides details of each article for 
	 * that issue, complete with details on the compounds in each article. 
	 * </p>
	 * 
	 * @return - a list of <code>ArticleData</code> objects, which provide
	 * details about each article, as well as any data for any compounds found.
	 */
	public List<ArticleData> crawlIssue(IssueDescription issueDetails) {
		List<ArticleDescription> articleDetailsList = crawler.getArticleDescriptions(issueDetails);
		return getArticleDatasFromArticleDetails(articleDetailsList);
	}

	/**
	 * <p>
	 * Uses the provided list of <code>ArticleDetails</code> to construct
	 * a list of <code>ArticleData</code>.
	 * </p>
	 * 
	 * @param adList - list of <code>ArticleDetails</code>s you want to 
	 * construct the list of <code>ArticleData</code> from.
	 * 
	 * @return list of <code>ArticleData</code> where each item is generated
	 * from an item in the provided <code>ArticleDetails</code> list.
	 */
	private List<ArticleData> getArticleDatasFromArticleDetails(List<ArticleDescription> adList) {
		List<ArticleData> articleDataList = new ArrayList<ArticleData>(adList.size());
		for (ArticleDescription ad : adList) {
			String doiPostfix = ad.getDoi().getPostfix();
			String natureId = doiPostfix.substring(doiPostfix.indexOf("/")+1);
			String ciUrl = "http://www.nature.com/nchem/journal/v1/n5/compound/"+natureId+"_ci.html";
			List<CompoundDetails> cdList = getCompoundDetailsList(ciUrl);
			articleDataList.add(new ArticleData(ad, cdList));
			LOG.info("Finished crawling for: "+ad.getDoi().toString());
		}
		return articleDataList;
	}

	/**
	 * <p>
	 * For a given article full-text HTML URI, this method will go
	 * through the full-text and find details for all compounds
	 * described within.
	 * </p>
	 * 
	 * @param fullTextHtmlUri - the URI of the full-text HTML.
	 * 
	 * @return details for all compounds described within the
	 * full-text HTML.
	 */
	private List<CompoundDetails> getCompoundDetailsList(String abstractUrl) {
		Document abstractDoc = null;
		try {
			abstractDoc = httpClient.getResourceHTML(abstractUrl);
		} catch (RuntimeException e) {
			return new ArrayList<CompoundDetails>();
		}
		Nodes compoundLinkNds = abstractDoc.query(".//x:h3/x:a[contains(@href,'/compound/') and contains(@href,'.html')]", X_XHTML);
		Set<String> compoundUrls = new HashSet<String>();
		for (int i = 0; i < compoundLinkNds.size(); i++) {
			Element compoundLink = (Element)compoundLinkNds.get(i);
			String compoundUrl = NATURE_HOMEPAGE_URL+compoundLink.getAttributeValue("href");
			compoundUrls.add(compoundUrl);
		}
		List<CompoundDetails> cdList = new ArrayList<CompoundDetails>(compoundUrls.size());
		for (String compoundUrl : compoundUrls) {
			LOG.info("Finding compound info at: "+compoundUrl);
			String cmpdId = getCompoundId(compoundUrl);
			if (cmpdId == null) {
				continue;
			}
			Document splashPageDoc = httpClient.getResourceHTML(compoundUrl);
			URI cmlUri = getCmlFileUri(splashPageDoc);
			URI molUri = getMolFileUri(splashPageDoc);
			URI chemDrawUri = getChemDrawFileUri(splashPageDoc);
			cdList.add(new CompoundDetails(cmpdId, compoundUrl, cmlUri, molUri, chemDrawUri));
		}
		return cdList;
	}

	/**
	 * <p>
	 * Gets a compounds ID from its splash page URL.
	 * </p>
	 * 
	 * @param compoundUrl - URL that the ID will be extracted 
	 * from.
	 * 
	 * @return String representing the compound ID.
	 */
	private String getCompoundId(String compoundUrl) {
		int startIdx = compoundUrl.indexOf("_comp")+5;
		int endIdx = compoundUrl.lastIndexOf(".");
		if (startIdx == -1 || endIdx == -1) {
			LOG.warn("Could not find compound ID from URL: "+compoundUrl);
			return null;
		}
		return compoundUrl.substring(startIdx, endIdx);
	}

	/**
	 * <p>
	 * Simple convenience method for the creation of URIs from
	 * a URL string to handle any exceptions.
	 * </p>
	 * 
	 * @param url - String of the URL you want to use to create
	 * the URI.
	 * 
	 * @return URI that represents the provided URL string.
	 */
	private URI createUri(String url) {
		try {
			return new URI(url, false);
		} catch (URIException e) {
			LOG.warn("Could not create URI from URL: "+url+" - "+e.getMessage());
			return null;
		}
	}

	/**
	 * <p>
	 * Extracts the link to a structure's CML file from its 
	 * splash page.
	 * </p>
	 * 
	 * @param splashPageDoc - XML Document of the splash page HTML.
	 * 
	 * @return the URL that links to the CML from the splash page. 
	 * Returns null if a CML link cannot be found.
	 */
	private URI getCmlFileUri(Document splashPageDoc) {
		Nodes cmlFileLinkNds = splashPageDoc.query(".//x:a[contains(@href,'/cml/')]", X_XHTML);
		if (cmlFileLinkNds.size() != 1) {
			return null;
		}
		Element cmlLink = (Element)cmlFileLinkNds.get(0);
		return createUri(NATURE_HOMEPAGE_URL+cmlLink.getAttributeValue("href"));
	}

	/**
	 * <p>
	 * Extracts the link to a structure's MOL file from its 
	 * splash page.
	 * </p>
	 * 
	 * @param splashPageDoc - XML Document of the splash page HTML.
	 * 
	 * @return the URL that links to the MOL from the splash page. 
	 * Returns null if a MOL link cannot be found.
	 */
	private URI getMolFileUri(Document splashPageDoc) {
		Nodes molFileLinkNds = splashPageDoc.query(".//x:a[contains(@href,'/mol/')]", X_XHTML);
		if (molFileLinkNds.size() != 1) {
			return null;
		}
		Element molLink = (Element)molFileLinkNds.get(0);
		return createUri(NATURE_HOMEPAGE_URL+molLink.getAttributeValue("href"));
	}

	/**
	 * <p>
	 * Extracts the link to a structure's ChemDraw file from its 
	 * splash page.
	 * </p>
	 * 
	 * @param splashPageDoc - XML Document of the splash page HTML.
	 * 
	 * @return the URL that links to the ChemDraw from the splash 
	 * page. Returns null if a ChemDraw link cannot be found.
	 */
	private URI getChemDrawFileUri(Document splashPageDoc) {
		Nodes chemdrawFileLinkNds = splashPageDoc.query(".//x:a[contains(@href,'/chemdraw/')]", X_XHTML);
		if (chemdrawFileLinkNds.size() != 1) {
			return null;
		}
		Element chemdrawLink = (Element)chemdrawFileLinkNds.get(0);
		return createUri(NATURE_HOMEPAGE_URL+chemdrawLink.getAttributeValue("href"));
	}

	/**
	 * <p>
	 * Class used to hold the details of an article found in the 
	 * full-text of an article in a Nature journal.
	 * </p>
	 * 
	 * @author Nick Day
	 * @version 0.1
	 *
	 */
	public class ArticleData {

		private ArticleDescription ad;
		private List<CompoundDetails> cdList;

		// hide the default constructor
		private ArticleData() {
			;
		}

		public ArticleData(ArticleDescription ad, List<CompoundDetails> cdList) {
			this();
			this.ad = ad;
			this.cdList = cdList;
		}

		/**
		 * <p>
		 * Gets the <code>ArticleDetails</code> description
		 * for this article.
		 * </p>
		 * 
		 * @return the <code>ArticleDetails</code> description
		 * for this article.
		 */
		public ArticleDescription getArticleDetails() {
			return ad;
		}

		/**
		 * <p>
		 * Gets the list of details about the compounds described
		 * in this article.
		 * </p>
		 * 
		 * @return the list of details about the compounds described
		 * in this article.
		 */
		public List<CompoundDetails> getCompoundDetailsList() {
			return cdList;
		}

	}

	/**
	 * <p>
	 * Class used to hold the details of a compound found in the 
	 * full-text of an article in a Nature journal.
	 * </p>
	 * 
	 * @author Nick Day
	 * @version 0.1
	 *
	 */
	public class CompoundDetails {

		private String id;
		private String splashPageUrl;
		private URI cmlUri;
		private URI molUri;
		private URI chemDrawUri;

		// hide the default constructor
		private CompoundDetails() {
			;
		}

		public CompoundDetails(String id, String splashPageUrl, URI cmlUri, URI molUri, URI chemDrawUri) {
			this.id = id;
			this.splashPageUrl = splashPageUrl;
			this.cmlUri = cmlUri;
			this.molUri = molUri;
			this.chemDrawUri = chemDrawUri;
		}

		/**
		 * <p>
		 * Gets the ID of this compound.
		 * </p>
		 * 
		 * @return the ID for this compound.
		 */
		public String getID() {
			return id;
		}

		/**
		 * <p>
		 * Gets the URI of the splash page for this compound.
		 * </p>
		 * 
		 * @return the URI of the splash page for this compound.
		 */
		public String getSplashPageUrl() {
			return splashPageUrl;
		}

		/**
		 * <p>
		 * Gets the URI of the CML file for this compound.
		 * </p>
		 * 
		 * @return the URI of the CML file for this compound.
		 */
		public URI getCmlUri() {
			return cmlUri;
		}

		/**
		 * <p>
		 * Gets the URI of the MOL file for this compound.
		 * </p>
		 * 
		 * @return the URI of the MOL file for this compound.
		 */
		public URI getMolUri() {
			return molUri;
		}

		/**
		 * <p>
		 * Gets the URI of the ChemDraw file for this compound.
		 * </p>
		 * 
		 * @return the URI of the ChemDraw file for this compound.
		 */
		public URI getChemDrawUri() {
			return chemDrawUri;
		}

	}

}
