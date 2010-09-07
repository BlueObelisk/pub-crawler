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
import nu.xom.Node;
import nu.xom.Nodes;

import org.apache.log4j.Logger;

import wwmm.pubcrawler.Utils;

/**
 * <p>
 * The <code>AcsIssueCrawler</code> class provides a method for obtaining
 * information about all articles from a particular issue of a journal
 * published by the American Chemical Society.
 * </p>
 * 
 * @author Nick Day
 * @version 1.1
 * 
 */
public class AcsIssueCrawler extends IssueCrawler {

	private AcsJournal journal;

	private static final Logger LOG = Logger.getLogger(AcsIssueCrawler.class);

	/**
	 * <p>
	 * Creates an instance of the AcsIssueCrawler class and
	 * specifies the journal of the issue to be crawled.
	 * </p>
	 * 
	 * @param doi of the article to be crawled.
	 */
	public AcsIssueCrawler(AcsJournal journal) {
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
	public IssueDescription getCurrentIssueDescription() {
		Document doc = getCurrentIssueHtml();
		Nodes journalInfo = doc.query(".//x:div[@id='tocMeta']", X_XHTML);
		int size = journalInfo.size();
		if (size != 1) {
			throw new CrawlerRuntimeException("Expected to find 1 element containing" +
					" the year/issue information but found "+size+".");
		}
		String info = journalInfo.get(0).getValue().trim();
		Pattern pattern = Pattern.compile("[^,]*,\\s*(\\d+)\\s+Volume\\s+(\\d+),\\s+Issue\\s+(\\d+)\\s+Pages\\s+(\\d+-\\d+).*");
		Matcher matcher = pattern.matcher(info);
		if (!matcher.find() || matcher.groupCount() != 4) {
			throw new CrawlerRuntimeException("Could not extract the year/issue information.");
		}
		String year = matcher.group(1);
		String issueId = matcher.group(3);
		LOG.debug("Found latest issue details for ACS journal "+journal.getFullTitle()+": year="+year+", issue="+issueId+".");
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
		String issueUrl = "http://pubs.acs.org/toc/"+journal.getAbbreviation()+"/current";
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
	public List<DOI> getDois(IssueDescription issueDetails) {
		String year = issueDetails.getYear();
		String issueId = issueDetails.getIssueId();
		List<DOI> dois = new ArrayList<DOI>();
		int volume = Integer.valueOf(year)-journal.getVolumeOffset();
		String issueUrl = ACS_HOMEPAGE_URL+"/toc/"+journal.getAbbreviation()+"/"+volume+"/"+issueId;
		LOG.info("Started to find DOIs from "+journal.getFullTitle()+", year "+year+", issue "+issueId+".");
		Document issueDoc = httpClient.getResourceHTML(issueUrl);
		List<Node> doiNodes = Utils.queryHTML(issueDoc, ".//x:div[@class='DOI']");
		for (Node doiNode : doiNodes) {
			String contents = ((Element)doiNode).getValue();
			String doiPostfix = contents.replaceAll("DOI:", "").trim();
			String doiUrl = DOI.DOI_SITE_URL+"/"+doiPostfix;
			DOI doi = new DOI(doiUrl); 
			dois.add(doi);
		}
		LOG.info("Finished finding issue DOIs: "+dois.size());
		return dois;
	}

	/**
	 * <p>
	 * Gets information describing all articles in the issue 
	 * defined by the <code>AcsJournal</code> and the provided
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
	public List<ArticleDescription> getArticleDescriptions(IssueDescription details) {
		List<DOI> dois = getDois(details);
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
		return getArticleDescriptions(new AcsArticleCrawler(), dois);
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
		AcsIssueCrawler acf = new AcsIssueCrawler(AcsJournal.CRYSTAL_GROWTH_AND_DESIGN);
		acf.setMaxArticlesToCrawl(10);
		//IssueDetails details = acf.getCurrentIssueDetails();
		IssueDescription details = new IssueDescription("2008", "5");
		List<ArticleDescription> adList = acf.getArticleDescriptions(details);
		for (ArticleDescription ad : adList) {
			System.out.println(ad.toString());
		}
	}

}
