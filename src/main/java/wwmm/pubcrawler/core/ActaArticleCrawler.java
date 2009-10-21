package wwmm.pubcrawler.core;

import static wwmm.pubcrawler.core.CrawlerConstants.X_XHTML;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Nodes;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
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

	private static final Logger LOG = Logger.getLogger(ActaArticleCrawler.class);

	/**
	 * <p>
	 * Creates an instance of the ActaArticleCrawler class and
	 * specifies the DOI of the article to be crawled.
	 * </p>
	 * 
	 * @param doi of the article to be crawled.
	 */
	public ActaArticleCrawler(DOI doi) {
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
		List<SupplementaryResourceDetails> suppFiles = getSupplementaryFilesDetails();
		setBibtexTool();
		if (bibtexTool != null) {
			String title = bibtexTool.getTitle();
			ArticleReference ref = bibtexTool.getReference();
			ad.setHasBeenPublished(true);
			String authors = bibtexTool.getAuthors();
			ad.setTitle(title);
			ad.setReference(ref);
			ad.setAuthors(authors);
			ad.setSupplementaryResources(suppFiles);
		}
		LOG.info("Finished finding article details: "+doi);
		return ad;
	}

	/**
	 * <p>
	 * Gets the article Bibtex file from the abstract webpage and sets
	 * the superclass <code>bibtexTool</code>.
	 * </p>
	 * 
	 */
	private void setBibtexTool() {
		String articleId = getArticleId();
		HttpPost postMethod = new HttpPost("http://scripts.iucr.org/cgi-bin/biblio");
		List<NameValuePair> pairs = new ArrayList<NameValuePair>(3);
		pairs.add(new BasicNameValuePair("name", "saveas"));
		pairs.add(new BasicNameValuePair("cnor", articleId));
		pairs.add(new BasicNameValuePair("Action", "download"));
		String encoding = "UTF-8";
		String paramStr = URLEncodedUtils.format(pairs, encoding);
		StringEntity entity;
		try {
			entity = new StringEntity(paramStr);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException("Entity encoding of "+encoding+" is illegal.", e);
		}
		postMethod.setEntity(entity);
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
		Nodes fullTextPdfLinks = articleAbstractHtml.query(".//x:a[./x:img[contains(@src,'graphics/pdfborder.gif')]]", X_XHTML);
		if (fullTextPdfLinks.size() != 1) {
			LOG.warn("Problem finding full text PDF link: "+doi);
			return null;
		}
		Element fullTextLink = (Element)fullTextPdfLinks.get(0);
		String linkText = "PDF";
		String fullTextUrl = fullTextLink.getAttributeValue("href");
		URI fullTextUri = createURI(fullTextUrl);
		return new FullTextResourceDetails(fullTextUri, linkText, "application/pdf");
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
		Nodes fullTextHtmlLinks = articleAbstractHtml.query(".//x:a[./x:img[contains(@src,'graphics/htmlborder.gif')]]", X_XHTML);
		if (fullTextHtmlLinks.size() != 1) {
			LOG.warn("Problem finding full text HTML link: "+doi);
			return null;
		}
		Element fullTextLink = (Element)fullTextHtmlLinks.get(0);
		String linkText = "HTML";
		String fullTextUrl = fullTextLink.getAttributeValue("href");
		URI fullTextUri = createURI(fullTextUrl);
		return new FullTextResourceDetails(fullTextUri, linkText, "text/html");
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
		Nodes cifNds = articleAbstractHtml.query(".//x:a[contains(@href,'http://scripts.iucr.org/cgi-bin/sendcif') and not(contains(@href,'mime'))]", X_XHTML);
		if (cifNds.size() == 0) {
			return new ArrayList<SupplementaryResourceDetails>(0);
		}
		String url = ((Element)cifNds.get(0)).getAttributeValue("href");
		URI uri = createURI(url);
		List<URI> cifUriList = getCifUrisFromUri(uri);
		List<SupplementaryResourceDetails> suppFiles = new ArrayList<SupplementaryResourceDetails>(cifUriList.size());
		for (URI cifUri : cifUriList) {
			String filename = getFilenameFromUrl(getURIString(cifUri));
			String contentType = httpClient.getContentType(cifUri);
			SupplementaryResourceDetails suppFile = new SupplementaryResourceDetails(cifUri, filename, "CIF", contentType);
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
	private List<URI> getCifUrisFromUri(URI uri) {
		List<URI> cifUriList = new ArrayList<URI>();
		if (uriPointsToCifListPage(uri)) {
			Document pageDoc = httpClient.getResourceHTML(uri);
			Nodes linkNds = pageDoc.query(".//x:a[contains(@href,'http://scripts.iucr.org/cgi-bin/sendcif')]", X_XHTML);
			if (linkNds.size() == 0) {
				LOG.warn("Could not find any CIF links at the supposed CIF list page: "+uri);
			}
			for (int i = 0; i < linkNds.size(); i++) {
				String url = ((Element)linkNds.get(i)).getAttributeValue("href");
				cifUriList.add(createURI(url));
			}
		} else {
			cifUriList.add(uri);
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
	private boolean uriPointsToCifListPage(URI uri) {
		String url = getURIString(uri);
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
		ActaArticleCrawler crawler = new ActaArticleCrawler(doi);
		ArticleDetails ad = crawler.getDetails();
		System.out.println(ad.toString());
	}

}
