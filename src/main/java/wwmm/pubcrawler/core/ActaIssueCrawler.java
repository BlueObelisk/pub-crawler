package wwmm.pubcrawler.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nu.xom.Attribute;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Node;

import org.apache.log4j.Logger;

import wwmm.pubcrawler.Utils;

/**
 * <p>
 * The <code>ActaIssueCrawler</code> class provides a method for obtaining
 * information about all articles from a particular issue of a journal
 * published by Acta Crystallographica.
 * </p>
 * 
 * @author Nick Day
 * @version 1.1
 * 
 */
public class ActaIssueCrawler extends IssueCrawler {

	public ActaJournal journal;
	private static final Logger LOG = Logger.getLogger(ActaIssueCrawler.class);

	/**
	 * <p>
	 * Creates an instance of the ActaIssueCrawler class and
	 * specifies the journal of the issue to be crawled.
	 * </p>
	 * 
	 * @param doi of the article to be crawled.
	 */
	public ActaIssueCrawler(ActaJournal journal) {
		this.journal = journal;
	}

	/**
	 * <p>
	 * Gets information to identify the last published issue of a
	 * the provided <code>ActaJournal</code>.
	 * </p>
	 * 
	 * @return the year and issue identifier.
	 * 
	 */
	@Override
	public IssueDescription getCurrentIssueDescription() {
		Document doc = getCurrentIssueHtml();
		List<Node> currentIssueLink = Utils.queryHTML(doc, "//x:a[contains(@target,'_parent')]");
		Node current = currentIssueLink.get(0);
		if (((Element) current).getValue().contains("preparation")) {
			current = currentIssueLink.get(1);
		}
		String info = ((Element)current).getAttributeValue("href");
		Pattern pattern = Pattern.compile("\\.\\./issues/(\\d\\d\\d\\d)/(\\d\\d/\\d\\d)/issconts.html");
		Matcher matcher = pattern.matcher(info);
		if (!matcher.find() || matcher.groupCount() != 2) {
			throw new CrawlerRuntimeException("Could not extract the year/issue information " +
					"from current issue for Acta journal, "+journal.getFullTitle()+".");
		}
		String year = matcher.group(1);
		String issueId = matcher.group(2).replaceAll("/", "-");
		LOG.info("Found latest issue details for Acta journal "+journal.getFullTitle()+": year="+year+", issue="+issueId+".");
		return new IssueDescription(year, issueId);
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
		String issueUrl = "http://journals.iucr.org/"+journal.getAbbreviation()+"/contents/backissuesbdy.html";
		return httpClient.getResourceHTML(issueUrl);
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
		IssueDescription details = getCurrentIssueDescription();
		return getDois(details);
	}

	/**
	 * <p>
	 * Gets the DOIs of all articles in the issue defined
	 * by the <code>ActaJournal</code> and the provided	year 
	 * and issue identifier (wrapped in the 
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
	public List<DOI> getDois(IssueDescription issueDescription) {
		String year = issueDescription.getYear();
		String issueId = issueDescription.getIssueId();
		Set<DOI> dois = new HashSet<DOI>();
		String issueUrl = "http://journals.iucr.org/"+journal.getAbbreviation()+"/issues/"
		+year+"/"+issueId.replaceAll("-", "/")+"/isscontsbdy.html";
		LOG.info("Started to find article DOIs from "+journal.getFullTitle()+", year "+year+", issue "+issueId+".");
		Document issueDoc = httpClient.getResourceHTML(issueUrl);
		List<Node> aTagDoiNodes = Utils.queryHTML(issueDoc, ".//x:a[contains(@href,'"+DOI.DOI_SITE_URL+"/10.1107/')]/@href");
		for (Node doiNode : aTagDoiNodes) {
			String doiStr = ((Attribute)doiNode).getValue();
			DOI doi = new DOI(doiStr);
			dois.add(doi);
		}
		// sometimes the DOIs aren't the href in an <a> tag, so we have to look
		// at the text as well...
		List<Node> textDoiNodes = Utils.queryHTML(issueDoc, ".//x:font[@size='2' and contains(.,'doi:10.1107/')]");
		for (Node doiNode : textDoiNodes) {
			String doiPrefix = ((Element)doiNode).getValue().substring(4);
			DOI doi = new DOI(DOI.DOI_SITE_URL+"/"+doiPrefix);
			dois.add(doi);
		}
		LOG.info("Found issue DOIs: "+dois.size());
		return new ArrayList<DOI>(dois);
	}

	/**
	 * <p>
	 * Gets information describing all articles in the issue 
	 * defined by the <code>ActaJournal</code> and the 
	 * provided	year and issue identifier (wrapped in the 
	 * <code>issueDescription</code> parameter.
	 * </p>
	 * 
	 * @param issueDescription - contains the year and issue
	 * identifier of the issue to be crawled.
	 * 
	 * @return a list where each item contains the details for 
	 * a particular article from the issue.
	 * 
	 */
	@Override
	public List<ArticleDescription> getArticleDescriptions(IssueDescription issueDescription) {
		List<DOI> dois = getDois(issueDescription);
		return getArticleDescriptions(dois);
	}

	/**
	 * <p>
	 * Gets information describing all articles defined by the list
	 * of DOIs provided.
	 * </p>
	 * 
	 * @param dois - a list of DOIs for the article that are to be
	 * crawled.
	 * 
	 * @return a list where each item contains the details for 
	 * a particular article from the issue.
	 * 
	 */
	@Override
	public List<ArticleDescription> getArticleDescriptions(List<DOI> dois) {
		return getArticleDescriptions(new ActaArticleCrawler(), dois);
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
		ActaIssueCrawler acf = new ActaIssueCrawler(ActaJournal.SECTION_B);
		acf.setMaxArticlesToCrawl(2);
		IssueDescription issueDescription = acf.getCurrentIssueDescription();
		List<ArticleDescription> adList = acf.getArticleDescriptions(issueDescription);
		for (ArticleDescription ad : adList) {
			System.out.println(ad.toString());
		}
	}

}
