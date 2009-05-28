package wwmm.pubcrawler.impl;

import wwmm.pubcrawler.core.RscIssueCrawler;
import wwmm.pubcrawler.core.SupplementaryResourceDetails;

/**
 * <p>
 * Provides a method of crawling an issue of a journal published
 * by the Royal Society of Chemistry, and only returning the details for
 * those articles that have a CIF as supplementary data.
 * </p>
 * 
 * @author Nick Day
 * @version 0.1;
 */
public class RscCifIssueCrawler extends CifIssueCrawler {
	
	public RscCifIssueCrawler(RscIssueCrawler crawler) {
		super(crawler);
	}
	
	/**
	 * <p>
	 * A Royal Society of Chemistry specific method of determining 
	 * whether a supplementary file refers to a CIF.
	 * </p>
	 * 
	 * @return true if the SupplementaryFileDetails described a 
	 * CIF file, false if not.
	 */
	@Override
	protected boolean isCifFile(SupplementaryResourceDetails sfd) {
		String linkText = sfd.getLinkText();
		if (linkText.contains("Crystal structure") ||
				linkText.contains("crystal structure")) {
			return true;
		} else {
			return false;
		}
	}

}