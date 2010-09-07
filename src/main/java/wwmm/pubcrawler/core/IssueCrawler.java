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

import java.util.ArrayList;
import java.util.List;

import nu.xom.Document;

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
	
	protected static int MAX_ARTICLES_TO_CRAWL = Integer.MAX_VALUE;
	
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
	 * Gets information to identify the last published issue of a journal (which
	 * is defined in the subclass).
	 * </p>
	 * 
	 * @return the year and issue identifier.
	 * 
	 */
	abstract public IssueDescription getCurrentIssueDescription();

	/**
	 * <p>
	 * Gets the HTML of the table of contents of the last published issue of the
	 * subclass journal.
	 * </p>
	 * 
	 * @return HTML of the issue table of contents as an XML Document.
	 * 
	 */
	abstract public Document getCurrentIssueHtml();

	/**
	 * <p>
	 * Gets the DOIs of all of the articles from the last published issue of the
	 * subclass journal.
	 * </p>
	 * 
	 * @return a list of the DOIs of the articles.
	 * 
	 */
	abstract public List<DOI> getCurrentIssueDOIs();

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
	 * Gets information describing all articles in the issue defined by the
	 * subclass journal and the provided year and issue identifier (wrapped in
	 * the <code>issueDetails</code> parameter.
	 * </p>
	 * 
	 * @param issueDetails
	 *            - contains the year and issue identifier of the issue to be
	 *            crawled.
	 * 
	 * @return a list where each item contains the details for a particular
	 *         article from the issue.
	 * 
	 */
	abstract public List<ArticleDescription> getArticleDescriptions(
			IssueDescription issueDescription);
	
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
	abstract public List<ArticleDescription> getArticleDescriptions(List<DOI> dois);
	
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
	protected List<ArticleDescription> getArticleDescriptions(ArticleCrawler articleCrawler, List<DOI> dois) {
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

}
