package wwmm.pubcrawler.core;

import static wwmm.pubcrawler.core.CrawlerConstants.NATURE_HOMEPAGE_URL;
import static wwmm.pubcrawler.core.CrawlerConstants.X_XHTML;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Node;
import nu.xom.Nodes;

import org.apache.commons.httpclient.URI;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import wwmm.pubcrawler.Utils;

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
			return articleDetails;
		}
		List<FullTextResourceDetails> fullTextResources = getFullTextResources();
		articleDetails.setFullTextResources(fullTextResources);
		String title = getTitle();
		articleDetails.setTitle(title);
		String authors = getAuthors();
		articleDetails.setAuthors(authors);
		ArticleReference ref = getReference();
		articleDetails.setReference(ref);
		List<SupplementaryResourceDetails> suppFiles = getSupplementaryFilesDetails();
		articleDetails.setSupplementaryResources(suppFiles);
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
	private ArticleReference getReference() {
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
	private List<SupplementaryResourceDetails> getSupplementaryFilesDetails() {
		Document suppPageDoc = getSupplementaryDataWebpage();
		if (suppPageDoc == null) {
			return Collections.EMPTY_LIST;
		}
		List<Node> suppLinks = Utils.queryHTML(suppPageDoc, ".//x:a[contains(@href,'/extref/')]");
		List<SupplementaryResourceDetails> sfList = new ArrayList<SupplementaryResourceDetails>(suppLinks.size());
		for (Node suppLink : suppLinks) {
			Element link = (Element)suppLink;
			String urlPostfix = link.getAttributeValue("href");
			String url = NATURE_HOMEPAGE_URL+urlPostfix;
			String filename = FilenameUtils.getName(url);
			URI uri = createURI(url);
			String linkText = link.getValue();
			linkText = linkText.replaceAll("\\s+", " ").trim();
			String contentType = httpClient.getContentType(uri);
			SupplementaryResourceDetails sf = new SupplementaryResourceDetails(uri, filename, linkText, contentType);
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
		Nodes suppPageLinks = articleAbstractHtml.query(".//x:a[contains(@href,'/suppinfo/')]", X_XHTML);
		if (suppPageLinks.size() == 0) {
			return null;
		} else if (suppPageLinks.size() > 1) {
			LOG.warn("Expected either 0 or 1 links to supporting info page, found "+suppPageLinks.size());
		}
		String urlPostfix = ((Element)suppPageLinks.get(0)).getAttributeValue("href");
		String url = NATURE_HOMEPAGE_URL+urlPostfix;
		URI suppPageUri = createURI(url);
		return httpClient.getResourceHTML(suppPageUri);
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
	private String getAuthors() {
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
		Nodes authorNds = articleAbstractHtml.query(".//x:meta[@name='"+name+"']", X_XHTML);
		if (authorNds.size() == 0) {
			LOG.info("No meta element found with name='"+name+"' at: "+doi);
			return null;
		}
		Element metaEl = (Element)authorNds.get(0);
		return metaEl.getAttributeValue("content");
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
	 * Gets the details about the full-text HTML resource for 
	 * this article.
	 * </p>
	 * 
	 * @return details about the full-text HTML resource for this
	 * article.
	 */
	private FullTextResourceDetails getFullTextHtmlDetails() {
		Nodes fullTextHtmlLinks = articleAbstractHtml.query(".//x:a[@class='fulltext']", X_XHTML);
		if (fullTextHtmlLinks.size() == 0) {
			LOG.warn("Problem getting full text HTML link: "+doi);
			return null;
		}
		Element fullTextLink = (Element)fullTextHtmlLinks.get(0);
		String linkText = fullTextLink.getValue().trim();
		String fullTextHtmlUrl = NATURE_HOMEPAGE_URL+fullTextLink.getAttributeValue("href");
		URI fullTextHtmlUri = createURI(fullTextHtmlUrl);
		return new FullTextResourceDetails(fullTextHtmlUri, linkText, "text/html");
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
		Nodes fullTextPdfLinks = articleAbstractHtml.query(".//x:a[@class='download-pdf']", X_XHTML);
		if (fullTextPdfLinks.size() == 0) {
			LOG.warn("Problem getting full text PDF link: "+doi);
			return null;
		}
		Element fullTextLink = (Element)fullTextPdfLinks.get(0);
		String linkText = fullTextLink.getValue().trim();
		String fullTextPdfUrl = NATURE_HOMEPAGE_URL+fullTextLink.getAttributeValue("href");
		URI fullTextPdfUri = createURI(fullTextPdfUrl);
		return new FullTextResourceDetails(fullTextPdfUri, linkText, "application/pdf");
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
		ArticleDetails details = nac.getDetails();
		System.out.println(details.toString());
	}

}
