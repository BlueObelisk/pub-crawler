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
package wwmm.pubcrawler.core.crawler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import nu.xom.Document;
import nu.xom.Node;
import wwmm.pubcrawler.core.CrawlerRuntimeException;
import wwmm.pubcrawler.core.model.*;
import wwmm.pubcrawler.core.utils.XPathUtils;

/**
 * <p>
 * The <code>IssueCrawler</code> class provides an outline for the methods that
 * a crawler of a journal issue should implement.
 * </p>
 * 
 * @author Nick Day
 * @version 1.1
 * @todo consider refactoring the getDetailsForCurrentArticles method out and
 *       making this an interface
 */
public abstract class IssueCrawler extends Crawler {
	private static Logger LOG = Logger.getLogger(IssueCrawler.class);
	
	protected static int MAX_ARTICLES_TO_CRAWL = Integer.MAX_VALUE;
	protected IssueInfo issueInfo;
	public Journal journal;
	
	protected IssueCrawler() {
		this.issueInfo = new IssueInfo();
		readProperties();
	}
	
	protected abstract void readProperties();
	
	public void setMaxArticlesToCrawl(int i) {
		if (i < 0) {
			throw new IllegalArgumentException("Cannot set max number of articles to crawl to less than 0.");
		}
		MAX_ARTICLES_TO_CRAWL = i;
	}
	
	public int getMaxArticlesToCrawl() {
		return MAX_ARTICLES_TO_CRAWL;
	}

		/**
	 * <p>
	 * Gets the DOIs of all articles in the issue defined by the subclass
	 * journal and the provided year and issue identifier (wrapped in the
	 * <code>issueDetails</code> parameter.
	 * </p>
	 * 
	 * @param issueDescription
	 *            - contains the year and issue identifier of the issue to be
	 *            crawled.
	 * 
	 * @return a list of the DOIs of the articles for the issue.
	 * 
	 */
	abstract public List<DOI> getDois(IssueDescription issueDescription);

	/**
	 * <p>
	 * Uses the provided article crawler to get the details for all the articles 
	 * that are found at the provided DOIs.  Will only crawl the number of articles
	 * that MAX_ARTICLE_TO_CRAWL has been set to.
	 * </p>
	 * 
	 * @param articleCrawler - crawler to use to crawl the articles at the provided
	 * DOIs.
	 * @param dois - DOIs for the articles that are to be crawled. 
	 * 
	 * @return list of <code>ArticleDetails</code> describing the articles at the
	 * provided DOIs.
	 */
	public List<ArticleDescription> getArticleDescriptions(List<DOI> dois) {
		ArticleCrawler articleCrawler = null;
		try {
			articleCrawler = (ArticleCrawler) issueInfo.articleCrawlerClass.newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Cannot create article crawler", e);
		}
		List<ArticleDescription> adList = new ArrayList<ArticleDescription>();
		int count = 0;
		for (DOI doi : dois) {
			if (count >= MAX_ARTICLES_TO_CRAWL) {
				break;
			}
			articleCrawler.setDOI(doi);
			ArticleDescription ad = articleCrawler.getDetails();
			adList.add(ad);
			count++;
		}
		return adList;
	}

	/**
	 * <p>
	 * Gets information describing all articles in the current issue of the
	 * journal as defined in the implemented subclass.
	 * </p>
	 * 
	 * @return a list where each item contains the details for a particular
	 *         article from the issue.
	 */
	final public List<ArticleDescription> getCurrentArticleDescriptions() {
		IssueDescription issueDetails = getCurrentIssueDescription();
		return getArticleDescriptions(issueDetails);
	}
	
	/**
	 * <p>
	 * Gets the DOIs for all articles in the current issue of the 
	 * journal as defined in the implemented subclass.
	 * </p>
	 * 
	 * @return a list where each item is the DOI for a particular 
	 * article from the current issue.
	 */
	final public List<DOI> getDoisForCurrentArticles() {
		IssueDescription issueDetails = getCurrentIssueDescription();
		return getDois(issueDetails);
	}

	/**
	 * <p>
	 * Gets information describing all articles in the issue 
	 * defined by the <code>RscJournal</code> and the provided
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
	public List<ArticleDescription> getArticleDescriptions(IssueDescription details) {
		List<DOI> dois = getDois(details);
		return getArticleDescriptions(dois);
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
	protected List<DOI> getCurrentIssueDOIs() {
		IssueDescription details = getCurrentIssueDescription();
		return getDois(details);
	}

	protected String getJournalInfo() throws IOException {
		Document doc = getCurrentIssueHtml();
		List<Node> journalInfoNodes = XPathUtils.queryHTML(doc, issueInfo.infoPath);
		int size = journalInfoNodes.size();
		if (size != 1) {
			throw new CrawlerRuntimeException("Expected to find 1 element containing" +
					" the year/issue information but found "+size+".");
		}
		String info = journalInfoNodes.get(0).getValue().trim();
		return info;
	}

	protected IssueDescription createIssueDescription(String info) {
		Pattern pattern = Pattern.compile(issueInfo.yearIssueRegex);
		Matcher matcher = pattern.matcher(info);
		if (!matcher.find() || matcher.groupCount() != issueInfo.matcherGroupCount) {
			throw new CrawlerRuntimeException("Could not extract the year/issue information.");
		}
		String yearVolume = matcher.group(issueInfo.yearMatcherGroup);
		if (issueInfo.yearVolumeReplaceFrom != null && issueInfo.yearVolumeReplaceTo != null) {
			yearVolume = yearVolume.replaceAll(issueInfo.yearVolumeReplaceFrom, issueInfo.yearVolumeReplaceTo);
		}
		String issueId = matcher.group(issueInfo.issueMatcherGroup);
		if (issueInfo.issueIdReplaceFrom != null && issueInfo.issueIdReplaceTo != null) {
			issueId = issueId.replaceAll(issueInfo.issueIdReplaceFrom, issueInfo.issueIdReplaceTo);
		}
		String year = getYearFromYearVolume(yearVolume, issueInfo.useVolume);
		LOG.debug("Found latest issue details for "+this+" journal "+journal.getFullTitle()+": year="+year+", issue="+issueId+".");
		return new IssueDescription(year, issueId);
	}

	/**
	 * <p>
	 * Gets information to identify the last published issue of a journal 
	 * </p>
	 * 
	 * @return the year and issue identifier.
	 * 
	 */
	public IssueDescription getCurrentIssueDescription() {
        String info = null;
        try {
            info = getJournalInfo();
        } catch (IOException e) {
            throw new RuntimeException("Unable to fetch current issue description", e);
        }
        return createIssueDescription(info);
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
	public Document getCurrentIssueHtml() {
		String issueUrl = issueInfo.currentIssueHtmlStart+journal.getAbbreviation()+issueInfo.currentIssueHtmlEnd;
		return httpClient.getResourceHTML(issueUrl);
	}

	protected String getYearFromYearVolume(String yearVolume, boolean useVolume) {
		return (useVolume) ? ""+(Integer.parseInt(yearVolume)+journal.getVolumeOffset()) : yearVolume;
	}
	
	protected String getVolumeFromYearVolume(String yearVolume, boolean useVolume) {
		return (useVolume) ? ""+(Integer.parseInt(yearVolume)-journal.getVolumeOffset()) : yearVolume;
	}

	public void mainTest(String year, String issue, int maxToCrawl) {
		this.setMaxArticlesToCrawl(maxToCrawl);
		IssueDescription details = new IssueDescription(year, issue);
		List<ArticleDescription> adList = this.getArticleDescriptions(details);
		for (ArticleDescription ad : adList) {
			System.out.println(ad.toString());
		}
	}

	public void mainTest2(int maxToCrawl) {
		this.setMaxArticlesToCrawl(maxToCrawl);
		IssueDescription issueDescription = this.getCurrentIssueDescription();
		List<ArticleDescription> adList = this.getArticleDescriptions(issueDescription);
		for (ArticleDescription ad : adList) {
			System.out.println(ad.toString());
		}
	}
	
}
