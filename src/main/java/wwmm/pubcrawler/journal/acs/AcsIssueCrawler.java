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

import static wwmm.pubcrawler.core.CrawlerConstants.ACS_HOMEPAGE_URL;
import static wwmm.pubcrawler.core.CrawlerConstants.X_XHTML;

import java.util.ArrayList;
import java.util.List;

import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Node;
import nu.xom.Nodes;

import org.apache.log4j.Logger;

import wwmm.pubcrawler.Utils;
import wwmm.pubcrawler.core.DOI;
import wwmm.pubcrawler.core.IssueCrawler;
import wwmm.pubcrawler.core.IssueDescription;

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

	static final Logger LOG = Logger.getLogger(AcsIssueCrawler.class);

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
	
	public AcsIssueCrawler(String abbreviation) {
		this((AcsJournal)AcsJournal.getJournal(abbreviation));
	}

	protected void readProperties() {
		issueInfo.infoPath = ".//x:div[@id='tocMeta']";
		issueInfo.yearIssueRegex = "[^,]*,\\s*(\\d+)\\s+Volume\\s+(\\d+),\\s+Issue\\s+(\\d+)\\s+Pages\\s+(\\d+-\\d+).*";
		issueInfo.matcherGroupCount = 4;
		issueInfo.yearMatcherGroup = 1;
		issueInfo.issueMatcherGroup = 3;
		issueInfo.currentIssueHtmlStart = "http://pubs.acs.org/toc/";
		issueInfo.currentIssueHtmlEnd = "/current";
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
	 * Main method only for demonstration of class use. Does not require
	 * any arguments.
	 * </p>
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		IssueCrawler acf = new AcsIssueCrawler(AcsJournal.CRYSTAL_GROWTH_AND_DESIGN);
		acf.mainTest("2008", "5", 10);
	}

}
