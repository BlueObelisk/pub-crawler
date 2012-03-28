package wwmm.pubcrawler.crawlers.elsevier;

import wwmm.pubcrawler.crawlers.IssueTocCrawlTaskFactory;
import wwmm.pubcrawler.crawlers.IssueTocParserFactory;
import wwmm.pubcrawler.crawlers.elsevier.ElsevierIssueTocCrawlTaskFactory;
import wwmm.pubcrawler.crawlers.elsevier.ElsevierIssueTocParserFactory;
import wwmm.pubcrawler.v2.inject.AbstractPublisherCrawlerModule;

/**
 * @author Sam Adams
 */
public class ElsevierCrawlerModule extends AbstractPublisherCrawlerModule {

    @Override
    protected Class<? extends IssueTocParserFactory> getIssueTocParserFactoryType() {
        return ElsevierIssueTocParserFactory.class;
    }

    @Override
    protected Class<? extends IssueTocCrawlTaskFactory> getIssueTocCrawlTaskFactoryType() {
        return ElsevierIssueTocCrawlTaskFactory.class;
    }

}
