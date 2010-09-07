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

import wwmm.pubcrawler.core.ArticleDescription;
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
	
	public static void main(String[] args) {
		ChemSocJapanCifIssueCrawler crawler = new ChemSocJapanCifIssueCrawler(ChemSocJapanJournal.CHEMISTRY_LETTERS);
		crawler.setMaxArticlesToCrawl(10);
		for (ArticleDescription ad : crawler.getCurrentArticleDescriptions()) {
			System.out.println(ad.toString());
		}
	}


}
