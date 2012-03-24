package wwmm.pubcrawler.v2.inject;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import wwmm.pubcrawler.controller.BasicHttpFetcher;
import wwmm.pubcrawler.controller.Fetcher;
import wwmm.pubcrawler.controller.URITask;
import wwmm.pubcrawler.v2.crawler.DefaultTaskQueue;
import wwmm.pubcrawler.v2.crawler.TaskQueue;

/**
 * @author Sam Adams
 */
public class PubcrawlerModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(TaskQueue.class).to(DefaultTaskQueue.class);
        bind(new TypeLiteral<Fetcher<URITask,CrawlerResponse>>(){}).to(BasicHttpFetcher.class);
    }
    
}
