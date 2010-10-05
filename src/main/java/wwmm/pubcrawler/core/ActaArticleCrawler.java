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

import static wwmm.pubcrawler.core.CrawlerConstants.ACS_HOMEPAGE_URL;
import static wwmm.pubcrawler.core.CrawlerConstants.X_XHTML;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Nodes;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;

/**
 * <p>
 * The <code>ActaArticleCrawler</code> class uses a provided DOI to get
 * information about an article that is published in a journal of Acta
 * Crytallographica.
 * </p>
 * 
 * @author Nick Day
 * @version 1.1
 * 
 */
public class ActaArticleCrawler extends ArticleCrawler {

	private static final String TEXT_HTML = "text/html";
	private static final Logger LOG = Logger.getLogger(ActaArticleCrawler.class);

	public ActaArticleCrawler() {
		;
	}

	public ActaArticleCrawler(DOI doi) {
		super(doi);
	}
	
	protected void readProperties() {
		articleInfo.fullTextHtmlXpath = ".//x:a[./x:img[contains(@src,'graphics/htmlborder.gif')]]";
		articleInfo.fullTextPdfXpath = ".//x:a[./x:img[contains(@src,'graphics/pdfborder.gif')]]";
		articleInfo.fullTextHtmlLinkUrl = "HTML";
		articleInfo.fullTextPdfLinkUrl = "PDF";
		
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
		if (doi == null) {
			throw new IllegalStateException("DOI is null, it must be set before the article details can be obtained.");
		}
		if (!doiResolved) {
			LOG.warn("The DOI provided for the article abstract ("+doi.toString()+") has not resolved so we cannot get article details.");
			return articleDetails;
		}
		List<FullTextResourceDescription> fullTextResources = getFullTextResources();
		articleDetails.setFullTextResources(fullTextResources);
		applyBibtexTool();
		LOG.info("Finished finding article details: "+doi);
		return articleDetails;
	}

	/**
	 * <p>
	 * Gets the article Bibtex file from the abstract webpage and sets
	 * the superclass <code>bibtexTool</code>.
	 * </p>
	 * 
	 */
	@Override
	protected void setBibtexTool() {
		String articleId = getArticleId();
		PostMethod postMethod = new PostMethod("http://scripts.iucr.org/cgi-bin/biblio");
		NameValuePair[] nvps = {
				new NameValuePair("name", "saveas"),
				new NameValuePair("cnor", articleId),
				new NameValuePair("Action", "download")
		};
		postMethod.setRequestBody(nvps);
		String bibstr = httpClient.getPostResultString(postMethod);
		bibtexTool = new BibtexTool(bibstr);
	}

	/**
	 * <p>
	 * Gets the article's unique ID (as provided by the publisher) from
	 * the abstract webpage.
	 * </p>
	 * 
	 * @return String containing the article's unique ID.
	 * 
	 */
	private String getArticleId() {
		Nodes nds = articleAbstractHtml.query(".//x:input[@name='cnor']", X_XHTML);
		if (nds.size() == 0) {
			LOG.warn("Could not find the article ID for "+doi.toString()+
			" webpage structure may have changed.  Crawler may need rewriting!");
			return null;
		}
		return ((Element)nds.get(0)).getAttributeValue("value");
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
		Nodes cifNds = articleAbstractHtml.query(".//x:a[contains(@href,'http://scripts.iucr.org/cgi-bin/sendcif') and not(contains(@href,'mime'))]", X_XHTML);
		if (cifNds.size() == 0) {
			return new ArrayList<SupplementaryResourceDescription>(0);
		}
		String url = ((Element)cifNds.get(0)).getAttributeValue("href");
		List<String> cifUrlList = getCifUrisFromUri(url);
		List<SupplementaryResourceDescription> suppFiles = new ArrayList<SupplementaryResourceDescription>(cifUrlList.size());
		for (String cifUrl : cifUrlList) {
			String filename = getFilenameFromUrl(cifUrl);
			String contentType = httpClient.getContentType(cifUrl);
			SupplementaryResourceDescription suppFile = new SupplementaryResourceDescription(cifUrl, filename, "CIF", contentType);
			suppFiles.add(suppFile);
		}
		return suppFiles;
	}

	/**
	 * <p>
	 * Retrieve the CIF URIs for this article from the URI provided.
	 * </p>
	 * 
	 * @param uri from which to obtain the CIF URIs for this article.
	 * 
	 * @return a list of the URIs for the CIFs for this article.
	 */
	private List<String> getCifUrisFromUri(String url) {
		List<String> cifUriList = new ArrayList<String>();
		if (uriPointsToCifListPage(url)) {
			Document pageDoc = httpClient.getResourceHTML(url);
			Nodes linkNds = pageDoc.query(".//x:a[contains(@href,'http://scripts.iucr.org/cgi-bin/sendcif')]", X_XHTML);
			if (linkNds.size() == 0) {
				LOG.warn("Could not find any CIF links at the supposed CIF list page: "+url);
			} else {
				for (int i = 0; i < linkNds.size(); i++) {
					String newUrl = ((Element)linkNds.get(i)).getAttributeValue("href");
					cifUriList.add(newUrl);
				}
			}
		} else {
			cifUriList.add(url);
		}
		return cifUriList;
	}

	/**
	 * <p>
	 * If the URI does not contain sup(\\d+) at the end, then it means that
	 * if doesn't point to a CIF, but instead points to another webpage that
	 * contains a list of CIFs.
	 * </p>
	 * 
	 * @param uri
	 * 
	 * @return
	 */
	private boolean uriPointsToCifListPage(String url) {
		int idx = url.lastIndexOf("/");
		String s = url.substring(idx);
		if (s.contains("sup")) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * <p>
	 * Gets the name of the supplementary file at the publisher's site from
	 * the supplementary file URL.
	 * </p>
	 * 
	 * @param fileUrl - the URL from which to obtain the filename.
	 * 
	 * @return the filename of the supplementary file.
	 */
	private String getFilenameFromUrl(String fileUrl) {
		// first, see if the file is a CIF, which has special URLs of the form
		// below
		Pattern pattern = Pattern.compile("http://scripts.iucr.org/cgi-bin/sendcif\\?(.{6}sup\\d+)");
		Matcher matcher = null;
		matcher = pattern.matcher(fileUrl);
		if (matcher.find()) {
			return matcher.group(1);
		} else {
			// if the previous regexp does not match, then it isn't a CIF, so
			// it should be fine just to take substring after the final '/'
			int idx = fileUrl.lastIndexOf("/");
			return fileUrl.substring(idx+1);
		}
	}

	/**
	 * <p>
	 * Main method meant for demonstration purposes only. Requires
	 * no arguments.
	 * </p>
	 * 
	 */
	public static void main(String[] args) {
		DOI doi = new DOI("http://dx.doi.org//10.1107/S0108270109006118");
		ArticleCrawler crawler = new ActaArticleCrawler(doi);
		ArticleDescription ad = crawler.getDetails();
		System.out.println(ad.toString());
	}
}
