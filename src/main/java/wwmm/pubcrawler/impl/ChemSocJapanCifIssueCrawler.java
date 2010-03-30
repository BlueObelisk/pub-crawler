package wwmm.pubcrawler.impl;

import wwmm.pubcrawler.core.ChemSocJapanIssueCrawler;
import wwmm.pubcrawler.core.ChemSocJapanJournal;
import wwmm.pubcrawler.core.SupplementaryResourceDescription;

/**
 * <p>
 * Provides a method of crawling an issue of a journal published
 * by the Chemical Society of Japan, and only returning the details for
 * those articles that have a CIF as supplementary data.
 * </p>
 * 
 * @author Nick Day
 * @version 0.1;
 */
public class ChemSocJapanCifIssueCrawler extends CifIssueCrawler {
	
	public ChemSocJapanCifIssueCrawler(ChemSocJapanIssueCrawler crawler) {
		super(crawler);
	}
	
	public ChemSocJapanCifIssueCrawler(ChemSocJapanJournal journal) {
		super(new ChemSocJapanIssueCrawler(journal));
	}
	
	/**
	 * <p>
	 * A Chemical Society of Japan specific method of determining 
	 * whether a supplementary file refers to a CIF.
	 * </p>
	 * 
	 * @return true if the SupplementaryFileDetails described a 
	 * CIF file, false if not.
	 */
	@Override
	protected boolean isCifFile(SupplementaryResourceDescription sfd) {
		String linkText = sfd.getLinkText();
		if (linkText.contains("CIF")) {
			return true;
		} else {
			return false;
		}
	}


}
