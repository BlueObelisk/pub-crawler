package wwmm.pubcrawler.crawlers.acs;

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
public class AcsCrawlerModule extends AbstractPublisherCrawlerModule {

    @Provides
    public PublicationListParserFactory<DocumentResource> providePublicationListParserFactory() {
        return new AcsPublicationListParserFactory();
    }

    @Provides
    public IssueTocParserFactory<DocumentResource> provideIssueTocParserFactory() {
        return new AcsIssueTocParserFactory();
    }


    @Override
    protected Class<? extends IssueTocParserFactory> getIssueTocParserFactoryType() {
        return AcsIssueTocParserFactory.class;
    }

    @Override
    protected Class<? extends IssueTocCrawlTaskFactory> getIssueTocCrawlTaskFactoryType() {
        return AcsIssueTocCrawlTaskFactory.class;
    }

    @Override
    protected PublisherId getPublisherId() {
        return Acs.PUBLISHER_ID;
    }
}
