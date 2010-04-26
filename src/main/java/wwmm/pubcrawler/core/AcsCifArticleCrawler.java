package wwmm.pubcrawler.core;

import static wwmm.pubcrawler.core.CrawlerConstants.CIF_CONTENT_TYPE;

import java.util.ArrayList;
import java.util.List;

public class AcsCifArticleCrawler extends AcsArticleCrawler {

	public static List<SupplementaryResourceDescription> getCifResources(ArticleDescription articleDescription) {
		List<SupplementaryResourceDescription> resources = articleDescription.getSupplementaryResources();
		List<SupplementaryResourceDescription> cifResources = new ArrayList<SupplementaryResourceDescription>(1);
		for (SupplementaryResourceDescription srd : resources) {
			String filename = srd.getFileId();
			if (filename.endsWith(".cif")) {
				String oldContentType = srd.getContentType();
				if (!oldContentType.contains(CIF_CONTENT_TYPE)) {
					srd.appendToContentType(CIF_CONTENT_TYPE);
				}
				cifResources.add(srd);
			}
		}
		return cifResources;
	}

}
