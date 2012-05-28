package wwmm.pubcrawler.crawlers.rsc;

import wwmm.pubcrawler.crawlers.IssueTocCrawlTaskFactory;
import wwmm.pubcrawler.model.id.PublisherId;
import wwmm.pubcrawler.parsers.IssueTocParserFactory;
import wwmm.pubcrawler.inject.AbstractPublisherCrawlerModule;

/**
 * @author Sam Adams
 */
public class RscCrawlerModule extends AbstractPublisherCrawlerModule {

    @Override
    protected Class<? extends IssueTocParserFactory> getIssueTocParserFactoryType() {
        return RscIssueTocParserFactory.class;
    }

    @Override
    protected Class<? extends IssueTocCrawlTaskFactory> getIssueTocCrawlTaskFactoryType() {
        return RscIssueTocCrawlTaskFactory.class;
    }

    @Override
    protected PublisherId getPublisherId() {
        return Rsc.PUBLISHER_ID;
    }

}