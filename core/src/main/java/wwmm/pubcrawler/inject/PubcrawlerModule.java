package wwmm.pubcrawler.inject;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import uk.ac.cam.ch.wwmm.httpcrawler.CrawlerResponse;
import wwmm.pubcrawler.controller.BasicHttpFetcher;
import wwmm.pubcrawler.controller.Fetcher;
import wwmm.pubcrawler.controller.URITask;
import wwmm.pubcrawler.crawlers.EnqueuingIssueHandler;
import wwmm.pubcrawler.crawlers.EnqueuingJournalHandler;
import wwmm.pubcrawler.crawlers.IssueHandler;
import wwmm.pubcrawler.crawlers.JournalHandler;
import wwmm.pubcrawler.crawler.DefaultTaskQueue;
import wwmm.pubcrawler.crawler.TaskQueue;

/**
 * @author Sam Adams
 */
public class PubcrawlerModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(TaskQueue.class).to(DefaultTaskQueue.class);
        bind(new TypeLiteral<Fetcher<URITask,CrawlerResponse>>(){}).to(BasicHttpFetcher.class);
        bind(JournalHandler.class).to(EnqueuingJournalHandler.class);
        bind(IssueHandler.class).to(EnqueuingIssueHandler.class);
    }
    
}
