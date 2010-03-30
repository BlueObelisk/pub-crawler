package wwmm.pubcrawler.impl;

import static wwmm.pubcrawler.core.CrawlerConstants.CIF_CONTENT_TYPE;

import java.util.ArrayList;
import java.util.List;

import wwmm.pubcrawler.core.ArticleDescription;
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
	final public List<ArticleDescription> getDetailsForArticles(IssueDescription details) {
		List<ArticleDescription> adList = crawler.getArticleDescriptions(details);
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
	final public List<ArticleDescription> getDetailsForCurrentArticles() {
		IssueDescription details = crawler.getCurrentIssueDescription();
		return getDetailsForArticles(details);
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
	
}
