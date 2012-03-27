package wwmm.pubcrawler.v2.inject.crawlers;

import wwmm.pubcrawler.crawlers.IssueTocCrawlTaskFactory;
import wwmm.pubcrawler.crawlers.IssueTocParserFactory;
import wwmm.pubcrawler.crawlers.acs.AcsIssueTocCrawlTaskFactory;
import wwmm.pubcrawler.crawlers.acs.AcsIssueTocParserFactory;

/**
 * @author Sam Adams
 */
public class AcsCrawlerModule extends AbstractPublisherCrawlerModule {

    @Override
    protected Class<? extends IssueTocParserFactory> getIssueTocParserFactoryType() {
        return AcsIssueTocParserFactory.class;
    }

    @Override
    protected Class<? extends IssueTocCrawlTaskFactory> getIssueTocCrawlTaskFactoryType() {
        return AcsIssueTocCrawlTaskFactory.class;
    }

}
