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
package wwmm.pubcrawler.impl;

import static wwmm.pubcrawler.core.CrawlerConstants.CIF_CONTENT_TYPE;

import java.util.ArrayList;
import java.util.List;

import wwmm.pubcrawler.core.ArticleDescription;
import wwmm.pubcrawler.core.DOI;
import wwmm.pubcrawler.core.IssueCrawler;
import wwmm.pubcrawler.core.IssueDescription;
import wwmm.pubcrawler.core.SupplementaryResourceDescription;

/**
 * <p>
 * The <code>CifIssueCrawler</code> class uses composition to extend
 * an IssueCrawler class.  It should only return those articles
 * in a journal issue that have CIFs (Crystallographic Information 
 * Files) as supplementary data.  Each implementing subclass need only
 * implement <code>isCifFile(SupplementaryFileDetails)</code> to 
 * provide a publisher specific method of determing whether a 
 * supplementary file is a CIF.
 * </p>  
 * 
 * @author Nick Day
 * @version 1.0
 *
 */
public abstract class CifIssueCrawler {
	
	// TODO - provide a 'Factory' way of creating the subclasses of this? 

	protected IssueCrawler crawler;
	
	public CifIssueCrawler(IssueCrawler crawler) {
		this.crawler = crawler;
	}
	
	/**
	 * <p>
	 * Gets details for articles that have a CIF as supplementary data in 
	 * the issue defined by <code>details</code>.
	 * </p>
	 * 
	 * @param details - containing the year and the issue to be crawled.
	 * 
	 * @return list of the details of those articles that have a CIF as
	 * supplementary data.
	 */
	final public List<ArticleDescription> getArticleDescriptions(IssueDescription details) {
		List<ArticleDescription> adList = crawler.getArticleDescriptions(details);
		List<ArticleDescription> cifAdList = new ArrayList<ArticleDescription>();
		for (ArticleDescription ad : adList) {
			if (isCifArticle(ad)) {
				cifAdList.add(ad);
			}
		}
		return cifAdList;
	}
	
	public List<ArticleDescription> getArticleDescriptions(List<DOI> dois) {
		List<ArticleDescription> adList = crawler.getArticleDescriptions(dois);
		List<ArticleDescription> cifAdList = new ArrayList<ArticleDescription>();
		for (ArticleDescription ad : adList) {
			if (isCifArticle(ad)) {
				cifAdList.add(ad);
			}
		}
		return cifAdList;
	}
	
	/**
	 * <p>
	 * Gets details for articles that have a CIF as supplementary data in 
	 * the current issue of the journal.
	 * </p>
	 * 
	 * @return a list of the details of those articles that have a CIF as
	 * supplementary data.
	 */
	final public List<ArticleDescription> getCurrentArticleDescriptions() {
		IssueDescription details = crawler.getCurrentIssueDescription();
		return getArticleDescriptions(details);
	}
	
	/**
	 * <p>
	 * Ascertains whether or not the article has a CIF as supplementary 
	 * data.  NOTE that if a supp file is deemed to be a CIF, then this
	 * method makes certain that its content type contains the official 
	 * CIF content-type (chemical/x-cif).
	 * </p>
	 * 
	 * @param details
	 * @return
	 */
	final private boolean isCifArticle(ArticleDescription details) {
		boolean isCifArticle = false;
		for (SupplementaryResourceDescription sfd : details.getSupplementaryResources()) {
			if (isCifFile(sfd)) {
				String oldContentType = sfd.getContentType();
				if (!oldContentType.contains(CIF_CONTENT_TYPE)) {
					sfd.appendToContentType(CIF_CONTENT_TYPE);
				}
				isCifArticle = true;
			}
		}
		return isCifArticle;
	}
	
	/**
	 * <p>
	 * When overidden in a subclass, this method should be a journal 
	 * specific method of investigating a SupplementaryFileDetails to 
	 * ascertain whether or not it describes a CIF.
	 * Ideally this would be based on a mime-type, but in reality will 
	 * probably be based around pattern-matching of URIs.
	 * </p>
	 * 
	 * @param details - details of the supplementary file to be investigated.
	 * 
	 * @return whether the file is a CIF or not.
	 */
	abstract protected boolean isCifFile(SupplementaryResourceDescription sfd);
	
	public List<DOI> getDois(IssueDescription issueDescription) {
		return crawler.getDois(issueDescription);
	}
	
	public List<DOI> getCurrentArticlesDois() {
		return crawler.getDoisForCurrentArticles();
	}
	
	public void setMaxArticlesToCrawl(int i) {
		crawler.setMaxArticlesToCrawl(i);
	}
	
	public IssueDescription getCurrentIssueDescription() {
		return crawler.getCurrentIssueDescription();
	}
	
}
