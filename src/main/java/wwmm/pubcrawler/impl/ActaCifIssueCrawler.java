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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import wwmm.pubcrawler.core.ActaIssueCrawler;
import wwmm.pubcrawler.core.ActaJournal;
import wwmm.pubcrawler.core.ArticleDescription;
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
		Matcher matcher = pattern.matcher(sfd.getURL());
		if (matcher.find()) {
			return true;
		}
		return false;
	}
	
	public static void main(String[] args) {
		ActaCifIssueCrawler crawler = new ActaCifIssueCrawler(ActaJournal.SECTION_C);
		crawler.setMaxArticlesToCrawl(3);
		for (ArticleDescription ad : crawler.getCurrentArticleDescriptions()) {
			System.out.println(ad.toString());
		}
	}
	
}
