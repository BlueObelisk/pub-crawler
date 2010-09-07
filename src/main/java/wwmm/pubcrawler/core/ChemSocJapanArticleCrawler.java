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

import static wwmm.pubcrawler.core.CrawlerConstants.CHEMSOCJAPAN_HOMEPAGE_URL;
import static wwmm.pubcrawler.core.CrawlerConstants.XHTML_NS;
import static wwmm.pubcrawler.core.CrawlerConstants.X_XHTML;

import java.util.ArrayList;
import java.util.List;

import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Nodes;
import nu.xom.Text;

import org.apache.log4j.Logger;

/**
 * <p>
 * The <code>ChemSocJapanArticleCrawler</code> class uses a provided 
 * DOI to get information about an article that is published in a 
 * journal of the Chemical Society of Japan.
 * </p>
 * 
 * @author Nick Day
 * @version 1.1
 * 
 */
public class ChemSocJapanArticleCrawler extends ArticleCrawler {

	private static final Logger LOG = Logger.getLogger(ChemSocJapanArticleCrawler.class);

	public ChemSocJapanArticleCrawler() {
		;
	}

	public ChemSocJapanArticleCrawler(DOI doi) {
		super(doi);
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
		List<FullTextResourceDescription> fullTextResources = getFullTextResources();
		articleDetails.setFullTextResources(fullTextResources);
		List<SupplementaryResourceDescription> suppFiles = getSupplementaryFilesDetails();
		setBibtexTool();
		if (bibtexTool != null) {
			String title = bibtexTool.getTitle();
			ArticleReference ref = bibtexTool.getReference();
			articleDetails.setHasBeenPublished(true);
			String authors = bibtexTool.getAuthors();
			articleDetails.setTitle(title);
			articleDetails.setReference(ref);
			articleDetails.setAuthors(authors);
			articleDetails.setSupplementaryResources(suppFiles);
		}
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
	private void setBibtexTool() {
		Nodes bibtexLinks = articleAbstractHtml.query(".//x:a[contains(@href,'/_bib/')]", X_XHTML);
		if (bibtexLinks.size() != 1) {
			return;
		}
		String urlPostfix = ((Element)bibtexLinks.get(0)).getAttributeValue("href");
		String bibUrl = CHEMSOCJAPAN_HOMEPAGE_URL+urlPostfix;
		String bibStr = httpClient.getResourceString(bibUrl);
		bibtexTool = new BibtexTool(bibStr);
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
	private List<FullTextResourceDescription> getFullTextResources() {
		List<FullTextResourceDescription> fullTexts = new ArrayList<FullTextResourceDescription>(1);
		Nodes pdfLinks = articleAbstractHtml.query(".//x:a[contains(@href,'_pdf') and contains(.,'PDF')]", X_XHTML);
		if (pdfLinks.size() == 0) {
			LOG.warn("Could not find PDF link for: "+doi);
			return null;
		}
		Element link = (Element)pdfLinks.get(0);
		String linkText = link.getValue().trim();
		String urlPostfix = link.getAttributeValue("href");
		String pdfUrl = CHEMSOCJAPAN_HOMEPAGE_URL+urlPostfix;
		FullTextResourceDescription ftrd = new FullTextResourceDescription(pdfUrl, linkText, "application/pdf");
		fullTexts.add(ftrd);
		return fullTexts;
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
	private List<SupplementaryResourceDescription> getSupplementaryFilesDetails() {
		Nodes suppListLinks = articleAbstractHtml.query(".//x:a[contains(@href,'_applist')]", X_XHTML);
		if (suppListLinks.size() == 0) {
			return new ArrayList<SupplementaryResourceDescription>(0);
		}
		String urlPostfix = ((Element)suppListLinks.get(0)).getAttributeValue("href");
		String suppListUrl = CHEMSOCJAPAN_HOMEPAGE_URL+urlPostfix;
		Document suppListDoc = httpClient.getResourceHTML(suppListUrl);
		Nodes suppTableNodes = suppListDoc.query(".//x:table[@cellpadding='2' and @cellspacing='3']", X_XHTML);
		Element suppTable = (Element)suppTableNodes.get(1);
		Nodes tableRows = suppTable.query(".//x:tr", X_XHTML);
		if (tableRows.size() < 3) {
			LOG.warn("Expected the supplementary document table to have at least 3 rows, found "+tableRows.size());
			return null;
		}
		List<SupplementaryResourceDescription> suppFiles = new ArrayList<SupplementaryResourceDescription>(1);
		for (int i = 2; i < tableRows.size(); i++) {
			Element row = (Element)tableRows.get(i);
			Nodes cells = row.query(".//x:td", X_XHTML);
			if (cells.size() != 4) {
				continue;
			}
			Element cell0 = (Element)cells.get(0);
			String linkText = ((Text)cell0.getChild(0)).getValue();
			Element cell3 = (Element)cells.get(3);
			Element suppLink = cell3.getFirstChildElement("a", XHTML_NS);
			String suppUrlPostfix = suppLink.getAttributeValue("href");
			String suppUrl = CHEMSOCJAPAN_HOMEPAGE_URL+suppUrlPostfix;
			String filename = getFilenameFromUrl(suppUrl);
			String contentType = httpClient.getContentType(suppUrl);
			SupplementaryResourceDescription suppFile = new SupplementaryResourceDescription(suppUrl, filename, linkText, contentType);
			suppFiles.add(suppFile);
		}
		return suppFiles;
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
		return cifUrl.substring(idx+1);
	}

	/**
	 * <p>
	 * Main method meant for demonstration purposes only. Requires
	 * no arguments.
	 * </p>
	 * 
	 */
	public static void main(String[] args) {
		DOI doi = new DOI("http://dx.doi.org/10.1246/cl.2008.682");
		ChemSocJapanArticleCrawler crawler = new ChemSocJapanArticleCrawler(doi);
		ArticleDescription ad = crawler.getDetails();
		System.out.println(ad.toString());
	}
}
