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
package wwmm.pubcrawler.journal.chemsocjapan;

import java.util.ArrayList;
import java.util.List;

import nu.xom.Attribute;
import nu.xom.Document;
import nu.xom.Node;

import org.apache.log4j.Logger;

import wwmm.pubcrawler.core.utils.Utils;
import wwmm.pubcrawler.core.crawler.IssueCrawler;
import wwmm.pubcrawler.core.model.ArticleDescription;
import wwmm.pubcrawler.core.model.DOI;
import wwmm.pubcrawler.core.model.IssueDescription;
import wwmm.pubcrawler.core.model.Journal;

/**
 * <p>
 * The <code>ChemSocJapanIssueCrawler</code> class provides a method for 
 * obtaining information about all articles from a particular issue of a 
 * journal published by the Chemical Society of Japan.
 * </p>
 * 
 * @author Nick Day
 * @version 1.1
 * 
 */
public class ChemSocJapanIssueCrawler extends IssueCrawler {

	private static final Logger LOG = Logger.getLogger(ChemSocJapanIssueCrawler.class);

	/**
	 * <p>
	 * Creates an instance of the ChemSocJapanIssueCrawler class and
	 * specifies the journal of the issue to be crawled.
	 * </p>
	 * 
	 * @param journal of the article to be crawled.
	 */
	public ChemSocJapanIssueCrawler(Journal journal) {
		this.journal = journal;
	}

	public ChemSocJapanIssueCrawler(String abbreviation) {
		this(ChemSocJapanJournalIndex.getIndex().getJournal(abbreviation));
	}

	protected void readProperties() {
		issueInfo.infoPath = "//x:span[@class='augr']";
		issueInfo.yearIssueRegex = "[^,]*,\\s+\\w+\\.\\s+(\\d+)\\s+\\([^,]*,\\s+(\\d\\d\\d\\d)\\)";
		issueInfo.matcherGroupCount = 2;
		issueInfo.yearMatcherGroup = 2;
		issueInfo.issueMatcherGroup = 1;
		issueInfo.currentIssueHtmlStart = "http://www.csj.jp/journals/";
		issueInfo.currentIssueHtmlEnd = "/cl-cont/newissue.html";
		
	}


	/**
	 * <p>
	 * Gets the DOIs of all articles in the issue defined
	 * by the <code>ChemSocJapanJournal</code> and the provided	
	 * year and issue identifier (wrapped in the 
	 * <code>issueDetails</code> parameter.
	 * </p>
	 * 
	 * @param details - contains the year and issue
	 * identifier of the issue to be crawled.
	 * 
	 * @return a list of the DOIs of the articles for the issue.
	 * 
	 */
	@Override
	public List<DOI> getDois(IssueDescription details) {
		String year = details.getYear();
		String issueId = details.getIssueId();
		String issueUrl = "http://www.chemistry.or.jp/journals/"+journal.getAbbreviation()+"/cl-cont/cl"+year+"-"+issueId+".html";
		LOG.info("Started to find DOIs from "+journal.getFullTitle()+", year "+year+", issue "+issueId+".");
		Document issueDoc = httpClient.getResourceHTML(issueUrl);
		List<DOI> dois = new ArrayList<DOI>();
		List<Node> doiNodes = Utils.queryHTML(issueDoc, ".//x:a[contains(@href,'http://www.is.csj.jp/cgi-bin/journals/pr/index.cgi?n=li') and not(contains(@href,'li_s'))]/@href");
		for (Node doiNode : doiNodes) {
			String link = ((Attribute)doiNode).getValue();
			int idx = link.indexOf("id=");
			String articleId = link.substring(idx+3).replaceAll("/", ".");
			String doiStr = "http://dx.doi.org/10.1246/"+articleId;
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
		Journal journal = ChemSocJapanJournalIndex.CHEMISTRY_LETTERS;
		IssueCrawler acf = new ChemSocJapanIssueCrawler(journal);
		IssueDescription details = acf.getCurrentIssueDescription();
		List<ArticleDescription> adList = acf.getArticleDescriptions(details);
		for (ArticleDescription ad : adList) {
			System.out.println(ad.toString());
		}
	}
}
