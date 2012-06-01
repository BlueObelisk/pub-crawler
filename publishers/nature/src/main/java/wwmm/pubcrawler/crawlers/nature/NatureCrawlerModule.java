package wwmm.pubcrawler.crawlers.nature;

import com.google.inject.Provides;
import wwmm.pubcrawler.crawlers.IssueTocCrawlTaskFactory;
import wwmm.pubcrawler.http.DocumentResource;
import wwmm.pubcrawler.inject.AbstractPublisherCrawlerModule;
import wwmm.pubcrawler.model.id.PublisherId;
import wwmm.pubcrawler.parsers.IssueTocParserFactory;
import wwmm.pubcrawler.parsers.PublicationListParserFactory;

/**
 * @author Sam Adams
 */
public class NatureCrawlerModule extends AbstractPublisherCrawlerModule {

    @Provides
    public PublicationListParserFactory<DocumentResource> providePublicationListParserFactory() {
        return new NaturePublicationListParserFactory();
    }

    @Provides
    public IssueTocParserFactory<DocumentResource> provideIssueTocParserFactory() {
        return new NatureIssueTocParserFactory();
    }

    @Override
    protected Class<? extends IssueTocParserFactory> getIssueTocParserFactoryType() {
        return NatureIssueTocParserFactory.class;
    }

    @Override
    protected Class<? extends IssueTocCrawlTaskFactory> getIssueTocCrawlTaskFactoryType() {
        return NatureIssueTocCrawlTaskFactory.class;
    }

    @Override
    protected PublisherId getPublisherId() {
        return Nature.PUBLISHER_ID;
    }
}
