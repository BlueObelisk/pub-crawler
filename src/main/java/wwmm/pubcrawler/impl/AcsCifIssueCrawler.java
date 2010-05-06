package wwmm.pubcrawler.impl;

import static wwmm.pubcrawler.core.CrawlerConstants.CIF_CONTENT_TYPE;
import wwmm.pubcrawler.core.AcsIssueCrawler;
import wwmm.pubcrawler.core.AcsJournal;
import wwmm.pubcrawler.core.ArticleDescription;
import wwmm.pubcrawler.core.SupplementaryResourceDescription;

/**
 * <p>
 * Provides a method of crawling an issue of a journal published
 * by the American Chemical Society, and only returning the details for
 * those articles that have a CIF as supplementary data.
 * </p>
 * 
 * @author Nick Day
 * @version 0.1;
 */
public class AcsCifIssueCrawler extends CifIssueCrawler {

	public AcsCifIssueCrawler(AcsIssueCrawler crawler) {
		super(crawler);
	}
	
	public AcsCifIssueCrawler(AcsJournal journal) {
		super(new AcsIssueCrawler(journal));
	}
	
	/**
	 * <p>
	 * An American Chemical Society specific method of determining 
	 * whether a supplementary file refers to a CIF.
	 * </p>
	 * 
	 * @return true if the SupplementaryFileDetails described a 
	 * CIF file, false if not.
	 */
	@Override
	protected boolean isCifFile(SupplementaryResourceDescription sfd) {
		String contentType = sfd.getContentType();
		if (contentType!= null && contentType.contains(CIF_CONTENT_TYPE)) {
			return true;
		} else {
			return false;
		}
	}
	
	public static void main(String[] args) {
		AcsCifIssueCrawler crawler = new AcsCifIssueCrawler(AcsJournal.CRYSTAL_GROWTH_AND_DESIGN);
		crawler.setMaxArticlesToCrawl(10);
		for (ArticleDescription ad : crawler.getCurrentArticleDescriptions()) {
			System.out.println(ad.toString());
		}
	}
	
}
