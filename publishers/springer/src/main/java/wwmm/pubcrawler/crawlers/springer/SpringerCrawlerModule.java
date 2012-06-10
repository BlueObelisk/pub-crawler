package wwmm.pubcrawler.crawlers.springer;

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
public class SpringerCrawlerModule extends AbstractPublisherCrawlerModule {

    @Provides
    public PublicationListParserFactory<DocumentResource> providePublicationListParserFactory() {
        return new SpringerPublicationListParserFactory();
    }

    @Provides
    public IssueTocParserFactory<DocumentResource> provideIssueTocParserFactory() {
        return new SpringerIssueTocParserFactory();
    }

    @Override
    protected Class<? extends IssueTocParserFactory> getIssueTocParserFactoryType() {
        return SpringerIssueTocParserFactory.class;
    }

    @Override
    protected Class<? extends IssueTocCrawlTaskFactory> getIssueTocCrawlTaskFactoryType() {
        return SpringerIssueTocCrawlTaskFactory.class;
    }

    @Override
    protected Class<? extends ArticleParserFactory> getArticleParserFactoryType() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected PublisherId getPublisherId() {
        return Springer.PUBLISHER_ID;
    }

}
