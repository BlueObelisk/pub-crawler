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
package wwmm.pubcrawler.journal.acs;

import static wwmm.pubcrawler.core.utils.CrawlerConstants.ACS_HOMEPAGE_URL;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Node;
import nu.xom.Nodes;

import org.apache.log4j.Logger;

import wwmm.pubcrawler.core.types.Doi;
import wwmm.pubcrawler.core.crawler.ArticleCrawler;
import wwmm.pubcrawler.core.model.ArticleDescription;
import wwmm.pubcrawler.core.model.*;
import wwmm.pubcrawler.core.model.SupplementaryResourceDescription;
import wwmm.pubcrawler.core.utils.XHtml;
import wwmm.pubcrawler.core.utils.XPathUtils;

/**
 * <p>
 * The <code>AcsArticleCrawler</code> class uses a provided DOI to get
 * information about an article that is published in a journal of the 
 * American Chemical Society.
 * </p>
 * 
 * @author Nick Day
 * @version 1.1
 * 
 */
public class AcsArticleCrawler extends ArticleCrawler {

	static final Logger LOG = Logger.getLogger(AcsArticleCrawler.class);

	public AcsArticleCrawler() {
		;
	}

	public AcsArticleCrawler(Doi doi) {
		super(doi);
	}
	
	protected void readProperties() {
		articleInfo.fullTextPdfLinkUrl = ACS_HOMEPAGE_URL;
		articleInfo.fullTextPdfXpath = ".//x:a[contains(@href,'/doi/pdf/')]";
		articleInfo.fullTextEnhancedPdfLinkUrl = ACS_HOMEPAGE_URL;
		articleInfo.fullTextEnhancedPdfXpath = ".//x:a[contains(@href,'/doi/pdfplus/')]";
		articleInfo.fullTextHtmlLinkUrl = ACS_HOMEPAGE_URL;
		articleInfo.fullTextHtmlXpath = ".//x:a[contains(@href,'/doi/full/')]";
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
		List<Node> suppLinks = XPathUtils.queryHTML(suppPageDoc, ".//x:div[@id='supInfoBox']//x:a[contains(@href,'/suppl/')]");
		List<SupplementaryResourceDescription> sfList = new ArrayList<SupplementaryResourceDescription>(suppLinks.size());
		for (Node suppLink : suppLinks) {
			Element link = (Element)suppLink;
			String urlPostfix = link.getAttributeValue("href");
			String url = ACS_HOMEPAGE_URL+urlPostfix;
			String filename = getFilenameFromUrl(url);
			String linkText = link.getValue();
			String contentType = httpClient.getContentType(url);
			SupplementaryResourceDescription sf = new SupplementaryResourceDescription(url, filename, linkText, contentType);
			sfList.add(sf);
		}
		return sfList;
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
	 * Crawls the abstract webpage to find a link to a webpage listing the
	 * article supplementary files.  This is then retrieved and returned.
	 * </p>
	 * 
	 * @return HTML of the webpage listing the article supplementary files.
	 * 
	 */
	private Document getSupplementaryDataWebpage() {
		Nodes suppPageLinks = articleAbstractHtml.query(".//x:a[contains(@title,'Supporting Information')]", XHtml.XPATH_CONTEXT);
		if (suppPageLinks.size() == 0) {
			return null;
		} else if (suppPageLinks.size() > 1) {
			LOG.warn("Expected either 0 or 1 links to supporting info page, found "+suppPageLinks.size());
		}
		String urlPostfix = ((Element)suppPageLinks.get(0)).getAttributeValue("href");
		String suppPageUrl = ACS_HOMEPAGE_URL+urlPostfix;
		return httpClient.getResourceHTML(suppPageUrl);
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
		Nodes authorNds = articleAbstractHtml.query(".//x:meta[@name='dc.Creator']", XHtml.XPATH_CONTEXT);
		if (authorNds.size() == 0) {
			LOG.warn("Problem finding authors at: "+doi);
			return null;
		}
		StringBuilder authors = new StringBuilder();
		for (int i = 0; i < authorNds.size(); i++) {
			String author = ((Element)authorNds.get(i)).getAttributeValue("content");
			authors.append(author);
			if (i < authorNds.size() - 1) {
				authors.append(", ");
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
		Nodes citationNds = articleAbstractHtml.query(".//x:div[@id='citation']", XHtml.XPATH_CONTEXT);
		if (citationNds.size() != 1) {
			LOG.warn("Problem finding bibliographic text at: "+doi);
			return null;
		}

		Element citationNd = (Element)citationNds.get(0);
		Nodes journalNds = citationNd.query("./x:cite", XHtml.XPATH_CONTEXT);
		String journal = null;
		if (journalNds.size() != 1) {
			LOG.warn("Problem finding journal text at: "+doi);
		} else {
			journal = ((Element)journalNds.get(0)).getValue().trim();
		}

		String citationContent = citationNd.getValue();
		String year = null;
		String volume = null;
		String number = null;
		String pages = null;
		if (!citationContent.contains("Article ASAP")) {
			articleDetails.setHasBeenPublished(true);
			Nodes yearNds = citationNd.query("./x:span[@class='citation_year']", XHtml.XPATH_CONTEXT);
			if (yearNds.size() != 1) {
				LOG.warn("Problem finding year text at: "+doi);
			} else {
				year = ((Element)yearNds.get(0)).getValue().trim();
			}
			Nodes volumeNds = citationNd.query("./x:span[@class='citation_volume']", XHtml.XPATH_CONTEXT);
			if (volumeNds.size() != 1) {
				LOG.warn("Problem finding volume text at: "+doi);
			} else {
				volume = ((Element)volumeNds.get(0)).getValue().trim();
			}
			String refContent = citationNd.getValue();
			Pattern p = Pattern.compile("[^\\(]*\\((\\d+)\\),\\s+pp\\s+(\\d+).(\\d+).*");
			Matcher matcher = p.matcher(refContent);
			if (!matcher.find() || matcher.groupCount() != 3) {
				LOG.warn("Problem finding issue and pages info at: "+doi);
			} else {
				number = matcher.group(1);
				pages = matcher.group(2)+"-"+matcher.group(3);
			}
		} else {
			articleDetails.setHasBeenPublished(false);
		}

		ArticleReference ar = new ArticleReference();
		ar.setJournalTitle(journal);
		ar.setVolume(volume);
		ar.setYear(year);
		ar.setNumber(number);
		ar.setPages(pages);
		return ar;
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
		Nodes titleNds = articleAbstractHtml.query(".//x:h1[@class='articleTitle']", XHtml.XPATH_CONTEXT);
		if (titleNds.size() != 1) {
			LOG.warn("Problem finding title at: "+doi);
			return null;
		}
		String title = titleNds.get(0).getValue();
		return title;
	}

	/**
	 * <p>
	 * Main method meant for demonstration purposes only. Requires
	 * no arguments.
	 * </p>
	 * @throws IOException 
	 * 
	 */
	public static void main(String[] args) throws IOException {
		Doi doi = new Doi("http://dx.doi.org/10.1021/cg100078b");
		ArticleCrawler crawler = new AcsArticleCrawler(doi);
		ArticleDescription ad = crawler.getDetails();
		System.out.println(ad.toString());
	}

}
