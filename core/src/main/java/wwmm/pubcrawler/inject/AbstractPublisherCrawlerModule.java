package wwmm.pubcrawler.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import uk.ac.cam.ch.wwmm.httpcrawler.HttpFetcher;
import wwmm.pubcrawler.crawlers.*;
import wwmm.pubcrawler.http.*;
import wwmm.pubcrawler.model.id.PublisherId;
import wwmm.pubcrawler.parsers.ArticleParserFactory;
import wwmm.pubcrawler.parsers.IssueTocParserFactory;
import wwmm.pubcrawler.repositories.Publisher;
import wwmm.pubcrawler.tasks.HttpCrawlTaskData;

/**
 * @author Sam Adams
 */
public abstract class AbstractPublisherCrawlerModule extends AbstractModule {

    @Override
    protected final void configure() {

        bind(String.class).annotatedWith(Publisher.class).toInstance(getPublisherId().getUid());

        if (getIssueTocParserFactoryType() != null) {
            bind(IssueTocParserFactory.class).to(getIssueTocParserFactoryType());
        }
        if (getArticleParserFactoryType() != null) {
            bind(ArticleParserFactory.class).to(getArticleParserFactoryType());
        }
        if (getIssueTocCrawlTaskFactoryType() != null) {
            bind(IssueTocCrawlTaskFactory.class).to(getIssueTocCrawlTaskFactoryType());
        }

        bind(JournalHandler.class).to(getJournalHandlerType());
        bind(IssueHandler.class).to(getIssueHandlerType());
        bind(ArticleHandler.class).to(getArticleHandlerType());
    }

    @Provides
    public RequestFactory<HttpCrawlTaskData, UriRequest> provideUriRequestFactory() {
        return new UriRequestFactory();
    }

    @Provides
    public Fetcher<UriRequest,DocumentResource> provideDocumentResourceFetcher(final HttpFetcher httpFetcher) {
        return new HtmlDocumentResourceHttpFetcher(httpFetcher);
    }

    protected Class<? extends JournalHandler> getJournalHandlerType() {
        return EnqueuingJournalHandler.class;
    }

    protected Class<? extends IssueHandler> getIssueHandlerType() {
        return EnqueuingIssueHandler.class;
    }

    protected Class<? extends ArticleHandler> getArticleHandlerType() {
        return NoOpArticleHandler.class;
    }

    protected abstract Class<? extends IssueTocParserFactory> getIssueTocParserFactoryType();

    protected abstract Class<? extends IssueTocCrawlTaskFactory> getIssueTocCrawlTaskFactoryType();

    protected abstract Class<? extends ArticleParserFactory> getArticleParserFactoryType();

    protected abstract PublisherId getPublisherId();
}
