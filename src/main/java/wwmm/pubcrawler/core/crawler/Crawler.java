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

import org.apache.log4j.Logger;

import wwmm.pubcrawler.core.utils.BasicHttpClient;
import wwmm.pubcrawler.core.utils.CrawlerHttpClient;

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

	protected BasicHttpClient httpClient;

	private static final Logger LOG = Logger.getLogger(Crawler.class);

	public Crawler() {
		httpClient = new CrawlerHttpClient();
	}

}