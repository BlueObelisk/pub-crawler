package wwmm.pubcrawler.impl;

import static wwmm.pubcrawler.core.CrawlerConstants.NATURE_HOMEPAGE_URL;
import static wwmm.pubcrawler.core.CrawlerConstants.X_XHTML;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Nodes;

import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import wwmm.pubcrawler.core.ArticleDetails;
import wwmm.pubcrawler.core.CrawlerHttpClient;
import wwmm.pubcrawler.core.FullTextResourceDetails;
import wwmm.pubcrawler.core.IssueDetails;
import wwmm.pubcrawler.core.NatureIssueCrawler;
import wwmm.pubcrawler.core.NatureJournal;
import wwmm.pubcrawler.core.OreTool;

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
	private NatureIssueCrawler crawler;

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
		List<ArticleDetails> articleDetailsList = crawler.getDetailsForCurrentArticles();
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
	public List<ArticleData> crawlIssue(IssueDetails issueDetails) {
		List<ArticleDetails> articleDetailsList = crawler.getDetailsForArticles(issueDetails);
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
	private List<ArticleData> getArticleDatasFromArticleDetails(List<ArticleDetails> adList) {
		List<ArticleData> articleDataList = new ArrayList<ArticleData>(adList.size());
		for (ArticleDetails ad : adList) {
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
		for (String compoundUrl : compoundUrls) {
			// if contains _ci. then is the compound index - we don't want that
			if (compoundUrl.contains("_ci.")) {
				continue;
			}
			LOG.info("Finding compound info at: "+compoundUrl);
			String cmpdId = getCompoundId(compoundUrl);
			if (cmpdId == null) {
				continue;
			}
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
			cdList.add(new CompoundDetails(cmpdId, splashPageUri, cmlUri, molUri, chemDrawUri));
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
	public static void main(String[] args) throws IOException {
		NatureCompoundsCrawler ncc = new NatureCompoundsCrawler(NatureJournal.CHEMISTRY);
		for (int i = 1; i > 0; i--) {
			String year = "2009";
			String issue = ""+i;
			IssueDetails details = new IssueDetails(year, issue);
			List<ArticleData> adList = ncc.crawlIssue(details);
			File rootFile = new File("c:/Users/ned24/workspace/nature-data/nchem/");
			File yearFile = new File(rootFile, year);
			File issueFile = new File(yearFile, issue);
			rootFile.mkdirs();
			for (ArticleData articleData: adList) {
				ArticleDetails ad = articleData.getArticleDetails();
				String doiPostfix = ad.getDoi().getPostfix().replaceAll("\\.", "_");
				doiPostfix = doiPostfix.substring(doiPostfix.lastIndexOf("/")+1);
				File articleFolder = new File(issueFile, doiPostfix);
				File oreFile = new File(articleFolder, doiPostfix+".rdf");
				OreTool ot = new OreTool(oreFile.toURI().toString(), ad);
				File fullTextHtmlFile = new File(articleFolder, doiPostfix+".fulltext.html");
				File fullTextPdfFile = new File(articleFolder, doiPostfix+".fulltext.pdf");
				CrawlerHttpClient crawler = new CrawlerHttpClient();
				for (FullTextResourceDetails ftrd : ad.getFullTextResources()) {
					if (ftrd.getContentType().contains("html")) {
						crawler.writeResourceToFile(ftrd.getURI(), fullTextHtmlFile);
					} else if (ftrd.getContentType().contains("pdf")) {
						crawler.writeResourceToFile(ftrd.getURI(), fullTextPdfFile);
					}
				}
				FileUtils.writeStringToFile(oreFile, ot.getORE());
				List<CompoundDetails> cdList = articleData.getCompoundDetailsList();
				for (CompoundDetails cd : cdList) {
					String cmpdId = cd.getID();
					File cmpdFolder = new File(articleFolder, cmpdId);
					crawler.writeResourceToFile(cd.getSplashPageUri(), new File(cmpdFolder, cmpdId+".splash.html"));
					crawler.writeResourceToFile(cd.getCmlUri(), new File(cmpdFolder, cmpdId+".cml"));
					crawler.writeResourceToFile(cd.getMolUri(), new File(cmpdFolder, cmpdId+".mol"));
					crawler.writeResourceToFile(cd.getChemDrawUri(), new File(cmpdFolder, cmpdId+".cdx"));
				}
			}
		}
	}

}
