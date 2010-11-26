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
package wwmm.pubcrawler.journal.nature;

import static wwmm.pubcrawler.core.CrawlerConstants.NATURE_HOMEPAGE_URL;
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
import wwmm.pubcrawler.core.DOI;
import wwmm.pubcrawler.core.IssueCrawler;
import wwmm.pubcrawler.core.IssueDescription;

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
	
	public NatureIssueCrawler(String abbreviation) {
		this((NatureJournal)NatureJournal.getJournal(abbreviation));
	}

	protected void readProperties() {
		issueInfo.infoPath = ".//x:a[.='Current issue']";
		issueInfo.yearIssueRegex = "/[^/]*/journal/v(\\d+)/n(\\d+)";
		issueInfo.matcherGroupCount = 2;
		issueInfo.yearMatcherGroup = 1;
		issueInfo.issueMatcherGroup = 2;
		
		issueInfo.currentIssueHtmlStart = NATURE_HOMEPAGE_URL+"/";
		issueInfo.currentIssueHtmlEnd = "/index.html";
		
		issueInfo.useVolume = true;
		
//		String issueUrl = NATURE_HOMEPAGE_URL+"/"+journal.getAbbreviation()+"/journal/v"+volume+"/n"+issueId+"/index.html";
		issueInfo.issueUrlStart     = NATURE_HOMEPAGE_URL+"/";
		issueInfo.issueUrlPreVolumeYear = "/journal/v";
		issueInfo.issueUrlPreIssue  = "/n";
		issueInfo.issueUrlEnd       = "/index.html";

		issueInfo.doiXpath = ".//x:span[@class='doi']";
		issueInfo.beheadDoi = 4;
		issueInfo.articleCrawlerClass = NatureArticleCrawler.class;
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
		String volumeYear = getVolumeFromYearVolume(year, issueInfo.useVolume);
		String issueUrl = issueInfo.issueUrlStart+journal.getAbbreviation()+
			issueInfo.issueUrlPreVolumeYear+volumeYear+issueInfo.issueUrlPreIssue+issueId+issueInfo.issueUrlEnd;
		LOG.info("Started to find DOIs from "+journal.getFullTitle()+", year "+year+", issue "+issueId+".");
		Document issueDoc = httpClient.getResourceHTML(issueUrl);
		List<Node> doiNodes = Utils.queryHTML(issueDoc, issueInfo.doiXpath);
		for (Node doiNode : doiNodes) {
			Element span = (Element)doiNode;
			String doiPostfix = span.getValue();
			doiPostfix = (issueInfo.beheadDoi != null) ? doiPostfix.substring(issueInfo.beheadDoi) : doiPostfix;
			String doiStr = DOI.DOI_SITE_URL+"/"+doiPostfix;
			DOI doi = new DOI(doiStr); 
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
		IssueCrawler nic = new NatureIssueCrawler(NatureJournal.CHEMISTRY);
		nic.mainTest2(5);
	}

}
