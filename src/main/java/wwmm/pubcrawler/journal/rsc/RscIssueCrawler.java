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
package wwmm.pubcrawler.journal.rsc;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Node;

import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import wwmm.pubcrawler.core.model.DOI;
import wwmm.pubcrawler.core.crawler.IssueCrawler;
import wwmm.pubcrawler.core.model.IssueDescription;
import wwmm.pubcrawler.core.model.Journal;
import wwmm.pubcrawler.core.utils.XPathUtils;

/**
 * <p>
 * The <code>RscIssueCrawler</code> class provides a method for obtaining
 * information about all articles from a particular issue of a journal
 * published by the Royal Society of Chemistry.
 * </p>
 * 
 * @author Nick Day
 * @version 1.1
 * 
 */
public class RscIssueCrawler extends IssueCrawler {

	private static final Logger LOG = Logger.getLogger(RscIssueCrawler.class);

	/**
	 * <p>
	 * Creates an instance of the RscIssueCrawler class and
	 * specifies the journal of the issue to be crawled.
	 * </p>
	 * 
	 * @param journal of the article to be crawled.
	 * 
	 */
	public RscIssueCrawler(Journal journal) {
		this.journal = journal;
	}
	
	public RscIssueCrawler(String abbreviation) {
		this(RscJournalIndex.getIndex().getJournal(abbreviation));
	}


	protected void readProperties() {
		issueInfo.infoPath = ".//x:div[@class='chem_title']";
		issueInfo.yearIssueRegex = "\\s*\\w*\\s*-\\s*(\\d+),\\sIss.\\s*(\\d+).*";
		issueInfo.matcherGroupCount = 2;
		issueInfo.yearMatcherGroup = 2;
		issueInfo.issueMatcherGroup = 1;
		issueInfo.currentIssueHtmlStart = "???";
		issueInfo.currentIssueHtmlEnd = "???";
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
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair("issueID", ""));
        parameters.add(new BasicNameValuePair("jname", journal.getFullTitle()));
        parameters.add(new BasicNameValuePair("name", journal.getAbbreviation()));
        HttpPost post = new HttpPost("http://pubs.rsc.org/en/Journals/Issues");
        try {
            post.setEntity(new UrlEncodedFormEntity(parameters));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Unable to encode form", e);
        }
        return httpClient.getPostResultXML(post);
	}

	private HttpPost createIssuePagePostMethod(String issueId) {
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair("isContentAvailable", "true"));
		parameters.add(new BasicNameValuePair("issueID", issueId));
		parameters.add(new BasicNameValuePair("jname", journal.getFullTitle()));
		parameters.add(new BasicNameValuePair("name", journal.getAbbreviation().toUpperCase()));
        HttpPost post = new HttpPost("http://pubs.rsc.org/en/Journals/Issues");
        try {
            post.setEntity(new UrlEncodedFormEntity(parameters));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Unable to encode form", e);
        }
        return post;
	}

	/**
	 * <p>
	 * Gets the DOIs of all articles in the issue 
	 * defined by the <code>RscJournal</code> and the provided
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
		String rscIssueId = getRscIssueId(details);
		Document issueDoc = httpClient.getPostResultXML(createIssuePagePostMethod(rscIssueId));
		LOG.info("Started to find DOIs from "+journal.getFullTitle()+", year "+year+", issue "+issueId+".");
		String articleLinkXpath = ".//x:a[contains(@href,'/ArticleLanding/')]";
		List<Node> articleNodes = XPathUtils.queryHTML(issueDoc, articleLinkXpath);

		if (articleNodes.size() == 0) {
			issueDoc = httpClient.getPostResultXML(createIssuePagePostMethod(""));
			articleNodes = XPathUtils.queryHTML(issueDoc, articleLinkXpath);
		}

		List<DOI> dois = new ArrayList<DOI>();
		for (Node articleNode : articleNodes) {
			Element articleElement = (Element)articleNode;
			String articleId = articleElement.getAttributeValue("name");
			if (StringUtils.isEmpty(articleId)) {
				continue;
			}
			/*
			if (!isArticle(text)) {
				continue;
			}
			 */
			String doiStr = DOI.DOI_SITE_URL+"/10.1039/"+articleId;
			DOI doi = new DOI(doiStr);
			dois.add(doi);
		}
		LOG.info("Finished finding issue DOIs: "+dois.size());
		return dois;
	}

	private String getRscIssueId(IssueDescription desc) {
		String year = desc.getYear();
		int yr = Integer.valueOf(year);
		int vol = journal.getVolumeFromYear(yr);
		String volume = ""+vol;
        if (RscJournalIndex.DALTON_TRANSACTIONS.equals(journal) ||
				RscJournalIndex.CHEMICAL_COMMUNICATIONS.equals(journal) &&
				yr < 2010) {
			volume = year.substring(2);
		}
		String issueId = desc.getIssueId();
		String journalAbbreviation = journal.getAbbreviation().toUpperCase();
		return journalAbbreviation+padWithLeadingZeros(volume)+padWithLeadingZeros(issueId);
	}

	private String padWithLeadingZeros(String s) {
		StringBuilder sb = new StringBuilder();
		for (int i = s.length(); i < 3; i++) {
			sb.append("0");
		}
		sb.append(s);
		return sb.toString();
	}

	/**
	 * <p>
	 * Unfortunately RSC give DOIs to the electronic version of their front 
	 * cover, contents list, back cover a few other things that aren't actually
	 * articles.  As we want to write an article crawler that fails if it can't 
	 * find the full-text HTML of an article, we don't want these non-article 
	 * DOIs being passed further down the crawler's processing.  They are 
	 * weeded out by this method.
	 * </p>
	 * 
	 * @param text
	 * @return boolean stating whether the doiElement links to an article or not
	 * 
	 */
	private boolean isArticle(String text) {
		if (text.contains("Cover") || 
				text.contains("Matter") ||
				text.contains("Editorial") ||
				text.trim().equals("Contents"))  {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * <p>
	 * Main method only for demonstration of class use. Does not require
	 * any arguments.
	 * </p>
	 * 
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		IssueCrawler acf = new RscIssueCrawler(RscJournalIndex.CHEMICAL_COMMUNICATIONS);
		acf.mainTest2(10);
	}

}
