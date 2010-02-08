package wwmm.pubcrawler.impl;

import org.apache.log4j.Logger;

import wwmm.pubcrawler.core.AcsIssueCrawler;
import wwmm.pubcrawler.core.AcsJournal;
import wwmm.pubcrawler.core.SupplementaryResourceDetails;

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
	
	private static final Logger LOG = Logger.getLogger(AcsCifIssueCrawler.class);

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
	protected boolean isCifFile(SupplementaryResourceDetails sfd) {
		String uri = sfd.getURI().toString();
		if (uri.endsWith(".cif")) {
			return true;
		} else {
			return false;
		}
	}
	
}
