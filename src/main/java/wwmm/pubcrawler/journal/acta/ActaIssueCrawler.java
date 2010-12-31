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
package wwmm.pubcrawler.journal.acta;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nu.xom.Attribute;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Node;

import org.apache.log4j.Logger;

import wwmm.pubcrawler.core.types.Doi;
import wwmm.pubcrawler.core.crawler.IssueCrawler;
import wwmm.pubcrawler.core.model.IssueDescription;
import wwmm.pubcrawler.core.model.Journal;
import wwmm.pubcrawler.core.utils.XPathUtils;

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

	private static final Logger LOG = Logger.getLogger(ActaIssueCrawler.class);

	/**
	 * <p>
	 * Creates an instance of the ActaIssueCrawler class and
	 * specifies the journal of the issue to be crawled.
	 * </p>
	 * 
	 * @param doi of the article to be crawled.
	 */
	public ActaIssueCrawler(Journal journal) {
		this.journal = journal;
	}
	
	public ActaIssueCrawler(String abbreviation) {
		this(ActaJournalIndex.getIndex().getJournal(abbreviation));
	}

	protected void readProperties() {
		issueInfo.infoPath = "//x:a[contains(@target,'_parent') and not(contains(. 'preparation')]/@href";
		issueInfo.yearIssueRegex = "\\.\\./issues/(\\d\\d\\d\\d)/(\\d\\d/\\d\\d)/issconts.html";
		issueInfo.matcherGroupCount = 4;
		issueInfo.yearMatcherGroup = 1;
		issueInfo.issueMatcherGroup = 2;
		issueInfo.currentIssueHtmlStart = "http://journals.iucr.org/";
		issueInfo.currentIssueHtmlEnd = "/contents/backissuesbdy.html";
		//
		issueInfo.issueIdReplaceFrom = "/";
		issueInfo.issueIdReplaceTo = "-";
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
	public List<Doi> getDois(IssueDescription issueDescription) {
		String year = issueDescription.getYear();
		String issueId = issueDescription.getIssueId();
		Set<Doi> dois = new HashSet<Doi>();
		String issueUrl = "http://journals.iucr.org/"+journal.getAbbreviation()+"/issues/"
		+year+"/"+issueId.replaceAll("-", "/")+"/isscontsbdy.html";
		LOG.info("Started to find article DOIs from "+journal.getFullTitle()+", year "+year+", issue "+issueId+".");
		Document issueDoc = httpClient.getResourceHTML(issueUrl);
		List<Node> doiNodes = XPathUtils.queryHTML(issueDoc, ".//x:a[contains(@href,'" + Doi.DOI_SITE_URL.resolve("/10.1107/").toString() + "')]/@href");
		for (Node doiNode : doiNodes) {
			String doiStr = ((Attribute)doiNode).getValue();
			Doi doi = new Doi(doiStr);
			dois.add(doi);
		}
		// sometimes the DOIs aren't the href in an <a> tag, so we have to look
		// at the text as well...
		List<Node> textDoiNodes = XPathUtils.queryHTML(issueDoc, ".//x:font[@size='2' and contains(.,'doi:10.1107/')]");
		for (Node doiNode : textDoiNodes) {
			String doiPrefix = ((Element)doiNode).getValue().substring(4);
			dois.add(new Doi(doiPrefix));
		}
		LOG.info("Found issue DOIs: "+dois.size());
		return new ArrayList<Doi>(dois);
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
		IssueCrawler acf = new ActaIssueCrawler(ActaJournalIndex.SECTION_B);
		acf.mainTest2(2);
	}

}
