package wwmm.pubcrawler.crawlers.wiley;

import wwmm.pubcrawler.crawlers.IssueTocCrawlTaskFactory;
import wwmm.pubcrawler.crawlers.IssueTocParserFactory;
import wwmm.pubcrawler.inject.AbstractPublisherCrawlerModule;

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
