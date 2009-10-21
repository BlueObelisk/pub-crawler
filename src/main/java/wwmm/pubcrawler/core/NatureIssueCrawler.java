package wwmm.pubcrawler.core;

import static wwmm.pubcrawler.core.CrawlerConstants.NATURE_HOMEPAGE_URL;
import static wwmm.pubcrawler.core.CrawlerConstants.X_XHTML;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Node;
import nu.xom.Nodes;

import org.apache.log4j.Logger;

import wwmm.pubcrawler.Utils;

/**
 * <p>
 * The <code>NatureIssueCrawler</code> class provides a method for obtaining
 * information about all articles from a particular issue of a journal
 * published by the Nature Publishing Group.
 * </p>
 * 
 * @author Nick Day
 * @version 0.1
 * 
 */
public class NatureIssueCrawler extends IssueCrawler {

	private NatureJournal journal;

	private static final Logger LOG = Logger.getLogger(NatureIssueCrawler.class);

	/**
	 * <p>
	 * Creates an instance of the NatureIssueCrawler class and
	 * specifies the journal of the issue to be crawled.
	 * </p>
	 * 
	 * @param doi of the article to be crawled.
	 */
	public NatureIssueCrawler(NatureJournal journal) {
		this.journal = journal;
	}

	/**
	 * <p>
	 * Gets information to identify the last published issue of a
	 * the provided <code>AcsJournal</code>.
	 * </p>
	 * 
	 * @return the year and issue identifier.
	 * 
	 */
	@Override
	public IssueDetails getCurrentIssueDetails() {
		Document doc = getCurrentIssueHtml();
		Nodes currentIssueNds = doc.query(".//x:a[.='Current issue']", X_XHTML);
		if (currentIssueNds.size() != 1) {
			throw new CrawlerRuntimeException("Expected to find 1 element containing" +
					" a link to the current issue TOC but found "+currentIssueNds.size()+".");
		}
		String urlPostfix = ((Element)currentIssueNds.get(0)).getAttributeValue("href");
		Pattern pattern = Pattern.compile("/"+journal.getAbbreviation()+"/journal/v(\\d+)/n(\\d+)");
		Matcher matcher = pattern.matcher(urlPostfix);
		if (!matcher.find() || matcher.groupCount() != 2) {
			throw new CrawlerRuntimeException("Could not extract the year/issue information.");
		}
		String volume = matcher.group(1);
		String number = matcher.group(2);
		String year = String.valueOf(Integer.valueOf(volume)+journal.getVolumeOffset());
		LOG.debug("Found latest issue details for Nature journal "+journal.getFullTitle()+": year="+year+", issue="+number+".");
		return new IssueDetails(year, number);
	}

	/**
	 * <p>
	 * Gets the HTML of the table of contents of the last 
	 * published issue of the provided journal.
	 * </p>
	 * 
	 * @return HTML of the issue table of contents.
	 * 
	 */
	@Override
	public Document getCurrentIssueHtml() {
		String url = NATURE_HOMEPAGE_URL+"/"+journal.getAbbreviation()+"/index.html";
		URI issueUri = createURI(url);
		return httpClient.getResourceHTML(issueUri);
	}

	/**
	 * <p>
	 * Gets the DOIs of all of the articles from the last 
	 * published issue of the provided journal.
	 * </p> 
	 * 
	 * @return a list of the DOIs of the articles.
	 * 
	 */
	@Override
	public List<DOI> getCurrentIssueDOIs() {
		IssueDetails details = getCurrentIssueDetails();
		return getDOIs(details);
	}

	/**
	 * <p>
	 * Gets the DOIs of all articles in the issue defined
	 * by the <code>AcsJournal</code> and the provided
	 * year and issue identifier (wrapped in the 
	 * <code>issueDetails</code> parameter.
	 * </p>
	 * 
	 * @param issueDetails - contains the year and issue
	 * identifier of the issue to be crawled.
	 * 
	 * @return a list of the DOIs of the articles for the issue.
	 * 
	 */
	@Override
	public List<DOI> getDOIs(IssueDetails issueDetails) {
		String year = issueDetails.getYear();
		String issueId = issueDetails.getIssueId();
		List<DOI> dois = new ArrayList<DOI>();
		int volume = Integer.valueOf(year)-journal.getVolumeOffset();
		String issueUrl = NATURE_HOMEPAGE_URL+"/"+journal.getAbbreviation()+"/journal/v"+volume+"/n"+issueId+"/index.html";
		URI issueUri = createURI(issueUrl);
		LOG.info("Started to find DOIs from "+journal.getFullTitle()+", year "+year+", issue "+issueId+".");
		Document issueDoc = httpClient.getResourceHTML(issueUri);
		List<Node> doiNodes = Utils.queryHTML(issueDoc, ".//x:span[@class='doi']");
		for (Node doiNode : doiNodes) {
			Element span = (Element)doiNode;
			String doiPostfix = span.getValue().substring(4);
			String doiStr = DOI.DOI_SITE_URL+"/"+doiPostfix;
			DOI doi = new DOI(createURI(doiStr)); 
			dois.add(doi);
		}
		LOG.info("Finished finding issue DOIs: "+dois.size());
		return dois;
	}

	/**
	 * <p>
	 * Gets information describing all articles in the issue 
	 * defined by the <code>NatureJournal</code> and the provided
	 * year and issue identifier (wrapped in the 
	 * <code>issueDetails</code> parameter.
	 * </p>
	 * 
	 * @param issueDetails - contains the year and issue
	 * identifier of the issue to be crawled.
	 * 
	 * @return a list where each item contains the details for 
	 * a particular article from the issue.
	 * 
	 */
	@Override
	public List<ArticleDetails> getDetailsForArticles(IssueDetails details) {
		List<DOI> dois = getDOIs(details);
		List<ArticleDetails> adList = new ArrayList<ArticleDetails>(dois.size());
		for (DOI doi : dois) {
			ArticleDetails ad = new NatureArticleCrawler(doi).getDetails();
			adList.add(ad);
		}
		LOG.info("Finished finding issue article details: "+details.getYear()+"-"+details.getIssueId());
		return adList;
	}

	/**
	 * <p>
	 * Main method only for demonstration of class use. Does not require
	 * any arguments.
	 * </p>
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		NatureIssueCrawler nic = new NatureIssueCrawler(NatureJournal.CHEMISTRY);
		IssueDetails details = nic.getCurrentIssueDetails();
		/*
		List<DOI> dois = nic.getDOIs(details);
		for (DOI doi : dois) {
			System.out.println(doi.toString());
		}
		 */
		List<ArticleDetails> adList = nic.getDetailsForArticles(details);
		for (ArticleDetails ad : adList) {
			System.out.println(ad.toString());
		}
	}

}
