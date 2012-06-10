package wwmm.pubcrawler.crawlers.wiley;

import com.google.inject.Provides;
import wwmm.pubcrawler.crawlers.IssueTocCrawlTaskFactory;
import wwmm.pubcrawler.http.DocumentResource;
import wwmm.pubcrawler.inject.AbstractPublisherCrawlerModule;
import wwmm.pubcrawler.model.id.PublisherId;
import wwmm.pubcrawler.parsers.ArticleParserFactory;
import wwmm.pubcrawler.parsers.IssueTocParserFactory;
import wwmm.pubcrawler.parsers.PublicationListParserFactory;

/**
 * @author Sam Adams
 */
public class WileyCrawlerModule extends AbstractPublisherCrawlerModule {

    @Provides
    public PublicationListParserFactory<DocumentResource> providePublicationListParserFactory() {
        return new WileyPublicationListParserFactory();
    }

    @Provides
    public IssueTocParserFactory<DocumentResource> provideIssueTocParserFactory() {
        return new WileyIssueTocParserFactory();
    }

    @Override
    protected Class<? extends IssueTocParserFactory> getIssueTocParserFactoryType() {
        return WileyIssueTocParserFactory.class;
    }

    @Override
    protected Class<? extends IssueTocCrawlTaskFactory> getIssueTocCrawlTaskFactoryType() {
        return WileyIssueTocCrawlTaskFactory.class;
    }

    @Override
    protected Class<? extends ArticleParserFactory> getArticleParserFactoryType() {
        return null;
    }

    @Override
    protected PublisherId getPublisherId() {
        return Wiley.PUBLISHER_ID;
    }
}
