package wwmm.pubcrawler.crawlers.rsc;

import com.google.inject.Provides;
import wwmm.pubcrawler.crawlers.IssueTocCrawlTaskFactory;
import wwmm.pubcrawler.http.DocumentResource;
import wwmm.pubcrawler.http.RequestFactory;
import wwmm.pubcrawler.inject.AbstractPublisherCrawlerModule;
import wwmm.pubcrawler.model.id.PublisherId;
import wwmm.pubcrawler.parsers.ArticleParserFactory;
import wwmm.pubcrawler.parsers.IssueTocParserFactory;
import wwmm.pubcrawler.parsers.PublicationListParserFactory;

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
    protected Class<? extends ArticleParserFactory> getArticleParserFactoryType() {
        return null;
    }

    @Provides
    public PublicationListParserFactory<DocumentResource> getPublicationListParserFactory() {
        return new RscPublicationListParserFactory();
    }

    @Provides
    public IssueTocParserFactory<DocumentResource> getIssueTocParserFactory() {
        return new RscIssueTocParserFactory();
    }

    @Provides
    public RequestFactory<RscIssueTocCrawlTaskData, RscIssueTocRequest> getRscIssueTocRequestFactory() {
        return new RscIssueTocRequestFactory();
    }

    @Override
    protected PublisherId getPublisherId() {
        return Rsc.PUBLISHER_ID;
    }

}