package wwmm.pubcrawler.inject;

import com.google.inject.AbstractModule;
import wwmm.pubcrawler.crawlers.IssueTocCrawlTaskFactory;
import wwmm.pubcrawler.crawlers.IssueTocParserFactory;

/**
 * @author Sam Adams
 */
public abstract class AbstractPublisherCrawlerModule extends AbstractModule {

    @Override
    protected final void configure() {
        if (getIssueTocParserFactoryType() != null) {
            bind(IssueTocParserFactory.class).to(getIssueTocParserFactoryType());
        }
        if (getIssueTocCrawlTaskFactoryType() != null) {
            bind(IssueTocCrawlTaskFactory.class).to(getIssueTocCrawlTaskFactoryType());
        }
    }

    protected abstract Class<? extends IssueTocParserFactory> getIssueTocParserFactoryType();

    protected abstract Class<? extends IssueTocCrawlTaskFactory> getIssueTocCrawlTaskFactoryType();

}
