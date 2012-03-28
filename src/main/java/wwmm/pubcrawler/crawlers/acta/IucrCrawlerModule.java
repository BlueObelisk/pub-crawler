package wwmm.pubcrawler.crawlers.acta;

import wwmm.pubcrawler.crawlers.IssueTocCrawlTaskFactory;
import wwmm.pubcrawler.crawlers.IssueTocParserFactory;
import wwmm.pubcrawler.v2.inject.AbstractPublisherCrawlerModule;

/**
 * @author Sam Adams
 */
public class IucrCrawlerModule extends AbstractPublisherCrawlerModule {

    @Override
    protected Class<? extends IssueTocParserFactory> getIssueTocParserFactoryType() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected Class<? extends IssueTocCrawlTaskFactory> getIssueTocCrawlTaskFactoryType() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

}
