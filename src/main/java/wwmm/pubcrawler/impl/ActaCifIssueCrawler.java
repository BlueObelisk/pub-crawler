package wwmm.pubcrawler.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.URIException;

import wwmm.pubcrawler.core.ActaIssueCrawler;
import wwmm.pubcrawler.core.ActaJournal;
import wwmm.pubcrawler.core.SupplementaryResourceDescription;

/**
 * <p>
 * Provides a method of crawling an issue of a journal published
 * by Acta Crystallographica, and only returning the details for
 * those articles that have a CIF as supplementary data.
 * </p>
 * 
 * @author Nick Day
 * @version 0.1;
 */
public class ActaCifIssueCrawler extends CifIssueCrawler {

	public ActaCifIssueCrawler(ActaIssueCrawler crawler) {
		super(crawler);
	}
	
	public ActaCifIssueCrawler(ActaJournal journal) {
		super(new ActaIssueCrawler(journal));
	}

	/**
	 * <p>
	 * An Acta Crystallographic specific method of determining 
	 * whether a supplementary file refers to a CIF.
	 * </p>
	 * 
	 * @return true if the SupplementaryFileDetails described a 
	 * CIF file, false if not.
	 */
	@Override
	protected boolean isCifFile(SupplementaryResourceDescription sfd) {
		Pattern pattern = Pattern.compile("http://scripts.iucr.org/cgi-bin/sendcif\\?.{6}sup\\d+");
		Matcher matcher = null;
		try {
			matcher = pattern.matcher(sfd.getURI().getURI());
		} catch (URIException e) {
			throw new RuntimeException("Error getting URI string.", e);
		}
		if (matcher.find()) {
			return true;
		}
		return false;
	}
	
}
