package wwmm.pubcrawler.v2.inject.crawlers;

import wwmm.pubcrawler.crawlers.IssueTocCrawlTaskFactory;
import wwmm.pubcrawler.crawlers.IssueTocParserFactory;
import wwmm.pubcrawler.crawlers.wiley.WileyIssueTocCrawlTaskFactory;
import wwmm.pubcrawler.crawlers.wiley.WileyIssueTocParserFactory;

/**
 * @author Sam Adams
 */
public class WileyCrawlerModule extends AbstractPublisherCrawlerModule {

    @Override
    protected Class<? extends IssueTocParserFactory> getIssueTocParserFactoryType() {
        return WileyIssueTocParserFactory.class;
    }

    @Override
    protected Class<? extends IssueTocCrawlTaskFactory> getIssueTocCrawlTaskFactoryType() {
        return WileyIssueTocCrawlTaskFactory.class;
    }
    
}
