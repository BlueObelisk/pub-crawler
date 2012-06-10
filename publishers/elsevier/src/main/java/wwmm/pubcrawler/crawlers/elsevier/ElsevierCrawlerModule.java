package wwmm.pubcrawler.crawlers.elsevier;

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
public class ElsevierCrawlerModule extends AbstractPublisherCrawlerModule {

    @Provides
    public PublicationListParserFactory<DocumentResource> providePublicationListParserFactory() {
        return new ElsevierPublicationListParserFactory();
    }

    @Provides
    public IssueTocParserFactory<DocumentResource> provideIssueTocParserFactory() {
        return new ElsevierIssueTocParserFactory();
    }

    @Override
    protected Class<? extends IssueTocParserFactory> getIssueTocParserFactoryType() {
        return ElsevierIssueTocParserFactory.class;
    }

    @Override
    protected Class<? extends IssueTocCrawlTaskFactory> getIssueTocCrawlTaskFactoryType() {
        return ElsevierIssueTocCrawlTaskFactory.class;
    }

    @Override
    protected Class<? extends ArticleParserFactory> getArticleParserFactoryType() {
        return null;
    }

    @Override
    protected PublisherId getPublisherId() {
        return Elsevier.PUBLISHER_ID;
    }

}
