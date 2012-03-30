package wwmm.pubcrawler.crawlers.springer;

import wwmm.pubcrawler.crawlers.IssueTocCrawlTaskFactory;
import wwmm.pubcrawler.crawlers.IssueTocParserFactory;
import wwmm.pubcrawler.crawlers.wiley.WileyIssueTocCrawlTaskFactory;
import wwmm.pubcrawler.crawlers.wiley.WileyIssueTocParserFactory;
import wwmm.pubcrawler.v2.inject.AbstractPublisherCrawlerModule;

/**
 * @author Sam Adams
 */
public class SpringerCrawlerModule extends AbstractPublisherCrawlerModule {

    @Override
    protected Class<? extends IssueTocParserFactory> getIssueTocParserFactoryType() {
        return SpringerIssueTocParserFactory.class;
    }

    @Override
    protected Class<? extends IssueTocCrawlTaskFactory> getIssueTocCrawlTaskFactoryType() {
        return SpringerIssueTocCrawlTaskFactory.class;
    }
    
}
