package wwmm.pubcrawler.core;

import static wwmm.pubcrawler.core.CrawlerConstants.ACS_HOMEPAGE_URL;
import static wwmm.pubcrawler.core.CrawlerConstants.X_XHTML;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Node;
import nu.xom.Nodes;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import wwmm.pubcrawler.Utils;

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

	private static final Logger LOG = Logger.getLogger(AcsArticleCrawler.class);

	/**
	 * <p>
	 * Creates an instance of the AcsArticleCrawler class and
	 * specifies the DOI of the article to be crawled.
	 * </p>
	 * 
	 * @param doi of the article to be crawled.
	 */
	public AcsArticleCrawler(DOI doi) {
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
	public ArticleDetails getDetails() {
		if (!doiResolved) {
			LOG.warn("The DOI provided for the article abstract ("+doi.toString()+") has not resolved so we cannot get article details.");
			return ad;
		}
		List<FullTextResourceDetails> fullTextResources = getFullTextResources();
		ad.setFullTextResources(fullTextResources);
		String title = getTitle();
		String authors = getAuthors();
		ArticleReference ref = getReference();
		List<SupplementaryResourceDetails> suppFiles = getSupplementaryFilesDetails();
		ad.setTitle(title);
		ad.setReference(ref);
		ad.setAuthors(authors);
		ad.setSupplementaryResources(suppFiles);
		LOG.debug("Finished finding article details: "+doi.toString());
		return ad;
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
	private List<FullTextResourceDetails> getFullTextResources() {
		List<FullTextResourceDetails> fullTextResources = new ArrayList<FullTextResourceDetails>(3);
		FullTextResourceDetails fullTextHtmlDetails = getFullTextHtmlDetails();
		if (fullTextHtmlDetails != null) {
			fullTextResources.add(fullTextHtmlDetails);
		}
		FullTextResourceDetails fullTextPdfDetails = getFullTextPdfDetails();
		if (fullTextPdfDetails != null) {
			fullTextResources.add(fullTextPdfDetails);
		}
		FullTextResourceDetails fullTextHiResPdfDetails = getFullTextEnhancedPdfDetails();
		if (fullTextHiResPdfDetails != null) {
			fullTextResources.add(fullTextHiResPdfDetails);
		}
		return fullTextResources;
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
	private FullTextResourceDetails getFullTextPdfDetails() {
		Nodes fullTextPdfLinks = articleAbstractHtml.query(".//x:a[contains(@href,'/doi/pdf/')]", X_XHTML);
		if (fullTextPdfLinks.size() == 0) {
			LOG.warn("Problem getting full text PDF link: "+doi);
			return null;
		}
		Element fullTextLink = (Element)fullTextPdfLinks.get(0);
		String linkText = fullTextLink.getValue().trim();
		String fullTextPdfUrl = ACS_HOMEPAGE_URL+fullTextLink.getAttributeValue("href");
		URI fullTextPdfUri = createURI(fullTextPdfUrl);
		return new FullTextResourceDetails(fullTextPdfUri, linkText, "application/pdf");
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
	private FullTextResourceDetails getFullTextEnhancedPdfDetails() {
		Nodes fullTextPdfLinks = articleAbstractHtml.query(".//x:a[contains(@href,'/doi/pdfplus/')]", X_XHTML);
		if (fullTextPdfLinks.size() == 0) {
			LOG.warn("Problem getting full text enhanced PDF link: "+doi);
			return null;
		}
		Element fullTextLink = (Element)fullTextPdfLinks.get(0);
		String linkText = fullTextLink.getValue().trim();
		String fullTextPdfUrl = ACS_HOMEPAGE_URL+fullTextLink.getAttributeValue("href");
		URI fullTextPdfUri = createURI(fullTextPdfUrl);
		return new FullTextResourceDetails(fullTextPdfUri, linkText, "application/pdf");
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
	private FullTextResourceDetails getFullTextHtmlDetails() {
		Nodes fullTextHtmlLinks = articleAbstractHtml.query(".//x:a[contains(@href,'/doi/full/')]", X_XHTML);
		if (fullTextHtmlLinks.size() == 0) {
			LOG.warn("Problem getting full text HTML link: "+doi);
			return null;
		}
		Element fullTextLink = (Element)fullTextHtmlLinks.get(0);
		String linkText = fullTextLink.getValue().trim();
		String fullTextHtmlUrl = ACS_HOMEPAGE_URL+fullTextLink.getAttributeValue("href");
		URI fullTextHtmlUri = createURI(fullTextHtmlUrl);
		return new FullTextResourceDetails(fullTextHtmlUri, linkText, "text/html");
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
	private List<SupplementaryResourceDetails> getSupplementaryFilesDetails() {
		Document suppPageDoc = getSupplementaryDataWebpage();
		if (suppPageDoc == null) {
			return Collections.EMPTY_LIST;
		}
		List<Node> suppLinks = Utils.queryHTML(suppPageDoc, ".//x:div[@id='supInfoBox']//x:a[contains(@href,'/suppl/')]");
		List<SupplementaryResourceDetails> sfList = new ArrayList<SupplementaryResourceDetails>(suppLinks.size());
		for (Node suppLink : suppLinks) {
			Element link = (Element)suppLink;
			String urlPostfix = link.getAttributeValue("href");
			String url = ACS_HOMEPAGE_URL+urlPostfix;
			String filename = getFilenameFromUrl(url);
			URI uri = createURI(url);
			String linkText = link.getValue();
			String contentType = httpClient.getContentType(uri);
			SupplementaryResourceDetails sf = new SupplementaryResourceDetails(uri, filename, linkText, contentType);
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
		return namePlusMime.substring(0,dot);
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
		Nodes suppPageLinks = articleAbstractHtml.query(".//x:a[contains(@title,'Supporting Information')]", X_XHTML);
		if (suppPageLinks.size() == 0) {
			return null;
		} else if (suppPageLinks.size() > 1) {
			LOG.warn("Expected either 0 or 1 links to supporting info page, found "+suppPageLinks.size());
		}
		String urlPostfix = ((Element)suppPageLinks.get(0)).getAttributeValue("href");
		String url = ACS_HOMEPAGE_URL+urlPostfix;
		URI suppPageUri = createURI(url);
		return httpClient.getResourceHTML(suppPageUri);
	}

	/**
	 * <p>
	 * Gets a authors of the article from the abstract webpage.
	 * </p>
	 * 
	 * @return String containing the article authors.
	 * 
	 */
	private String getAuthors() {
		Nodes authorNds = articleAbstractHtml.query(".//x:meta[@name='dc.Creator']", X_XHTML);
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
	private ArticleReference getReference() {
		Nodes citationNds = articleAbstractHtml.query(".//x:div[@id='citation']", X_XHTML);
		if (citationNds.size() != 1) {
			LOG.warn("Problem finding bibliographic text at: "+doi);
			return null;
		}

		Element citationNd = (Element)citationNds.get(0);
		Nodes journalNds = citationNd.query("./x:cite", X_XHTML);
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
			ad.setHasBeenPublished(true);
			Nodes yearNds = citationNd.query("./x:span[@class='citation_year']", X_XHTML);
			if (yearNds.size() != 1) {
				LOG.warn("Problem finding year text at: "+doi);
			}
			year = ((Element)yearNds.get(0)).getValue().trim();		
			Nodes volumeNds = citationNd.query("./x:span[@class='citation_volume']", X_XHTML);
			if (volumeNds.size() != 1) {
				LOG.warn("Problem finding volume text at: "+doi);
			}
			volume = ((Element)volumeNds.get(0)).getValue().trim();
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
			ad.setHasBeenPublished(false);
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
	private String getTitle() {
		Nodes titleNds = articleAbstractHtml.query(".//x:h1[@class='articleTitle']", X_XHTML);
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
		DOI doi = new DOI("http://dx.doi.org/10.1021/je800923q");
		AcsArticleCrawler crawler = new AcsArticleCrawler(doi);
		ArticleDetails ad = crawler.getDetails();
		System.out.println(ad.toString());
		String title = ad.getTitle();
		title = new String(title.getBytes("ISO-8859-1"), "UTF8");
		FileUtils.writeStringToFile(new File("C:\\Users\\ned24\\workspace\\test.txt"), title);
	}

}
