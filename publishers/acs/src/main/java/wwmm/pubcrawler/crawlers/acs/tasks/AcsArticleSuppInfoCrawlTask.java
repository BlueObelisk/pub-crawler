package wwmm.pubcrawler.crawlers.acs.tasks;

import wwmm.pubcrawler.crawlers.CrawlTaskRunner;
import wwmm.pubcrawler.http.DocumentResource;
import wwmm.pubcrawler.http.Fetcher;
import wwmm.pubcrawler.http.RequestFactory;
import wwmm.pubcrawler.http.UriRequest;
import wwmm.pubcrawler.processors.ArticleProcessor;
import wwmm.pubcrawler.tasks.*;

import javax.inject.Inject;

/**
 * @author Sam Adams
 */
public class AcsArticleSuppInfoCrawlTask implements TaskSpecification<ArticleCrawlTaskData> {

    public static final AcsArticleSuppInfoCrawlTask INSTANCE = new AcsArticleSuppInfoCrawlTask();

    @Override
    public Class<Runner> getRunnerClass() {
        return Runner.class;
    }

    @Override
    public Marshaller<ArticleCrawlTaskData> getDataMarshaller() {
        return new ArticleCrawlTaskDataMarshaller();
    }

    public static class Runner extends CrawlTaskRunner<ArticleCrawlTaskData, UriRequest, DocumentResource> {

        @Inject
        public Runner(final Fetcher<UriRequest, DocumentResource> fetcher, final RequestFactory<HttpCrawlTaskData, UriRequest> requestFactory, final ArticleProcessor<DocumentResource> resourceProcessor) {
            super(fetcher, requestFactory, resourceProcessor);
        }
    }

}
