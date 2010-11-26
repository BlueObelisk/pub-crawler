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
import wwmm.pubcrawler.core.IssueCrawler;
import wwmm.pubcrawler.core.Journal;
import wwmm.pubcrawler.core.JournalIndex;
import wwmm.pubcrawler.core.SupplementaryResourceDescription;
import wwmm.pubcrawler.journal.rsc.RscIssueCrawler;
import wwmm.pubcrawler.journal.rsc.RscJournalIndex;

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
	
	public RscCifIssueCrawler(IssueCrawler crawler) {
		super(crawler);
	}
	
	public RscCifIssueCrawler(Journal journal) {
		super(new RscIssueCrawler(journal));
	}
	
	public RscCifIssueCrawler(String abbreviation) {
		this(RscJournalIndex.getIndex().getJournal(abbreviation));
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
	protected boolean isCifFile(SupplementaryResourceDescription sfd) {
		String linkText = sfd.getLinkText();
		if (linkText.contains("Crystal structure") ||
				linkText.contains("crystal structure")) {
			return true;
		} else {
			return false;
		}
	}
	
	public static void main(String[] args) {
		RscCifIssueCrawler crawler = new RscCifIssueCrawler(RscJournalIndex.CHEMICAL_COMMUNICATIONS);
		for (ArticleDescription ad : crawler.getCurrentArticleDescriptions()) {
			System.out.println(ad.toString());
		}
	}

}
