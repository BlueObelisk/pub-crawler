package wwmm.pubcrawler.core;

import org.apache.log4j.Logger;

import wwmm.pubcrawler.BasicHttpClient;

/**
 * <p>
 * The abstract <code>Crawler</code> class is intended to be used as a
 * superclass for any web crawler classes. It contains objects (e.g. a HTTP
 * client) and methods generic to the use and manipulation of web resources.
 * </p>
 * 
 * 
 * @todo consider making this a helper class or a library rather than a
 *       superclass, it doesn't have any state...
 * @author Nick Day
 * @version 1.1
 * 
 */
public abstract class Crawler {

	BasicHttpClient httpClient;

	private static final Logger LOG = Logger.getLogger(Crawler.class);

	public Crawler() {
		httpClient = new CrawlerHttpClient();
	}

}
