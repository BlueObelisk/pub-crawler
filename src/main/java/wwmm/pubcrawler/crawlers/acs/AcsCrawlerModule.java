package wwmm.pubcrawler.crawlers.acs;

import wwmm.pubcrawler.crawlers.IssueTocCrawlTaskFactory;
import wwmm.pubcrawler.crawlers.IssueTocParserFactory;
import wwmm.pubcrawler.crawlers.acs.AcsIssueTocCrawlTaskFactory;
import wwmm.pubcrawler.crawlers.acs.AcsIssueTocParserFactory;
import wwmm.pubcrawler.v2.inject.AbstractPublisherCrawlerModule;

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
