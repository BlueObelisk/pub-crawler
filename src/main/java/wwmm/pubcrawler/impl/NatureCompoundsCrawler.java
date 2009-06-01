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

import wwmm.pubcrawler.core.ArticleDetails;
import wwmm.pubcrawler.core.CrawlerHttpClient;
import wwmm.pubcrawler.core.FullTextResourceDetails;
import wwmm.pubcrawler.core.NatureIssueCrawler;
import wwmm.pubcrawler.core.NatureJournal;

/**
 * <p>
 * Provides a method of crawling an issue of a journal published
 * by Nature and extracting information about each compound 
 * mentioned in the full-text of each article.
 * 
 * HUGE NOTE: you will need to have a subscription to the
 * Nature journal of choice for this to work, as it requires
 * access to article full-text.
 * </p>
 * 
 * @author Nick Day
 * @version 0.1
 *
 */
public class NatureCompoundsCrawler {

	private final CrawlerHttpClient httpClient = new CrawlerHttpClient();
	private NatureJournal journal;

	private static final Logger LOG = Logger.getLogger(NatureCompoundsCrawler.class);

	// hide the default constructor
	private NatureCompoundsCrawler() {
		;
	}
	
	public NatureCompoundsCrawler(NatureJournal journal) {
		this.journal = journal;
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
		NatureIssueCrawler crawler = new NatureIssueCrawler(journal);
		List<ArticleDetails> articleDetailsList = crawler.getDetailsForCurrentArticles();
		List<ArticleData> articleDataList = new ArrayList<ArticleData>(articleDetailsList.size());
		for (ArticleDetails ad : articleDetailsList) {
			URI fullTextHtmlUri = getArticleFullTextHtmlUri(ad);
			if (fullTextHtmlUri == null) {
				LOG.warn("Could not find a full-text URI for article with DOI: "+ad.getDoi().toString());
				continue;
			}
			List<CompoundDetails> cdList = getCompoundDetailsList(fullTextHtmlUri);
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
	private List<CompoundDetails> getCompoundDetailsList(URI fullTextHtmlUri) {
		Document fullTextDoc = httpClient.getResourceHTML(fullTextHtmlUri);
		Nodes compoundLinkNds = fullTextDoc.query(".//x:a[contains(@href,'/compound/')]", X_XHTML);
		Set<String> compoundUrls = new HashSet<String>();
		for (int i = 0; i < compoundLinkNds.size(); i++) {
			Element compoundLink = (Element)compoundLinkNds.get(i);
			String compoundUrl = NATURE_HOMEPAGE_URL+compoundLink.getAttributeValue("href");
			compoundUrls.add(compoundUrl);
		}
		List<CompoundDetails> cdList = new ArrayList<CompoundDetails>(compoundUrls.size());
		int count = 1;
		for (String compoundUrl : compoundUrls) {
			LOG.info("Finding compound info at: "+compoundUrl);
			URI splashPageUri = createUri(compoundUrl);
			if (splashPageUri == null) {
				continue;
			}
			Document splashPageDoc = httpClient.getResourceHTML(splashPageUri);
			String cmlUrl = getCmlFileUrl(splashPageDoc);
			URI cmlUri = createUri(cmlUrl);
			String molUrl = cmlUrl.replaceAll("cml", "mol");
			URI molUri = createUri(molUrl);
			String chemDrawUrl = cmlUrl.replaceAll("cml", "cdx");
			chemDrawUrl = chemDrawUrl.replaceAll("/cdx/", "/chemdraw/");
			URI chemDrawUri = createUri(chemDrawUrl);
			cdList.add(new CompoundDetails(""+count, splashPageUri, cmlUri, molUri, chemDrawUri));
			count++;
		}
		return cdList;
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
	 */
	private String getCmlFileUrl(Document splashPageDoc) {
		Nodes cmlFileLinkNds = splashPageDoc.query(".//x:a[contains(@href,'/cml/')]", X_XHTML);
		if (cmlFileLinkNds.size() != 1) {
			LOG.warn("Could not find link to CML file.");
		}
		Element cmlLink = (Element)cmlFileLinkNds.get(0);
		return NATURE_HOMEPAGE_URL+cmlLink.getAttributeValue("href");
	}

	/**
	 * <p>
	 * Gets the URI of the full-text HTML for the provided
	 * <code>ArticleDetails</code>.  If none is found then null
	 * will be returned.
	 * </p>
	 * 
	 * @param ad - <code>ArticleDetails</code> of the article you
	 * wish to find the full-text HTML for.
	 * 
	 * @return the URI of the full-text HTML for the provided
	 * <code>ArticleDetails</code>.  If none is found then null
	 * will be returned.
	 */
	private URI getArticleFullTextHtmlUri(ArticleDetails ad) {
		List<FullTextResourceDetails> fullTexts = ad.getFullTextResources();
		for (FullTextResourceDetails ftrd : fullTexts) {
			if (ftrd.getContentType().contains("html")) {
				return ftrd.getURI();
			}
		}
		return null;
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

		private ArticleDetails ad;
		private List<CompoundDetails> cdList;

		// hide the default constructor
		private ArticleData() {
			;
		}

		public ArticleData(ArticleDetails ad, List<CompoundDetails> cdList) {
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
		public ArticleDetails getArticleDetails() {
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
		private URI splashPageUri;
		private URI cmlUri;
		private URI molUri;
		private URI chemDrawUri;

		// hide the default constructor
		private CompoundDetails() {
			;
		}

		public CompoundDetails(String id, URI splashPageUri, URI cmlUri, URI molUri, URI chemDrawUri) {
			this.id = id;
			this.splashPageUri = splashPageUri;
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
		public URI getSplashPageUri() {
			return splashPageUri;
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
	
	/**
	 * <p>
	 * Main method meant for demonstration purposes only. Requires
	 * no arguments.
	 * </p>
	 * 
	 */
	public static void main(String[] args) {
		NatureCompoundsCrawler ncc = new NatureCompoundsCrawler(NatureJournal.CHEMISTRY);
		List<ArticleData> adList = ncc.crawlCurrentIssue();
		for (ArticleData ad : adList) {
			System.out.println(ad.getArticleDetails());
			List<CompoundDetails> cdList = ad.getCompoundDetailsList();
			for (CompoundDetails cd : cdList) {
				System.out.println(cd.getSplashPageUri());
				System.out.println(cd.getChemDrawUri());
				System.out.println(cd.getMolUri());
				System.out.println("---------------------------------------");
			}
			System.out.println("======================================================");
		}
	}

}
